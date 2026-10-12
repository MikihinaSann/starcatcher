package com.wdiscute.starcatcher.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import com.wdiscute.starcatcher.neoforged.fml.ModList;

import java.util.List;

// Quality Food has no Fabric 1.21.1 build; quality operations are no-ops.
public class QualityFoodCompat
{
    public static void addQuality(ItemStack itemStack, Player player, double chance)
    {
    }

    public static boolean isLoaded()
    {
        return ModList.get().isLoaded("quality_food");
    }

    public static void removeQuality(List<ItemStack> items)
    {
    }
}
