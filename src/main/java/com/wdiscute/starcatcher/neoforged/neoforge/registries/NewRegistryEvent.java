package com.wdiscute.starcatcher.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

/**
 * Marker shim: on Fabric registries are created eagerly via
 * {@code FabricRegistryBuilder}, so registration here is a no-op.
 */
public class NewRegistryEvent extends Event
{
    public <T> void register(Registry<T> registry)
    {
    }
}
