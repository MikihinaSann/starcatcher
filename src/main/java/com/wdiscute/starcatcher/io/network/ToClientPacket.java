package com.wdiscute.starcatcher.io.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.logging.Level;

public interface ToClientPacket extends CustomPacketPayload{
    @Override
    default void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handleClean(context));

        context.setPacketHandled(true);
    };

    @OnlyIn(Dist.CLIENT)
    private void handleClean(NetworkEvent.Context context){
        handleClient(context, Minecraft.getInstance().level,  Minecraft.getInstance().player);
    }

    void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player);
}
