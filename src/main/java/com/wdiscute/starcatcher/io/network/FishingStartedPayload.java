package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public record FishingStartedPayload(FishProperties fp, ItemStack rod) implements ToClientPacket {
    public static final StreamCodec<FishingStartedPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, FishingStartedPayload::fp,
            StreamCodec.ITEM_STACK, FishingStartedPayload::rod,
            FishingStartedPayload::new
    );



    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player) {
        Minecraft.getInstance().setScreen(new FishingMinigameScreen(fp, rod));
    }
}
