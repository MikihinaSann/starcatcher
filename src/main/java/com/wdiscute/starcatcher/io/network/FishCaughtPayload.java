package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.storage.FishProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.network.NetworkEvent;

public record FishCaughtPayload(FishProperties fp, boolean newFish, int size, int weight) implements ToClientPacket {
    public static final StreamCodec<FishCaughtPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, FishCaughtPayload::fp,
            StreamCodec.BOOL, FishCaughtPayload::newFish,
            StreamCodec.INT, FishCaughtPayload::size,
            StreamCodec.INT, FishCaughtPayload::weight,
            FishCaughtPayload::new
    );

    @Override
    public void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player) {
        Starcatcher.fishCaughtToast(fp(), newFish(), size(), weight());
    }
}
