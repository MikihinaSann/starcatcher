package net.neoforged.neoforge.registries;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Fabric port shim of NeoForge's RegistryBuilder backed by
 * {@link FabricRegistryBuilder}. Registry sync is a no-op: identical code
 * registers identical entries on both sides, as on Fabric.
 */
public class RegistryBuilder<T>
{
    private final ResourceKey<Registry<T>> key;
    private ResourceLocation defaultKey;

    public RegistryBuilder(ResourceKey<Registry<T>> key)
    {
        this.key = key;
    }

    public RegistryBuilder<T> sync(boolean sync)
    {
        return this;
    }

    public RegistryBuilder<T> defaultKey(ResourceLocation defaultKey)
    {
        this.defaultKey = defaultKey;
        return this;
    }

    public RegistryBuilder<T> maxId(int maxId)
    {
        return this;
    }

    public Registry<T> create()
    {
        FabricRegistryBuilder<T, ?> builder = defaultKey != null
                ? FabricRegistryBuilder.createDefaulted(key, defaultKey)
                : FabricRegistryBuilder.createSimple(key);
        return builder.buildAndRegister();
    }
}
