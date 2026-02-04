package com.wdiscute.starcatcher.registry.fishing;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.fishing.compat.*;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FishingPropertiesRegistry
{

    public static void register()
    {
        DGMinecraftFishes.bootstrap();
        DGStarcatcherFishes.bootstrap();
        DGTideFishes.bootstrap();
        DGAquacultureFishes.bootstrap();
        DGFishOfThievesFishes.bootstrap();
        DGNetherDepthsUpgradeFishes.bootstrap();
        DGSullysModFishes.bootstrap();
        DGUpgradeAquaticFishes.bootstrap();
        DGEnvironmentalFishes.bootstrap();
        DGBetterEndFishes.bootstrap();
        DGCollectorsReapFishes.bootstrap();
        DGMinersDelightFishes.bootstrap();
        DGAlexsCavesFishes.bootstrap();
        DGCrittersAndCompanionsFishes.bootstrap();
        DGHybridAquaticFishes.bootstrap();
        DGAquamiraeFishes.bootstrap();
        DGTerraFirmaCraftFishes.bootstrap();
        DGUnusualFishFishes.bootstrap();
        DGSpawnFishes.bootstrap();
        DGFintasticFishes.bootstrap();
    }

    //region builders
    public static FishProperties.Builder fish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish);
    }

    public static FishProperties.Builder overworldFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD);
    }

    public static FishProperties.Builder endFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.END);
    }

    public static FishProperties.Builder endOuterIslandsFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.END_OUTER_ISLANDS);
    }

    public static FishProperties.Builder netherLavaFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA);
    }

    public static FishProperties.Builder netherLavaCrimsonForestFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_CRIMSON_FOREST);
    }

    public static FishProperties.Builder netherLavaWarpedForestFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_WARPED_FOREST);
    }

    public static FishProperties.Builder netherLavaSoulSandValleyFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_SOUL_SAND_VALLEY);
    }

    public static FishProperties.Builder netherLavaBasaltDeltasFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_BASALT_DELTAS);
    }

    public static FishProperties.Builder overworldLushCavesFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES)
                .withBaitRestrictions(FishProperties.BaitRestrictions.LUSH_BAIT);
    }

    public static FishProperties.Builder overworldDeepDarkFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .withBaitRestrictions(FishProperties.BaitRestrictions.SCULK_BAIT);
    }

    public static FishProperties.Builder overworldSurfaceFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SURFACE);
    }

    public static FishProperties.Builder overworldSurfaceLava(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_SURFACE);
    }

    public static FishProperties.Builder overworldCavesFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_STONE_CAVES);
    }

    public static FishProperties.Builder overworldDripstoneCavesFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DRIPSTONE_CAVES)
                .withBaitRestrictions(FishProperties.BaitRestrictions.DRIPSTONE_BAIT);
    }

    public static FishProperties.Builder overworldUndergroundFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_UNDERGROUND);
    }

    public static FishProperties.Builder overworldUndergroundLava(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_UNDERGROUND);
    }

    public static FishProperties.Builder overworldMountainFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE
                        .withMustBeCaughtAboveY(100)
                        .withMustBeCaughtBelowY(Integer.MAX_VALUE));
    }

    public static FishProperties.Builder overworldDeepslateFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEPSLATE);
    }

    public static FishProperties.Builder overworldDeepslateLava(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_DEEPSLATE);
    }

    public static FishProperties.Builder overworldColdLakeFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_LAKE);
    }

    public static FishProperties.Builder overworldWarmLakeFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE);
    }

    public static FishProperties.Builder overworldWarmMountainFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE);
    }

    public static FishProperties.Builder overworldColdMountainFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_MOUNTAIN);
    }

    public static FishProperties.Builder overworldColdOceanFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN);
    }

    public static FishProperties.Builder overworldColdRiverFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_RIVER);
    }

    public static FishProperties.Builder overworldLakeFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE);
    }

    public static FishProperties.Builder overworldOceanFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ALL_OCEANS);
    }

    public static FishProperties.Builder overworldWarmOceanFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN);
    }

    public static FishProperties.Builder overworldDeepOceanFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN);
    }

    public static FishProperties.Builder overworldRiverFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER);
    }

    public static FishProperties.Builder overworldBeachFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BEACH);
    }


    public static FishProperties.Builder overworldMushroomFieldsFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS);
    }

    public static FishProperties.Builder overworldJungleFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE);
    }

    public static FishProperties.Builder overworldTaigaFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_TAIGA);
    }

    public static FishProperties.Builder overworldCherryGroveFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .withBaitRestrictions(FishProperties.BaitRestrictions.CHERRY_BAIT);
    }

    public static FishProperties.Builder overworldSwampFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .withBaitRestrictions(FishProperties.BaitRestrictions.MURKWATER_BAIT);
    }

    public static FishProperties.Builder overworldDarkForestFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST);
    }

    public static FishProperties.Builder overworldForestFish(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_FOREST);
    }

    public static FishProperties.Builder overworldVoidFishing(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_VOID);
    }

    public static FishProperties.Builder netherVoidFishing(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_VOID);
    }

    public static FishProperties.Builder endVoidFishing(ResourceLocation fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.END_VOID);
    }

    //endregion

    private static final List<Pair<ResourceKey<FishProperties>, FishProperties>> PROPERTIES = new ArrayList<>();
    private static final List<ResourceKey<FishProperties>> COMPAT_KEYS = new ArrayList<>();

    static ResourceKey<FishProperties> createKey(FishProperties fp)
    {
        return ResourceKey.create(
                Starcatcher.FISH_REGISTRY, fp.catchInfo().fishLoc());
    }

    public static void registerStarcatcherBucketAndEntity(FishProperties.Builder builder)
    {
        builder.withBucketedFish(ModItems.STARCAUGHT_BUCKET.getId());
        builder.withEntityToSpawn(U.holderEntity("starcatcher", "fish"));
        DGStarcatcherFishes.STARCATCHER_FISHES.add(builder.build());
        register(builder);
    }

    public static void registerStarcatcherOnlyEntity(FishProperties.Builder builder)
    {
        builder.withEntityToSpawn(U.holderEntity("starcatcher", "fish"));
        register(builder);
    }


    public static void register(FishProperties.Builder builder)
    {
        FishProperties properties = builder.build();
        ResourceKey<FishProperties> key = FishingPropertiesRegistry.createKey(properties);
        PROPERTIES.add(Pair.of(key, properties));
        String namespace = key.location().getNamespace();
        if (!namespace.equals("minecraft") && !namespace.equals("starcatcher"))
            COMPAT_KEYS.add(key);
    }

    public static void registerConditions(BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        for (ResourceKey<FishProperties> compatKey : COMPAT_KEYS)
        {
            //fix for hybrid aquatic as their modid is hybrid_aquatic but items use hybrid-aquatic
            if(compatKey.location().getNamespace().equals("hybrid-aquatic"))
            {
                consumer.accept(compatKey, new ModLoadedCondition("hybrid_aquatic"));
                continue;
            }
            consumer.accept(compatKey, new ModLoadedCondition(compatKey.location().getNamespace()));
        }
    }

    public static void bootstrap(BootstapContext<FishProperties> context)
    {
        PROPERTIES.forEach(p -> context.register(p.getFirst(), p.getSecond()));
    }
}
