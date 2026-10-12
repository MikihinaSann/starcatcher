package com.wdiscute.starcatcher.neoforged.neoforge.network.registration;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.wdiscute.starcatcher.neoforged.neoforge.network.NfNetworking;
import com.wdiscute.starcatcher.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.BiConsumer;

/**
 * Fabric port shim of NeoForge's PayloadRegistrar.
 */
public class PayloadRegistrar
{
    private final String version;
    private boolean optional;

    public PayloadRegistrar(String version)
    {
        this.version = version;
    }

    public PayloadRegistrar optional()
    {
        this.optional = true;
        return this;
    }

    public <T extends CustomPacketPayload> PayloadRegistrar playToClient(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, IPayloadContext> handler)
    {
        PayloadTypeRegistry.playS2C().register(type, codec);
        NfNetworking.registerClientReceiver(type, handler);
        return this;
    }

    public <T extends CustomPacketPayload> PayloadRegistrar playToServer(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, IPayloadContext> handler)
    {
        PayloadTypeRegistry.playC2S().register(type, codec);
        NfNetworking.registerServerReceiver(type, handler);
        return this;
    }

    public <T extends CustomPacketPayload> PayloadRegistrar playBidirectional(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, IPayloadContext> handler)
    {
        playToClient(type, codec, handler);
        playToServer(type, codec, handler);
        return this;
    }

    public <T extends CustomPacketPayload> PayloadRegistrar commonToClient(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, IPayloadContext> handler)
    {
        return playToClient(type, codec, handler);
    }

    public <T extends CustomPacketPayload> PayloadRegistrar commonToServer(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, IPayloadContext> handler)
    {
        return playToServer(type, codec, handler);
    }
}
