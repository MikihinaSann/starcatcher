package com.wdiscute.starcatcher.neoforged.neoforge.event.tick;

import net.minecraft.server.MinecraftServer;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public abstract class ServerTickEvent extends Event
{
    private final MinecraftServer server;

    protected ServerTickEvent(MinecraftServer server)
    {
        this.server = server;
    }

    public MinecraftServer getServer()
    {
        return server;
    }

    public static class Post extends ServerTickEvent
    {
        public Post(MinecraftServer server)
        {
            super(server);
        }

        public boolean hasTime()
        {
            return true;
        }
    }

    public static class Pre extends ServerTickEvent
    {
        public Pre(MinecraftServer server)
        {
            super(server);
        }
    }
}
