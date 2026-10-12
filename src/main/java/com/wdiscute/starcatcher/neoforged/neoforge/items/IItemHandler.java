package com.wdiscute.starcatcher.neoforged.neoforge.items;

import net.minecraft.world.item.ItemStack;

/**
 * Fabric port shim of NeoForge's IItemHandler.
 */
public interface IItemHandler
{
    int getSlots();

    ItemStack getStackInSlot(int slot);

    ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

    ItemStack extractItem(int slot, int amount, boolean simulate);

    int getSlotLimit(int slot);

    default boolean isItemValid(int slot, ItemStack stack)
    {
        return true;
    }
}
