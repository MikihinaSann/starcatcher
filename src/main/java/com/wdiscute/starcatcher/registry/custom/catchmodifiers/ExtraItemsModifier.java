package com.wdiscute.starcatcher.registry.custom.catchmodifiers;

import net.minecraft.world.item.ItemStack;

public class ExtraItemsModifier extends AbstractCatchModifier
{
    final int count;
    public ExtraItemsModifier(int count)
    {
        this.count = count;
    }

    @Override
    public ItemStack modifyItemStack(ItemStack is)
    {
        is.grow(count);
        return is;
    }
}
