package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

// FTB Teams has no Fabric 1.21.1 build; team sharing is a no-op.
public class FTBTeamsCompat
{
    public static void awardToTeam(Player player, FishProperties fp, ResourceLocation rl, int ticks)
    {
    }
}
