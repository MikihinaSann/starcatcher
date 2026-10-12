package com.wdiscute.starcatcher.neoforged.fml.event.lifecycle;

import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public class FMLClientSetupEvent extends Event
{
    public void enqueueWork(Runnable work)
    {
        work.run();
    }
}
