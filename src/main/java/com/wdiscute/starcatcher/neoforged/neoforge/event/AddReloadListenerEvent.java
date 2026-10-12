package com.wdiscute.starcatcher.neoforged.neoforge.event;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric port shim of NeoForge's AddReloadListenerEvent. Listeners are queued
 * and must be registered with Fabric's {@code ResourceManagerHelper} by the
 * caller (see the ported event wiring).
 */
public class AddReloadListenerEvent extends Event
{
    private final List<PreparableReloadListener> listeners = new ArrayList<>();

    public void addListener(PreparableReloadListener listener)
    {
        listeners.add(listener);
    }

    public List<PreparableReloadListener> getListeners()
    {
        return listeners;
    }
}
