package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;

public class DGSpawnFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {

        //
        // ,---.
        //'   .-'   ,---.   ,--,--. ,--.   ,--. ,--,--,
        //`.  `-.  | .-. | ' ,-.  | |  |.'.|  | |      \
        //.-'    | | '-' ' \ '-'  | |   .'.   | |  ||  |
        //`-----'  |  |-'   `--`--' '--'   '--' `--''--'
        //         `--'

        register(fish(U.locItem("spawn", "angler_fish"))
                .withBucketedFish(U.locItem("spawn", "angler_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("spawn", "angler_fish"))
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withRarity(FishProperties.Rarity.RARE)
                .withDaytime(FishProperties.Daytime.MIDNIGHT)
                .withDifficulty(FishProperties.Difficulty.FOUR_AQUA)
                .withBaseChance(20)
        );

        register(fish(U.locItem("spawn", "tuna_egg_bucket"))
                .withBucketedFish(U.locItem("spawn", "tuna_egg_bucket"))
                .withEntityToSpawn(U.holderEntity("spawn", "tuna"))
                .withAlwaysSpawnEntity()
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ALL_OCEANS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
        );

        register(fish(U.locItem("spawn", "baby_sunfish_bucket"))
                .withBucketedFish(U.locItem("spawn", "baby_sunfish_bucket"))
                .withEntityToSpawn(U.holderEntity("spawn", "sunfish"))
                .withAlwaysSpawnEntity()
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.EPIC)
                .withDaytime(FishProperties.Daytime.NOON)
                .withBaseChance(20)
                .withDifficulty(FishProperties.Difficulty.TWO_THIN.vanishing())
        );

        register(fish(U.locItem("spawn", "captured_octopus"))
                .withEntityToSpawn(U.holderEntity("spawn", "octopus"))
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ALL_OCEANS)
                .withBaseChance(1)
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
        );

        register(fish(U.locItem("spawn", "herring"))
                .withBucketedFish(U.locItem("spawn", "herring_bucket"))
                .withEntityToSpawn(U.holderEntity("spawn", "herring"))
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ALL_OCEANS)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

    }
}
