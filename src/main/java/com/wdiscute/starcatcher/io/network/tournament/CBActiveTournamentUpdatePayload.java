package com.wdiscute.starcatcher.io.network.tournament;


import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.network.ToClientPacket;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record CBActiveTournamentUpdatePayload(List<GameProfile> listSignups,
                                              Tournament tour) implements ToClientPacket
{

    public static CBActiveTournamentUpdatePayload helper(Player player, Tournament tournament)
    {
        if (player.level().isClientSide) throw new RuntimeException();
        List<GameProfile> list = new ArrayList<>();
        for (var entry : tournament.playerScores)
        {
            GameProfileCache profileCache = player.level().getServer().getProfileCache();
            if (profileCache != null)
            {
                Optional<GameProfile> gameProfile = profileCache.get(entry.playerUUID);
                gameProfile.ifPresent(list::add);
            }
        }

        return new CBActiveTournamentUpdatePayload(list, tournament);
    }

    public static final StreamCodec<GameProfile> GAME_PROFILE_STREAM_CODEC = StreamCodec.composite(
            StreamCodec.UUID, GameProfile::getId,
            StreamCodec.STRING, GameProfile::getName,
            GameProfile::new
    );

    public static final StreamCodec<List<GameProfile>> GAME_PROFILE_STREAM_CODEC_LIST = GAME_PROFILE_STREAM_CODEC.list();


    public static final StreamCodec<CBActiveTournamentUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            GAME_PROFILE_STREAM_CODEC_LIST, CBActiveTournamentUpdatePayload::listSignups,
            Tournament.STREAM_CODEC, CBActiveTournamentUpdatePayload::tour,
            CBActiveTournamentUpdatePayload::new
    );

    @Override
    public void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player) {
        TournamentOverlay.onTournamentReceived(tour, listSignups);

    }
}
