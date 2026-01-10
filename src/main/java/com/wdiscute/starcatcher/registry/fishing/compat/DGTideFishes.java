package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.FishProperties.WorldRestrictions.Seasons;
import net.minecraft.tags.BiomeTags;

public class DGTideFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {
        //
        //  ,--.   ,--.    ,--.
        //,-'  '-. `--'  ,-|  |  ,---.
        //'-.  .-' ,--. ' .-. | | .-. :
        //  |  |   |  | \ `-' | \   --.
        //  `--'   `--'  `---'   `----'
        //

        register(overworldColdLakeFish(U.locItem("tide", "trout"))
                .withBucketedFish(U.locItem("tide", "trout_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "trout"))
                .withSeasons(Seasons.SPRING, Seasons.WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 2000, 1600))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withDaytime(FishProperties.Daytime.DAY)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldLakeFish(U.locItem("tide", "bass"))
                .withBucketedFish(U.locItem("tide", "bass_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "bass"))
                .withSeasons(Seasons.SPRING, Seasons.WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 2000, 1600))
                .withWeather(FishProperties.Weather.CLEAR)
        );

        register(overworldLakeFish(U.locItem("tide", "yellow_perch"))
                .withBucketedFish(U.locItem("tide", "yellow_perch_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "yellow_perch"))
                .withSeasons(Seasons.SPRING, Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(25, 10, 200, 20))
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withWeather(FishProperties.Weather.RAIN)
        );

        register(overworldMountainFish(U.locItem("tide", "bluegill"))
                .withBucketedFish(U.locItem("tide", "bluegill_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "bluegill"))
                .withSeasons(Seasons.SPRING, Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 200, 20))
        );

        register(overworldWarmMountainFish(U.locItem("tide", "mint_carp"))
                .withBucketedFish(U.locItem("tide", "mint_carp_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "mint_carp"))
                .withSeasons(Seasons.SUMMER, Seasons.AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 10000, 5000))
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withWeather(FishProperties.Weather.RAIN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldColdRiverFish(U.locItem("tide", "pike"))
                .withBucketedFish(U.locItem("tide", "pike_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "pike"))
                .withSeasons(Seasons.WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 5000))
        );

        register(overworldWarmLakeFish(U.locItem("tide", "guppy"))
                .withBucketedFish(U.locItem("tide", "guppy_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "guppy"))
                .withSizeAndWeight(FishProperties.sizeWeight(4, 1, 2, 1))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withDaytime(FishProperties.Daytime.NIGHT)
        );

        register(overworldColdLakeFish(U.locItem("tide", "catfish"))
                .withBucketedFish(U.locItem("tide", "catfish_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "catfish"))
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 5000))
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldColdLakeFish(U.locItem("tide", "clayfish"))
                .withBucketedFish(U.locItem("tide", "clayfish_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "clayfish"))
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 200, 100))
                .withWeather(FishProperties.Weather.RAIN)
        );

        //tide saltwater
        register(overworldOceanFish(U.locItem("tide", "tuna"))
                .withBucketedFish(U.locItem("tide", "tuna_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "tuna"))
                .withSeasons(Seasons.AUTUMN, Seasons.WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(200, 100, 200000, 150000))
        );

        register(overworldColdOceanFish(U.locItem("tide", "ocean_perch"))
                .withBucketedFish(U.locItem("tide", "ocean_perch_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "ocean_perch"))
                .withSeasons(Seasons.AUTUMN, Seasons.WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 2000, 1600))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withDaytime(FishProperties.Daytime.NIGHT)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldOceanFish(U.locItem("tide", "mackerel"))
                .withBucketedFish(U.locItem("tide", "mackerel_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "mackerel"))
                .withSeasons(Seasons.AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(35, 15, 500, 400))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldWarmOceanFish(U.locItem("tide", "angelfish"))
                .withBucketedFish(U.locItem("tide", "angelfish_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "angelfish"))
                .withSeasons(Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 100, 5))
                .withWeather(FishProperties.Weather.RAIN)
        );

        register(overworldOceanFish(U.locItem("tide", "barracuda"))
                .withBucketedFish(U.locItem("tide", "barracuda_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "barracuda"))
                .withSizeAndWeight(FishProperties.sizeWeight(150, 50, 30000, 20000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDaytime(FishProperties.Daytime.NIGHT)
                .withWeather(FishProperties.Weather.RAIN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
        );

        register(overworldWarmOceanFish(U.locItem("tide", "sailfish"))
                .withBucketedFish(U.locItem("tide", "sailfish_bucket"))
                .withEntityToSpawn(U.holderEntity("tide", "sailfish"))
                .withSeasons(Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(250, 50, 100000, 50000))
                .withWeather(FishProperties.Weather.RAIN)
        );

        //tide underground
        register(overworldCavesFish(U.locItem("tide", "cave_eel"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 5, 3))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(overworldCavesFish(U.locItem("tide", "crystal_shrimp"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(10, 5, 2, 1))
        );

        register(overworldCavesFish(U.locItem("tide", "iron_tetra"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(4, 1, 2, 1))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
        );

        register(overworldCavesFish(U.locItem("tide", "glowfish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(20, 10, 10, 5))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldCavesFish(U.locItem("tide", "anglerfish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 20000, 15000))
        );

        register(overworldCavesFish(U.locItem("tide", "cave_crawler"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1000, 500))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(overworldCavesFish(U.locItem("tide", "gilded_minnow"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(5, 2, 10, 6))
        );

        //tide deepslate
        register(overworldDeepslateFish(U.locItem("tide", "deep_grouper"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(200, 50, 200000, 100000))
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
        );

        register(overworldDeepslateFish(U.locItem("tide", "shadow_snapper"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 10000, 5000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldDeepslateFish(U.locItem("tide", "abyss_angler"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 20000, 15000))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withBaseChance(2)
        );

        register(overworldDeepslateFish(U.locItem("tide", "lapis_lanternfish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(20, 10, 100, 5))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldDeepslateFish(U.locItem("tide", "luminescent_jellyfish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(50, 30, 5000, 3000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
        );

        register(overworldDeepslateFish(U.locItem("tide", "crystalline_carp"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(80, 30, 10000, 5000))
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldDeepslateFish(U.locItem("tide", "bedrock_tetra"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(4, 1, 2, 1))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        //tide biome specific
        register(fish(U.locItem("tide", "prarie_pike"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT.withBiomes(U.rl("minecraft", "plains")))
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.locItem("tide", "sandskipper"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1000, 500))
                .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT.withBiomes(U.rl("minecraft", "desert")))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_MOVING)
        );

        register(overworldCherryGroveFish(U.locItem("tide", "blossom_bass"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SPRING)
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 5000, 3000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(overworldFish(U.locItem("tide", "oakfish"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SPRING, Seasons.AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 3000, 2000))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD.withBiomesTags(BiomeTags.IS_FOREST.location()))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldColdLakeFish(U.locItem("tide", "frostbite_flounder"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.AUTUMN, Seasons.WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 30, 6000, 4000))
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
        );

        register(overworldFish(U.locItem("tide", "mirage_catfish"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.FOUR_BIG)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD.withBiomesTags(BiomeTags.IS_BADLANDS.location()))
        );

        register(overworldDeepDarkFish(U.locItem("tide", "echofin_snapper"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(80, 30, 10000, 5000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
        );

        register(overworldFish(U.locItem("tide", "sunspike_goby"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 100, 5))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD.withBiomesTags(BiomeTags.IS_BADLANDS.location()))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(overworldFish(U.locItem("tide", "birch_trout"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 2000, 1700))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD.withBiomesTags(StarcatcherTags.IS_BIRCH_FOREST))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldMountainFish(U.locItem("tide", "stonefish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 2000, 1700))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldDripstoneCavesFish(U.locItem("tide", "dripstone_darter"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(5, 2, 5, 1))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(overworldSwampFish(U.locItem("tide", "slimefin_snapper"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 10000, 10000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
        );

        register(overworldMushroomFieldsFish(U.locItem("tide", "sporestalker"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 5000, 3000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
        );

        register(overworldJungleFish(U.locItem("tide", "leafback"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SPRING, Seasons.AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 3000, 2000))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.FOUR_BIG_MOVING)
        );

        register(overworldLushCavesFish(U.locItem("tide", "fluttergill"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1000, 500))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(overworldTaigaFish(U.locItem("tide", "pine_perch"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SPRING, Seasons.AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(25, 10, 500, 300))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        //missing structure restriction support to add windbass and aquathorn from tide mod

        //tide overworld lava
        register(overworldSurfaceLava(U.locItem("tide", "ember_koi"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 3000, 2000))
                .withDifficulty(FishProperties.Difficulty.FOUR_BIG)
                .withRarity(FishProperties.Rarity.EPIC)
        );

        register(overworldSurfaceLava(U.locItem("tide", "inferno_guppy"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(4, 1, 20, 2))
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(overworldSurfaceLava(U.locItem("tide", "obsidian_pike"))
                //no bucketed version
                //no entity version
                .withSeasons(Seasons.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 5, 15000, 10000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(overworldSurfaceLava(U.locItem("tide", "volcano_tuna"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(200, 100, 150000, 50000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
        );

        //tide nether
        register(netherLavaFish(U.locItem("tide", "magma_mackerel"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(35, 15, 500, 300))
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(netherLavaBasaltDeltasFish(U.locItem("tide", "ashen_perch"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(25, 10, 200, 100))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(netherLavaSoulSandValleyFish(U.locItem("tide", "soulscaler"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(80, 30, 10000, 5000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
        );

        register(netherLavaWarpedForestFish(U.locItem("tide", "warped_guppy"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(4, 1, 4, 1))
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
        );

        register(netherLavaCrimsonForestFish(U.locItem("tide", "crimson_fangjaw"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(netherLavaSoulSandValleyFish(U.locItem("tide", "witherfin"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
        );

        register(netherLavaFish(U.locItem("tide", "blazing_swordfish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(200, 100, 100000, 50000))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.FOUR_BIG)
        );

        //tide end
        register(endFish(U.locItem("tide", "endstone_perch"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(25, 10, 500, 300))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
        );

        register(endFish(U.locItem("tide", "enderfin"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 5000, 3000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
        );

        register(endFish(U.locItem("tide", "endergazer"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(60, 30, 6000, 4000))
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withRarity(FishProperties.Rarity.EPIC)
        );

        register(endOuterIslandsFish(U.locItem("tide", "purpur_pike"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
        );

        register(endOuterIslandsFish(U.locItem("tide", "chorus_cod"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                .withRarity(FishProperties.Rarity.EPIC)
        );

        register(endFish(U.locItem("tide", "elytrout"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(50, 20, 5000, 3000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(endFish(U.locItem("tide", "voidseeker"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 15000, 10000))
                .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
        );

        //TODO put into corresponding category
        register(overworldLakeFish(U.locItem("tide", "midas_fish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(800, 50, 310000, 120000))
                .withWeather(FishProperties.Weather.THUNDER)
                .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDifficulty(FishProperties.Difficulty.THREE_BIG_TWO_THIN_VANISHING)
        );

        register(overworldOceanFish(U.locItem("tide", "shooting_starfish"))
                //no bucketed version
                //no entity version
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1000, 500))
                .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDaytime(FishProperties.Daytime.MIDNIGHT)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );
    }
}
