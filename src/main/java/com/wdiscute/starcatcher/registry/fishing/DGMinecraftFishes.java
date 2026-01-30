package com.wdiscute.starcatcher.registry.fishing;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import static com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry.*;


public class DGMinecraftFishes
{
    public static void bootstrap() {

        //ocean
        register(overworldOceanFish(U.locItem("minecraft", "cod"))
                .withBucketedFish(U.locItem("minecraft", "cod_bucket"))
                .withEntityToSpawn(U.holderEntity("minecraft", "cod"))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
        );

        register(overworldOceanFish(U.locItem("minecraft", "pufferfish"))
                .withBucketedFish(U.locItem("minecraft", "pufferfish_bucket"))
                .withEntityToSpawn(U.holderEntity("minecraft", "pufferfish"))
                .withSizeAndWeight(FishProperties.sizeWeight(70, 20, 10000, 3000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );


        //warm ocean
        register(overworldWarmOceanFish(U.locItem("minecraft", "tropical_fish"))
                .withBucketedFish(U.locItem("minecraft", "tropical_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("minecraft", "tropical_fish"))
                .withSizeAndWeight(FishProperties.sizeWeight(70, 20, 10000, 3000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.FOUR_AQUA)
        );

        //river
        register(overworldRiverFish(U.locItem("minecraft", "salmon"))
                .withBucketedFish(U.locItem("minecraft", "salmon_bucket"))
                .withEntityToSpawn(U.holderEntity("minecraft", "salmon"))
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 10000, 8000))
        );


        //mobs
        register(fish(BuiltInRegistries.ITEM.getKey(Items.NETHER_STAR))
                .withAlwaysSpawnEntity(true)
                .withEntityToSpawn(U.holderEntity("minecraft", "wither"))
                .withBaseChance(0)
                .withBaitRestrictions(
                        FishProperties.BaitRestrictions.DEFAULT
                                .withCorrectBait(BuiltInRegistries.ITEM.getKey(Items.WITHER_SKELETON_SKULL))
                                .withCorrectBaitChanceAdded(200)
                )
                .withDifficulty(FishProperties.Difficulty.WITHER)
                .withItemToOverrideWith(ModItems.UNKNOWN_FISH.getId())
                .withRarity(FishProperties.Rarity.LEGENDARY)
        );

        register(fish(BuiltInRegistries.ITEM.getKey(Items.CREEPER_HEAD))
                .withAlwaysSpawnEntity(true)
                .withEntityToSpawn(U.holderEntity("minecraft", "creeper"))
                .withBaseChance(0)
                .withBaitRestrictions(
                        FishProperties.BaitRestrictions.DEFAULT
                                .withCorrectBait(Starcatcher.rl("gunpowder_bait"))
                                .withCorrectBaitChanceAdded(100)
                )
                .withDifficulty(FishProperties.Difficulty.CREEPER)
                .withItemToOverrideWith(ModItems.UNKNOWN_FISH.getId())
                .withRarity(FishProperties.Rarity.EPIC)
        );

        register(overworldSurfaceFish(U.holderItem("minecraft", "rotten_flesh"))
                .withEntityToSpawn(U.locItem("minecraft", "drowned"))
                .withBaseChance(1)
                .withDaytime(FishProperties.Daytime.NIGHT)
                .withWeather(FishProperties.Weather.RAIN)
                .withHasGuideEntry(false)
                .withAlwaysSpawnEntity()
                .withSkipMinigame(true));
    }
}
