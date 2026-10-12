package com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps;

import com.wdiscute.starcatcher.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric port shim of NeoForge's RegisterDataMapTypesEvent.
 */
public class RegisterDataMapTypesEvent extends Event
{
    private final List<DataMapType<?, ?>> types = new ArrayList<>();

    public void register(DataMapType<?, ?> type)
    {
        types.add(type);
        DataMapManager.registerType(type);
    }

    public List<DataMapType<?, ?>> getTypes()
    {
        return types;
    }
}
