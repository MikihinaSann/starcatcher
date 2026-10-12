package com.wdiscute.starcatcher.neoforged.neoforge.network.event;

import com.wdiscute.starcatcher.neoforged.bus.api.Event;
import com.wdiscute.starcatcher.neoforged.fml.event.lifecycle.InterModProcessEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Fabric port shim of NeoForge's RegisterPayloadHandlersEvent.
 */
public class RegisterPayloadHandlersEvent extends Event
{
    public PayloadRegistrar registrar(String version)
    {
        return new PayloadRegistrar(version);
    }
}
