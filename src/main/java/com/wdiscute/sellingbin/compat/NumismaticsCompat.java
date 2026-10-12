package com.wdiscute.sellingbin.compat;

import net.minecraft.world.item.ItemStack;

// Create: Numismatics has no Fabric 1.21.1 build; card deposits always fail.
public class NumismaticsCompat
{
    public static boolean deposit(ItemStack result, ItemStack card)
    {
        return false;
    }
}
