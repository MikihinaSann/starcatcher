package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.registry.fishrestrictions.SeasonRestriction;
import net.minecraft.world.level.Level;

// Ecliptic Seasons has no Fabric 1.21.1 build; stubbed to always-allowed.
public class EclipticSeasonsCompat
{
    public static SeasonRestriction.Seasons getSeason(Level level)
    {
        return SeasonRestriction.Seasons.ALL;
    }
}
