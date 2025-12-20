package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public record FishingCompletedPayload(int time, boolean completedTreasure, boolean perfectCatch, int hits) implements ToServerPacket
{
    public static final StreamCodec<FishingCompletedPayload> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.INT, FishingCompletedPayload::time,
            StreamCodec.BOOL, FishingCompletedPayload::completedTreasure,
            StreamCodec.BOOL, FishingCompletedPayload::perfectCatch,
            StreamCodec.INT, FishingCompletedPayload::hits,
            FishingCompletedPayload::new
    );

    @Override
    public void handleServer(NetworkEvent.Context context, ServerPlayer player, ServerLevel level) {
        U.spawnFishFromPlayerFishing(player, time, completedTreasure, perfectCatch, hits);
    }
}
