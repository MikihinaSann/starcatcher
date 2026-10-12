package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.registry.fishrestrictions.SeasonRestriction;
import net.minecraft.world.level.Level;

// Serene Seasons has no Fabric 1.21.1 build; stubbed to always-allowed.
public class SereneSeasonsCompat
{
    public static SeasonRestriction.Seasons getSeason(Level level)
    {
        return SeasonRestriction.Seasons.ALL;
    }
}
