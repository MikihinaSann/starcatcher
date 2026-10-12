package net.neoforged.neoforge.client.extensions.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Arrays;
import java.util.List;

public class RegisterClientExtensionsEvent extends Event
{
    public void registerItem(IClientItemExtensions extensions, ItemLike... items)
    {
        for (ItemLike item : items)
            IClientItemExtensions.register(item.asItem(), extensions);
    }

    public void registerItem(IClientItemExtensions extensions, List<? extends ItemLike> items)
    {
        for (ItemLike item : items)
            IClientItemExtensions.register(item.asItem(), extensions);
    }

    @SuppressWarnings("unchecked")
    public void registerItem(IClientItemExtensions extensions, DeferredItem<? extends Item>... items)
    {
        for (DeferredItem<? extends Item> item : items)
            IClientItemExtensions.register(item.get(), extensions);
    }

    public void registerItem(IClientItemExtensions extensions,
                             java.util.stream.Stream<? extends DeferredHolder<Item, ? extends Item>> items)
    {
        items.forEach(h -> IClientItemExtensions.register(h.get(), extensions));
    }
}
