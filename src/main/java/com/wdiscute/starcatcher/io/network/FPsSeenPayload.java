package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FPsSeenPayload(List<ResourceLocation> locs) implements ToServerPacket {

    public static final StreamCodec< FPsSeenPayload> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.RESOURCE_LOCATION.list(), FPsSeenPayload::locs,
            FPsSeenPayload::new
    );

    @Override
    public void handleServer(NetworkEvent.Context context, ServerPlayer player, ServerLevel level) {
        Map<ResourceLocation, FishCaughtCounter> map = new HashMap<>(FishingGuideAttachment.getFishesCaught(player));

        locs.forEach(loc -> {
            FishCaughtCounter fishCaughtCounter = map.get(loc);

            if (fishCaughtCounter != null)
                map.replace(loc,  fishCaughtCounter.removeNotification());
        });

        FishingGuideAttachment.setFishesCaught(player, map);

    }
}
