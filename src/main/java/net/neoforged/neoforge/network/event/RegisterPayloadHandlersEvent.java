package net.neoforged.neoforge.network.event;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

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
