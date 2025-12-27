package com.wdiscute.starcatcher.tournament;

import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.stand.CBStandTournamentUpdatePayload;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TournamentHandler
{
    private static final List<Tournament> finishedTournaments = new ArrayList<>();
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
                new HashMap<>(),
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

    public static void startTournament(Player playerWhoStartedTheTournament, Tournament tournament)
    {
        Level level = playerWhoStartedTheTournament.level();

        for (Map.Entry<UUID, TournamentPlayerScore> entry : tournament.playerScores.entrySet())
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.getKey());
            sendActiveTournamentUpdateToClient(player, tournament);
        }

        //send to all players to update stand screens
        ModNetworking.sendToAllPlayers(CBStandTournamentUpdatePayload.helper(playerWhoStartedTheTournament, tournament));

        activeTournaments.add(tournament);
        setupTournaments.remove(tournament);
        tournament.status = Tournament.Status.ACTIVE;
        tournament.lastsUntilEpoch = System.currentTimeMillis() + tournament.settings.durationInTicks / 20 * 1000;
    }

    public static void cancelTournament(Player ownerPlayer, Tournament tournament)
    {
        Level level = ownerPlayer.level();

        for (Map.Entry<UUID, TournamentPlayerScore> entry : tournament.playerScores.entrySet())
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.getKey());
            sendActiveTournamentUpdateToClient(player, tournament);
        }

        activeTournaments.remove(tournament);
        finishedTournaments.add(tournament);
        tournament.status = Tournament.Status.CANCELLED;

        ModNetworking.sendToAllPlayers(CBStandTournamentUpdatePayload.helper(ownerPlayer, tournament));
    }


    public static void addScore(Player playerToAwardScoreTo, FishProperties fp, boolean perfectCatch, int size, int weight)
    {
        if (playerToAwardScoreTo.level().isClientSide) return;
        for (Tournament t : activeTournaments)
        {
            //update score
            if (t.playerScores.containsKey(playerToAwardScoreTo.getUUID()))
            {
                //simple scoring
                if (t.settings.scoring.equals(TournamentSettings.Scoring.SIMPLE))
                {
                    t.playerScores.get(playerToAwardScoreTo.getUUID()).addScore(1);
                }

                //weight scoring
                if (t.settings.scoring.equals(TournamentSettings.Scoring.WEIGHT))
                {
                    t.playerScores.get(playerToAwardScoreTo.getUUID()).addScore(weight);
                }

                //weight scoring
                if (t.settings.scoring.equals(TournamentSettings.Scoring.WEIGHT))
                {
                    t.playerScores.get(playerToAwardScoreTo.getUUID()).addScore(weight);
                }


                Level level = playerToAwardScoreTo.level();
                for (Map.Entry<UUID, TournamentPlayerScore> entry : t.playerScores.entrySet())
                {
                    ServerPlayer sp = level.getServer().getPlayerList().getPlayer(entry.getKey());
                    sendActiveTournamentUpdateToClient(sp, t);
                }
            }
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
                ModNetworking.sendToAllPlayers(CBStandTournamentUpdatePayload.helper(player, t));
            }
        }
    }

    public static void tick(MinecraftServer server)
    {
        long levelTicks = server.getTickCount();
        if (levelTicks % 20 != 0) return;

        List<Tournament> finishedTournaments = new ArrayList<>();
        for (Tournament t : activeTournaments)
        {
            if (System.currentTimeMillis() >= t.lastsUntilEpoch)
            {
                finishedTournaments.add(t);

                UUID winner = null;
                int bestScore = 0;

                for (Map.Entry<UUID, TournamentPlayerScore> entry : t.playerScores.entrySet())
                {
                    if (entry.getValue().score > bestScore)
                    {
                        bestScore = entry.getValue().score;
                        winner = entry.getKey();
                    }
                }

                Level level = null;

                if (winner == null)
                {
                }
                else
                {
                    GameProfileCache profileCache = server.getProfileCache();

                    if (profileCache == null)
                    {
                    }
                    else
                    {
                        Optional<GameProfile> gameProfile = server.getProfileCache().get(winner);

                        if (gameProfile.isPresent())
                        {
                        }
                        else
                        {
                        }
                    }


                }


            }
        }

        activeTournaments.removeAll(finishedTournaments);
    }

    //getters
    public static Tournament getSetupTournamentOrNull(UUID uuid)
    {
        for (Tournament t : setupTournaments)
        {
            if(t.tournamentUUID.equals(uuid)) return t;
        }
        return null;
    }

    public static Tournament getActiveTournamentOrNull(UUID uuid)
    {
        for (Tournament t : activeTournaments)
        {
            if(t.tournamentUUID.equals(uuid)) return t;
        }
        return null;
    }

    public static Tournament getFinishedTournamentOrNull(UUID uuid)
    {
        for (Tournament t : finishedTournaments)
        {
            if(t.tournamentUUID.equals(uuid)) return t;
        }
        return null;
    }

}
