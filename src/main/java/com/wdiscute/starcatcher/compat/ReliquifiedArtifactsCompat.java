package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// Reliquified Artifacts has no Fabric 1.21.1 build; angler's-hat relic hooks are no-ops.
public class ReliquifiedArtifactsCompat
{
    public static boolean shouldAwardBonusTreasure(Player player)
    {
        return false;
    }

    public static List<ItemStack> getBonusCatchItems(Player player, FishingBobEntity fbe)
    {
        return List.of();
    }

    public static void awardRelicXP(Player player, boolean gotTreasure)
    {
    }
}
