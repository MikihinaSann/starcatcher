package com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Fabric port shim of NeoForge's DataMapType.
 */
public class DataMapType<R, T>
{
    private final ResourceLocation id;
    private final ResourceKey<Registry<R>> registryKey;
    private final Codec<T> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> networkCodec;
    private final boolean mandatorySync;

    private DataMapType(ResourceLocation id, ResourceKey<Registry<R>> registryKey, Codec<T> codec,
                        StreamCodec<? super RegistryFriendlyByteBuf, T> networkCodec, boolean mandatorySync)
    {
        this.id = id;
        this.registryKey = registryKey;
        this.codec = codec;
        this.networkCodec = networkCodec;
        this.mandatorySync = mandatorySync;
    }

    public static <R, T> Builder<R, T> builder(ResourceLocation id, ResourceKey<Registry<R>> registryKey, Codec<T> codec)
    {
        return new Builder<>(id, registryKey, codec);
    }

    public static final class Builder<R, T>
    {
        private final ResourceLocation id;
        private final ResourceKey<Registry<R>> registryKey;
        private final Codec<T> codec;
        private StreamCodec<? super RegistryFriendlyByteBuf, T> networkCodec;
        private boolean mandatorySync;

        private Builder(ResourceLocation id, ResourceKey<Registry<R>> registryKey, Codec<T> codec)
        {
            this.id = id;
            this.registryKey = registryKey;
            this.codec = codec;
        }

        public Builder<R, T> synced(StreamCodec<? super RegistryFriendlyByteBuf, T> networkCodec, boolean mandatorySync)
        {
            this.networkCodec = networkCodec;
            this.mandatorySync = mandatorySync;
            return this;
        }

        public Builder<R, T> synced(Codec<T> networkCodec, boolean mandatorySync)
        {
            return synced(net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistries(networkCodec), mandatorySync);
        }

        public DataMapType<R, T> build()
        {
            return new DataMapType<>(id, registryKey, codec, networkCodec, mandatorySync);
        }
    }

    public ResourceLocation id()
    {
        return id;
    }

    public ResourceKey<Registry<R>> registryKey()
    {
        return registryKey;
    }

    public Codec<T> codec()
    {
        return codec;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, T> networkCodec()
    {
        return networkCodec;
    }

    public boolean mandatorySync()
    {
        return mandatorySync;
    }

    /**
     * Directory under {@code data_maps/} for this type's registry, matching
     * NeoForge's layout ({@code minecraft:item} -> {@code item},
     * {@code starcatcher:fish} -> {@code starcatcher/fish}).
     */
    public String directory()
    {
        ResourceLocation rl = registryKey.location();
        return "minecraft".equals(rl.getNamespace()) ? rl.getPath() : rl.getNamespace() + "/" + rl.getPath();
    }
}
