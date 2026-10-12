package net.neoforged.neoforge.registries;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.Event;

/**
 * Fabric port shim of NeoForge's DataPackRegistryEvent backed by
 * {@link DynamicRegistries#registerSynced}.
 */
public class DataPackRegistryEvent extends Event
{
    public static class NewRegistry extends DataPackRegistryEvent
    {
    }

    public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec,
                                     java.util.function.Consumer<RegistryBuilder<T>> builderConsumer)
    {
        dataPackRegistry(registryKey, codec);
    }

    public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, Codec<T> networkCodec,
                                     java.util.function.Consumer<RegistryBuilder<T>> builderConsumer)
    {
        dataPackRegistry(registryKey, codec);
    }

    public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec)
    {
        dataPackRegistry(registryKey, codec, (StreamCodec<? super RegistryFriendlyByteBuf, T>) null);
    }

    public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec,
                                     StreamCodec<? super RegistryFriendlyByteBuf, T> networkCodec)
    {
        dataPackRegistry(registryKey, codec, codec, networkCodec);
    }

    public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, Codec<T> networkCodec,
                                     StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec)
    {
        DynamicRegistries.registerSynced(registryKey, codec);
    }

    public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, Codec<T> networkCodec)
    {
        DynamicRegistries.registerSynced(registryKey, codec);
    }
}
