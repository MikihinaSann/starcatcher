package com.wdiscute.starcatcher.compat.curios;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import com.wdiscute.starcatcher.neoforged.fml.ModList;

import java.util.List;

public class CuriosCompat
{
    public static boolean isLoaded()
    {
        return ModList.get().isLoaded("trinkets");
    }

    public static List<ItemStack> getItems(Player player)
    {
        return TrinketsApi.getTrinketComponent(player)
                .map(component -> component.getAllEquipped().stream()
                        .map(net.minecraft.util.Tuple::getB)
                        .toList())
                .orElse(List.of());
    }
}
