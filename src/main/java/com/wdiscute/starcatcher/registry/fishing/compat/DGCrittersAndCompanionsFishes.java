package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;

public class DGCrittersAndCompanionsFishes extends FishingPropertiesRegistry
{


    public static void bootstrap()
    {

        //
        // ,-----.         ,--.   ,--.     ,--.                                                    ,--.      ,-----.                                              ,--.
        //'  .--./ ,--.--. `--' ,-'  '-. ,-'  '-.  ,---.  ,--.--.  ,---.       ,--,--. ,--,--,   ,-|  |     '  .--./  ,---.  ,--,--,--.  ,---.   ,--,--. ,--,--,  `--'  ,---.  ,--,--,   ,---.
        //|  |     |  .--' ,--. '-.  .-' '-.  .-' | .-. : |  .--' (  .-'      ' ,-.  | |      \ ' .-. |     |  |     | .-. | |        | | .-. | ' ,-.  | |      \ ,--. | .-. | |      \ (  .-'
        //'  '--'\ |  |    |  |   |  |     |  |   \   --. |  |    .-'  `)     \ '-'  | |  ||  | \ `-' |     '  '--'\ ' '-' ' |  |  |  | | '-' ' \ '-'  | |  ||  | |  | ' '-' ' |  ||  | .-'  `)
        // `-----' `--'    `--'   `--'     `--'    `----' `--'    `----'       `--`--' `--''--'  `---'       `-----'  `---'  `--`--`--' |  |-'   `--`--' `--''--' `--'  `---'  `--''--' `----'
        //                                                                                                                              `--'


        register(overworldBeachFish(U.locItem("crittersandcompanions", "clam"))
                .withSkipMinigame(true)
        );

        register(overworldRiverFish(U.locItem("crittersandcompanions", "koi_fish"))
                .withBucketedFish(U.locItem("crittersandcompanions", "koi_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("crittersandcompanions", "koi_fish"))
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 3000, 2000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(overworldDeepOceanFish(U.locItem("crittersandcompanions", "dumbo_octopus_bucket"))
                .withAlwaysSpawnEntity(true)
                .withBucketedFish(U.locItem("crittersandcompanions", "dumbo_octopus_bucket"))
                .withEntityToSpawn(U.holderEntity("crittersandcompanions", "dumbo_octopus"))
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1000, 300))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldDeepOceanFish(U.locItem("crittersandcompanions", "sea_bunny_bucket"))
                .withAlwaysSpawnEntity(true)
                .withBucketedFish(U.locItem("crittersandcompanions", "sea_bunny_bucket"))
                .withEntityToSpawn(U.holderEntity("crittersandcompanions", "sea_bunny"))
                .withSizeAndWeight(FishProperties.sizeWeight(40, 10, 200, 60))
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withRarity(FishProperties.Rarity.RARE)
        );

    }
}
