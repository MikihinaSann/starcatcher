package com.wdiscute.starcatcher.registry.fishing;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.fishing.compat.*;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
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
        MinecraftFishingProperties.bootstrap();
        StarcatcherFishingProperties.bootstrap();
        TideFishingProperties.bootstrap();
        AquacultureFishingProperties.bootstrap();
        FishOfThievesFishingProperties.bootstrap();
        NetherDepthsUpgradeFishingProperties.bootstrap();
        SullysModFishingProperties.bootstrap();
        UpgradeAquaticFishingProperties.bootstrap();
        EnvironmentalFishingProperties.bootstrap();
        CollectorsReapFishingProperties.bootstrap();
        MinersDelightFishingProperties.bootstrap();
        AlexsCavesFishingProperties.bootstrap();
        CrittersAndCompanionsFishingProperties.bootstrap();
        HybridAquaticFishingProperties.bootstrap();
        AquamiraeFishingProperties.bootstrap();
        TerraFirmaCraftFishProperties.bootstrap();
    }

    //region builders
    protected static FishProperties.Builder fish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish);
    }

    protected static FishProperties.Builder overworldFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD);
    }

    protected static FishProperties.Builder endFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.END);
    }

    protected static FishProperties.Builder endOuterIslandsFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.END_OUTER_ISLANDS);
    }

    protected static FishProperties.Builder netherLavaFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA);
    }

    protected static FishProperties.Builder netherLavaCrimsonForestFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_CRIMSON_FOREST);
    }

    protected static FishProperties.Builder netherLavaWarpedForestFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_WARPED_FOREST);
    }

    protected static FishProperties.Builder netherLavaSoulSandValleyFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_SOUL_SAND_VALLEY);
    }

    protected static FishProperties.Builder netherLavaBasaltDeltasFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_BASALT_DELTAS);
    }

    protected static FishProperties.Builder overworldLushCavesFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES)
                .withBaitRestrictions(FishProperties.BaitRestrictions.LUSH_BAIT);
    }

    protected static FishProperties.Builder overworldDeepDarkFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .withBaitRestrictions(FishProperties.BaitRestrictions.SCULK_BAIT);
    }

    protected static FishProperties.Builder overworldSurfaceFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SURFACE);
    }

    protected static FishProperties.Builder overworldSurfaceLava(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_SURFACE);
    }

    protected static FishProperties.Builder overworldCavesFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_STONE_CAVES);
    }

    protected static FishProperties.Builder overworldDripstoneCavesFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DRIPSTONE_CAVES)
                .withBaitRestrictions(FishProperties.BaitRestrictions.DRIPSTONE_BAIT);
    }

    protected static FishProperties.Builder overworldUndergroundFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_UNDERGROUND);
    }

    protected static FishProperties.Builder overworldUndergroundLava(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_UNDERGROUND);
    }

    protected static FishProperties.Builder overworldMountainFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE
                        .withMustBeCaughtAboveY(100)
                        .withMustBeCaughtBelowY(Integer.MAX_VALUE));
    }

    protected static FishProperties.Builder overworldDeepslateFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEPSLATE);
    }

    protected static FishProperties.Builder overworldDeepslateLava(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_DEEPSLATE);
    }

    protected static FishProperties.Builder overworldColdLakeFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_LAKE);
    }

    protected static FishProperties.Builder overworldWarmLakeFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE);
    }

    protected static FishProperties.Builder overworldWarmMountainFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE);
    }

    protected static FishProperties.Builder overworldColdMountainFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_MOUNTAIN);
    }

    protected static FishProperties.Builder overworldColdOceanFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN);
    }

    protected static FishProperties.Builder overworldColdRiverFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_RIVER);
    }

    protected static FishProperties.Builder overworldLakeFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE);
    }

    protected static FishProperties.Builder overworldOceanFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN);
    }

    protected static FishProperties.Builder overworldWarmOceanFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN);
    }

    protected static FishProperties.Builder overworldDeepOceanFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN);
    }

    protected static FishProperties.Builder overworldRiverFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER);
    }

    protected static FishProperties.Builder overworldBeachFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BEACH);
    }


    protected static FishProperties.Builder overworldMushroomFieldsFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS);
    }

    protected static FishProperties.Builder overworldJungleFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE);
    }

    protected static FishProperties.Builder overworldTaigaFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_TAIGA);
    }

    protected static FishProperties.Builder overworldCherryGroveFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .withBaitRestrictions(FishProperties.BaitRestrictions.CHERRY_BAIT);
    }

    protected static FishProperties.Builder overworldSwampFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP)
                .withBaitRestrictions(FishProperties.BaitRestrictions.MURKWATER_BAIT);
    }

    protected static FishProperties.Builder overworldDarkForestFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST);
    }

    protected static FishProperties.Builder overworldForestFish(Supplier<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_FOREST);
    }

    //endregion

    private static final List<Pair<ResourceKey<FishProperties>, FishProperties>> PROPERTIES = new ArrayList<>();
    private static final List<ResourceKey<FishProperties>> COMPAT_KEYS = new ArrayList<>();

    static ResourceKey<FishProperties> createKey(FishProperties fp)
    {
        return ResourceKey.create(
                Starcatcher.FISH_REGISTRY, (Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(fp.catchInfo().fish().get()))));
    }

    protected static void registerStarcatcherBucketAndEntity(FishProperties.Builder builder)
    {
        builder.withBucketedFish(ModItems.STARCAUGHT_BUCKET);
        builder.withEntityToSpawn(U.holderEntity("starcatcher", "fish"));
        builder.build();
        register(builder);
    }

    protected static void register(FishProperties.Builder builder)
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
