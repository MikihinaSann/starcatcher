package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;

public class SullysModFishingProperties extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {
        //
        // ,---.            ,--. ,--.           ,--.             ,--.   ,--.            ,--.
        //'   .-'  ,--.,--. |  | |  | ,--. ,--. |  |  ,---.      |   `.'   |  ,---.   ,-|  |
        //`.  `-.  |  ||  | |  | |  |  \  '  /  `-'  (  .-'      |  |'.'|  | | .-. | ' .-. |
        //.-'    | '  ''  ' |  | |  |   \   '        .-'  `)     |  |   |  | ' '-' ' \ `-' |
        //`-----'   `----'  `--' `--' .-'  /         `----'      `--'   `--'  `---'   `---'
        //                            `---'

        register(fish(U.locItem("sullysmod", "piranha"))
                .withBucketedFish(U.locItem("sullysmod", "piranha_bucket"))
                .withEntityToSpawn(U.holderEntity("sullysmod", "piranha"))
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 500, 300))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                        .withBiomesTags(U.rl("sullysmod", "biome/piranha_spawn_in")))
        );

        register(fish(U.locItem("sullysmod", "lanternfish"))
                .withBucketedFish(U.locItem("sullysmod", "lanternfish_bucket"))
                .withEntityToSpawn(U.holderEntity("sullysmod", "lanternfish"))
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                        .withBiomesTags(U.rl("sullysmod", "biome/lanternfish_spawn_in")))
        );
    }
}
