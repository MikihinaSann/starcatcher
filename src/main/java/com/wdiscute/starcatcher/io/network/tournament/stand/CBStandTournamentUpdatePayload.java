package com.wdiscute.starcatcher.io.network.tournament.stand;


import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.network.ToClientPacket;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.Tournament;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record CBStandTournamentUpdatePayload(List<GameProfile> listSignups, Tournament tour) implements ToClientPacket
{

    public static CBStandTournamentUpdatePayload helper(Level level, Tournament tournament)
    {
        if (level.isClientSide) throw new RuntimeException();
        List<GameProfile> list = new ArrayList<>();
        for (var entry : tournament.playerScores)
        {
            GameProfileCache profileCache = level.getServer().getProfileCache();
            if (profileCache != null)
            {
                Optional<GameProfile> gameProfile = profileCache.get(entry.playerUUID);
                gameProfile.ifPresent(list::add);
            }
        }

        return new CBStandTournamentUpdatePayload(list, tournament);
    }

    public static final StreamCodec<GameProfile> GAME_PROFILE_STREAM_CODEC = StreamCodec.composite(
            StreamCodec.UUID, GameProfile::getId,
            StreamCodec.STRING, GameProfile::getName,
            GameProfile::new
    );

    public static final StreamCodec<List<GameProfile>> GAME_PROFILE_STREAM_CODEC_LIST = GAME_PROFILE_STREAM_CODEC.list();

    public static final StreamCodec<CBStandTournamentUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            GAME_PROFILE_STREAM_CODEC_LIST, CBStandTournamentUpdatePayload::listSignups,
            Tournament.STREAM_CODEC, CBStandTournamentUpdatePayload::tour,
            CBStandTournamentUpdatePayload::new
    );

    @Override
    public void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player) {
        //only accept packet if cache is empty, or if it's the correct uuid being disaplyed
        if (Minecraft.getInstance().screen instanceof StandScreen ss)
        {
                ss.onTournamentReceived(this.tour());
        }
        StandScreen.gameProfilesCache = new HashMap<>();
        this.listSignups().forEach(e -> StandScreen.gameProfilesCache.put(e.getId(), e.getName()));
    }

}
