package com.wdiscute.starcatcher.neoforged.fml.loading;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import com.wdiscute.starcatcher.neoforged.api.distmarker.Dist;

/**
 * Fabric port shim of the bits of FMLLoader used here.
 */
public class FMLLoader
{
    public static Dist getDist()
    {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
                ? Dist.CLIENT
                : Dist.DEDICATED_SERVER;
    }

    public static boolean isProduction()
    {
        return !FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
