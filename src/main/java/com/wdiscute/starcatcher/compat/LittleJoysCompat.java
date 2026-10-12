package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.neoforged.fml.ModList;

// Little Joys has no Fabric 1.21.1 build; fishing spots are never detected.
public class LittleJoysCompat
{
    public static boolean isLoaded()
    {
        return ModList.get().isLoaded("littlejoys");
    }

    public static boolean isOnFishingSpot(FishingBobEntity fbe)
    {
        return false;
    }
}
