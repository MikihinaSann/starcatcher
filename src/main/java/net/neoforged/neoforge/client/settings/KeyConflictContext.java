package net.neoforged.neoforge.client.settings;

public enum KeyConflictContext
{
    UNIVERSAL,
    GUI,
    IN_GAME;

    public boolean isActive()
    {
        return true;
    }

    public boolean conflicts(KeyConflictContext other)
    {
        return this == other || this == UNIVERSAL || other == UNIVERSAL;
    }
}
