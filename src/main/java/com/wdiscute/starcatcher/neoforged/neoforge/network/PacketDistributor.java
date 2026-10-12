package com.wdiscute.starcatcher.neoforged.neoforge.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import com.wdiscute.starcatcher.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Fabric port shim of NeoForge's PacketDistributor.
 */
public class PacketDistributor
{
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads)
    {
        ServerPlayNetworking.send(player, payload);
        for (CustomPacketPayload p : payloads)
            ServerPlayNetworking.send(player, p);
    }

    public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload)
    {
        for (ServerPlayer player : PlayerLookup.tracking(entity))
            ServerPlayNetworking.send(player, payload);
    }

    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload)
    {
        sendToPlayersTrackingEntity(entity, payload);
        if (entity instanceof ServerPlayer sp)
            ServerPlayNetworking.send(sp, payload);
    }

    public static void sendToAllPlayers(CustomPacketPayload payload)
    {
        net.minecraft.server.MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers())
            ServerPlayNetworking.send(player, payload);
    }

    public static void sendToServer(CustomPacketPayload payload)
    {
        NfNetworking.sendToServer(payload);
    }
}
