package net.neoforged.neoforge.event;

import net.minecraft.core.RegistryAccess;
import net.neoforged.bus.api.Event;

public class TagsUpdatedEvent extends Event
{
    private final RegistryAccess registryAccess;
    private final UpdateCause updateCause;

    public TagsUpdatedEvent(RegistryAccess registryAccess, UpdateCause updateCause)
    {
        this.registryAccess = registryAccess;
        this.updateCause = updateCause;
    }

    public TagsUpdatedEvent(RegistryAccess registryAccess)
    {
        this(registryAccess, UpdateCause.SERVER_DATA_LOAD);
    }

    public RegistryAccess getRegistryAccess()
    {
        return registryAccess;
    }

    public UpdateCause getUpdateCause()
    {
        return updateCause;
    }

    public boolean shouldUpdateStaticData()
    {
        return updateCause == UpdateCause.SERVER_DATA_LOAD;
    }

    public enum UpdateCause
    {
        SERVER_DATA_LOAD,
        CLIENT_PACKET_RECEIVED
    }
}
