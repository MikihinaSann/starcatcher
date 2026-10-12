package net.neoforged.neoforge.client.event;

import net.neoforged.bus.api.Event;

public class RegisterColorHandlersEvent extends Event
{
    public static class Block extends RegisterColorHandlersEvent
    {
        public void register(net.minecraft.client.color.block.BlockColor color,
                             net.minecraft.world.level.block.Block... blocks)
        {
            net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK.register(color, blocks);
        }
    }

    public static class Item extends RegisterColorHandlersEvent
    {
        public void register(net.minecraft.client.color.item.ItemColor color,
                             net.minecraft.world.level.ItemLike... items)
        {
            net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.ITEM.register(color, items);
        }
    }
}
