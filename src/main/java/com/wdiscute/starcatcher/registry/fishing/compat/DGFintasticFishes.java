package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;

public class DGFintasticFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {

        //
        //,------. ,--.            ,--.                      ,--.   ,--.
        //|  .---' `--' ,--,--,  ,-'  '-.  ,--,--.  ,---.  ,-'  '-. `--'  ,---.
        //|  `--,  ,--. |      \ '-.  .-' ' ,-.  | (  .-'  '-.  .-' ,--. | .--'
        //|  |`    |  | |  ||  |   |  |   \ '-'  | .-'  `)   |  |   |  | \ `--.
        //`--'     `--' `--''--'   `--'    `--`--' `----'    `--'   `--'  `---'
        //


        register(fish(U.holderItem("fintastic", "minnow_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "minnow_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "minnow"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SURFACE
                        .withBiomesTags(BiomeTags.IS_JUNGLE.location(), StarcatcherTags.IS_SWAMP, StarcatcherTags.IS_RIVER))
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(fish(U.holderItem("fintastic", "featherback_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "featherback_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "featherback"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
        );

        register(fish(U.holderItem("fintastic", "guppy_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "guppy_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "guppy"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLES_AND_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY)
        );

        register(fish(U.holderItem("fintastic", "arapaima_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "arapaima_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "arapaima"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(fish(U.holderItem("fintastic", "pleco_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "pleco_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "pleco"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLES_AND_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
        );

        register(fish(U.holderItem("fintastic", "moony_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "moony_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "moony"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SURFACE
                        .withBiomes(Biomes.BEACH.location(), Biomes.MANGROVE_SWAMP.location())
                        .withBiomesTags(StarcatcherTags.IS_LUKEWARM_OCEAN, StarcatcherTags.IS_WARM_OCEAN))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
        );

        register(fish(U.holderItem("fintastic", "coelacanth_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("fintastic", "coelacanth"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SURFACE
                        .withBiomesTags(StarcatcherTags.IS_WARM_OCEAN))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
        );

        register(fish(U.holderItem("fintastic", "gourami_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "gourami_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "gourami"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                        .withBiomesTags(StarcatcherTags.IS_SWAMP))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
        );

        register(fish(U.holderItem("fintastic", "daphnia_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "daphnia_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "daphnia"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(fish(U.holderItem("fintastic", "artemia_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "artemia_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "artemia"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );
    }
}
