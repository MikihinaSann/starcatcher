package com.wdiscute.starcatcher.io.network.tournament;


import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.network.ToServerPacket;
import com.wdiscute.starcatcher.tournament.StandMenu;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;

public record SBStandTournamentNameChangePayload(UUID uuid, String name) implements ToServerPacket
{
    public static final StreamCodec<GameProfile> GAME_PROFILE_STREAM_CODEC = StreamCodec.composite(
            StreamCodec.UUID, GameProfile::getId,
            StreamCodec.STRING, GameProfile::getName,
            GameProfile::new
    );

    public static final StreamCodec<List<GameProfile>> GAME_PROFILE_STREAM_CODEC_LIST = GAME_PROFILE_STREAM_CODEC.list();

    public static final StreamCodec<SBStandTournamentNameChangePayload> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.UUID, SBStandTournamentNameChangePayload::uuid,
            StreamCodec.STRING, SBStandTournamentNameChangePayload::name,
            SBStandTournamentNameChangePayload::new
    );

    @Override
    public void handleServer(NetworkEvent.Context context, ServerPlayer player, ServerLevel level) {
        if(player.containerMenu instanceof StandMenu sm)
        {
            sm.sbe.tournament.name = name;
        }
    }
}
