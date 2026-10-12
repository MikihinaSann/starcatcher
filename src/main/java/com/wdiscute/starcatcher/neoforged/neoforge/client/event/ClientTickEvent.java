package com.wdiscute.starcatcher.neoforged.neoforge.client.event;

import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public abstract class ClientTickEvent extends Event
{
    public static class Post extends ClientTickEvent
    {
    }

    public static class Pre extends ClientTickEvent
    {
    }
}
