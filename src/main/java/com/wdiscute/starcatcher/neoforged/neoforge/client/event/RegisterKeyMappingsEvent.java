package com.wdiscute.starcatcher.neoforged.neoforge.client.event;

import net.minecraft.client.KeyMapping;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public class RegisterKeyMappingsEvent extends Event
{
    public void register(KeyMapping keyMapping)
    {
        net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(keyMapping);
    }
}
