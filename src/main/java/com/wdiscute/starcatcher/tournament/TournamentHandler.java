package com.wdiscute.starcatcher.tournament;

import com.google.common.collect.BoundType;
import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.io.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.stand.CBStandTournamentUpdatePayload;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TournamentHandler
{
    private static final List<Tournament> finishedTournaments = new ArrayList<>();
    private static final List<Tournament> cancelledTournaments = new ArrayList<>();
    private static final List<Tournament> activeTournaments = new ArrayList<>();
    private static final List<Tournament> setupTournaments = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(TournamentHandler.class);

    public static Tournament getTournamentOrNew(UUID uuid)
    {
        for (Tournament t : setupTournaments)
        {
            if (t.tournamentUUID.equals(uuid)) return t;
        }

        for (Tournament t : activeTournaments)
        {
            if (t.tournamentUUID.equals(uuid)) return t;
        }

        for (Tournament t : finishedTournaments)
        {
            if (t.tournamentUUID.equals(uuid)) return t;
        }

        Tournament tournament = new Tournament(
                uuid,
                "Unnamed Tournament",
                Tournament.Status.SETUP,
                null,
                new ArrayList<>(),
                new TournamentSettings(
                        TournamentSettings.Scoring.SIMPLE,
                        48000,
                        0,
                        0,
                        SingleStackContainer.EMPTY_LIST),
                SingleStackContainer.EMPTY_LIST,
                200
        );

        setupTournaments.add(tournament);
        return tournament;
    }

    public static void sendActiveTournamentUpdateToClient(ServerPlayer sp, Tournament tournament)
    {
        if(sp == null || tournament == null) return;
        ModNetworking.sendToPlayer(sp, CBActiveTournamentUpdatePayload.helper(sp, tournament));
    }

    public static void clearTournamentToClient(ServerPlayer sp)
    {
        if (sp == null) return;
        ModNetworking.sendToPlayer(sp, new CBClearTournamentPayload(":)"));
    }

    public static void startTournament(Player playerWhoStartedTheTournament, Tournament tournament)
    {
        Level level = playerWhoStartedTheTournament.level();

        for (var entry : tournament.playerScores)
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.playerUUID);
            sendActiveTournamentUpdateToClient(player, tournament);
        }

        for (var playerScore : tournament.playerScores)
        {
            MinecraftServer server = playerWhoStartedTheTournament.level().getServer();
            ServerPlayer player = server.getPlayerList().getPlayer(playerScore.playerUUID);
            if (player != null)
            {
                player.sendSystemMessage(Component.literal(tournament.name + " has started!"));
                player.sendSystemMessage(Component.literal("Press [Tab] to toggle the tournament's scoreboard.").setStyle(Style.EMPTY.withColor(-6250336)));
            }
        }

        //send to all players to update stand screens
        ModNetworking.sendToAllPlayers(CBStandTournamentUpdatePayload.helper(playerWhoStartedTheTournament.level(), tournament));

        activeTournaments.add(tournament);
        setupTournaments.remove(tournament);
        tournament.status = Tournament.Status.ACTIVE;
        tournament.lastsUntilEpoch = System.currentTimeMillis() + tournament.settings.durationInTicks / 20 * 1000;
    }

    public static void cancelTournament(Level level, Tournament tournament)
    {
        for (var entry : tournament.playerScores)
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.playerUUID);
            sendActiveTournamentUpdateToClient(player, tournament);
            clearTournamentToClient(player);
        }

        setupTournaments.remove(tournament);
        activeTournaments.remove(tournament);
        finishedTournaments.add(tournament);
        tournament.status = Tournament.Status.CANCELLED;

        ModNetworking.sendToAllPlayers(CBStandTournamentUpdatePayload.helper(level, tournament));
    }

    public static void addScore(Player playerToAwardScoreTo, FishProperties fp, boolean perfectCatch, int size, int weight)
    {
        if (playerToAwardScoreTo.level().isClientSide) return;
        for (Tournament t : activeTournaments)
        {
            t.playerScores.forEach(p ->
                    {
                        if (p.playerUUID.equals(playerToAwardScoreTo.getUUID()))
                        {
                            //simple scoring
                            if (t.settings.scoring.equals(TournamentSettings.Scoring.SIMPLE))
                            {
                                p.addScore(1);
                            }

                            Level level = playerToAwardScoreTo.level();
                            for (var entry : t.playerScores)
                            {
                                ServerPlayer sp = level.getServer().getPlayerList().getPlayer(entry.playerUUID);
                                sendActiveTournamentUpdateToClient(sp, t);
                            }
                        }
                    }
            );
        }
    }

    public static void setName(ServerPlayer player, UUID uuid, String name)
    {
        if (player.level().isClientSide) return;
        for (Tournament t : setupTournaments)
        {
            if (t.tournamentUUID.equals(uuid) && player.getUUID().equals(t.owner))
            {
                t.name = name;
                ModNetworking.sendToAllPlayers(CBStandTournamentUpdatePayload.helper(player.level(), t));
            }
        }
    }

    public static void tick(MinecraftServer server)
    {
        long levelTicks = server.getTickCount();
        if (levelTicks % 20 != 0) return;

        List<Tournament> finished = new ArrayList<>();
        for (Tournament t : activeTournaments)
        {
            if (System.currentTimeMillis() >= t.lastsUntilEpoch)
            {
                finished.add(t);
                t.status = Tournament.Status.FINISHED;
                UUID winner = null;
                int bestScore = 0;

                for (TournamentPlayerScore playerscore : t.playerScores)
                {
                    if (playerscore.score > bestScore)
                    {
                        bestScore = playerscore.score;
                        winner = playerscore.playerUUID;
                    }
                }

                String winnerString = "???";

                if (winner != null)
                    winnerString = server.getProfileCache().get(winner).get().getName();

                for (var playerScore : t.playerScores)
                {
                    ServerPlayer player = server.getPlayerList().getPlayer(playerScore.playerUUID);
                    if (player != null)
                    {
                        TournamentHandler.clearTournamentToClient(player);
                        player.sendSystemMessage(Component.literal(t.name + " has ended! The winner is " + winnerString + "!"));
                    }
                }

            }
        }

        finishedTournaments.addAll(finished);
        activeTournaments.removeAll(finished);
    }

    public static List<Tournament> getAll()
    {
        List<Tournament> t = new ArrayList<>();
        t.addAll(activeTournaments);
        t.addAll(setupTournaments);
        t.addAll(finishedTournaments);
        return t;
    }

    public static void setAll(List<Tournament> tournaments)
    {
        activeTournaments.clear();
        activeTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.ACTIVE)).toList());

        cancelledTournaments.clear();
        cancelledTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.CANCELLED)).toList());

        finishedTournaments.clear();
        finishedTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.FINISHED)).toList());

        setupTournaments.clear();
        setupTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.SETUP)).toList());
    }

    public static Tournament getTournamentForPlayer(Player player)
    {
        AtomicReference<Tournament> tToReturn = new AtomicReference<>();
        activeTournaments.forEach(t ->
        {
            Stream<TournamentPlayerScore> tournamentPlayerScoreStream = t.playerScores.stream().filter(p -> p.playerUUID.equals(player.getUUID()));
            if (tournamentPlayerScoreStream.findFirst().isPresent()) tToReturn.set(t);
        });

        return tToReturn.get();
    }
}
