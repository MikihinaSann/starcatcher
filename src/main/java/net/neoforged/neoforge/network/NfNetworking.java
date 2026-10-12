package net.neoforged.neoforge.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.ClientPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.ServerPayloadContext;

import java.util.function.BiConsumer;

/**
 * Internal bridge between the NeoForge-style registrar shim and Fabric's
 * networking. Client-only classes are touched lazily so this is safe on a
 * dedicated server.
 */
public class NfNetworking
{
    public static <T extends CustomPacketPayload> void registerClientReceiver(
            CustomPacketPayload.Type<T> type, BiConsumer<T, IPayloadContext> handler)
    {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, ctx) ->
        {
            IPayloadContext context = new ClientPayloadContext(ctx);
            context.enqueueWork(() -> handler.accept(payload, context));
        });
    }

    public static <T extends CustomPacketPayload> void registerServerReceiver(
            CustomPacketPayload.Type<T> type, BiConsumer<T, IPayloadContext> handler)
    {
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, ctx) ->
        {
            IPayloadContext context = new ServerPayloadContext(ctx);
            context.enqueueWork(() -> handler.accept(payload, context));
        });
    }

    public static void sendToServer(CustomPacketPayload payload)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            ClientPlayNetworking.send(payload);
    }
}
