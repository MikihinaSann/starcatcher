package com.wdiscute.starcatcher.neoforged.neoforge.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DeferredItem<T extends Item> extends DeferredHolder<Item, T>
        implements net.minecraft.world.level.ItemLike
{
    public DeferredItem(ResourceKey<Item> key, Holder<Item> holder)
    {
        super(key, holder);
    }

    public DeferredItem(ResourceKey<Item> key, net.minecraft.core.Registry<Item> registry)
    {
        super(key, registry);
    }

    @Override
    public Item asItem()
    {
        return get();
    }

    public ItemStack toStack()
    {
        return toStack(1);
    }

    public ItemStack toStack(int count)
    {
        return new ItemStack(get(), count);
    }
}
