package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.blocks.ModBlocks;
import com.wdiscute.starcatcher.registry.fishing.TrophyPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;

import static com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry.*;

public class DGTrophies extends TrophyPropertiesRegistry
{
    public static void bootstrap()
    {


        registerTrophy(TrophyProperties.builder()
                        .setFish(ModBlocks.TROPHY_BRONZE.asItem().builtInRegistryHolder())
                        .hideUntilCaught()
                        .setTrophyType(TrophyProperties.TrophyType.TROPHY)
                        .setAllProgress(new TrophyProperties.RarityProgress(50, 20))
        );

        registerTrophy(TrophyProperties.builder()
                        .setFish(ModBlocks.TROPHY_SILVER.asItem().builtInRegistryHolder())
                        .hideUntilCaught()
                        .setTrophyType(TrophyProperties.TrophyType.TROPHY)
                        .setAllProgress(new TrophyProperties.RarityProgress(100, 50))
        );

        registerTrophy(TrophyProperties.builder()
                        .setFish(ModBlocks.TROPHY_GOLD.asItem().builtInRegistryHolder())
                        .hideUntilCaught()
                        .setTrophyType(TrophyProperties.TrophyType.TROPHY)
                        .setAllProgress(new TrophyProperties.RarityProgress(200, 0))
                        .withProgress(FishProperties.Rarity.COMMON, new TrophyProperties.RarityProgress(0, 44))
                        .withProgress(FishProperties.Rarity.UNCOMMON, new TrophyProperties.RarityProgress(0, 23))
                        .withProgress(FishProperties.Rarity.RARE, new TrophyProperties.RarityProgress(0, 15))
                        .withProgress(FishProperties.Rarity.EPIC, new TrophyProperties.RarityProgress(0, 16))
                        .withProgress(FishProperties.Rarity.LEGENDARY, new TrophyProperties.RarityProgress(0, 10))
        );
        //                                         ,--.
        // ,---.   ,---.   ,---. ,--.--.  ,---.  ,-'  '-.  ,---.
        //(  .-'  | .-. : | .--' |  .--' | .-. : '-.  .-' (  .-'
        //.-'  `) \   --. \ `--. |  |    \   --.   |  |   .-'  `)
        //`----'   `----'  `---' `--'     `----'   `--'   `----'
        //

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldFish(ModItems.DRIFTING_WATERLOGGED_BOTTLE))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .setAllProgress(new TrophyProperties.RarityProgress(6, 15))
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldSurfaceLava(ModItems.SCALDING_BOTTLE))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .setAllProgress(new TrophyProperties.RarityProgress(0, 27))
                        .withChanceToCatch(33)
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldSurfaceLava(ModItems.BURNING_BOTTLE))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .setAllProgress(new TrophyProperties.RarityProgress(0, 42))
                        .withChanceToCatch(33)
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldDeepOceanFish(ModItems.HOPEFUL_BOTTLE))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .withProgress(FishProperties.Rarity.EPIC, new TrophyProperties.RarityProgress(5, 0))
                        .withChanceToCatch(33)
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldDeepOceanFish(ModItems.HOPELESS_BOTTLE))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .withProgress(FishProperties.Rarity.EPIC, new TrophyProperties.RarityProgress(5, 0))
                        .withChanceToCatch(33)
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldRiverFish(ModItems.TRUE_BLUE_BOTTLE))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .withProgress(FishProperties.Rarity.LEGENDARY, new TrophyProperties.RarityProgress(1, 0))
                        .withChanceToCatch(1)
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(
                                fish(ModItems.WITHERED_BOTTLE)
                                        .withBaseChance(0)
                                        .withBaitRestrictions(
                                                FishProperties.BaitRestrictions.DEFAULT
                                                        .withCorrectBait(BuiltInRegistries.ITEM.getKey(Items.WITHER_SKELETON_SKULL))
                                                        .withCorrectBaitChanceAdded(200)
                                        ))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
        );

        registerTrophy(TrophyProperties.builder()
                        .withFP(overworldDeepslateFish(ModItems.CRYSTAL_HOOK))
                        .setTrophyType(TrophyProperties.TrophyType.SECRET)
                        .withProgress(FishProperties.Rarity.EPIC, new TrophyProperties.RarityProgress(1, 0))
        );

        //
        //          ,--.   ,--.
        // ,---.  ,-'  '-. |  ,---.   ,---.  ,--.--.  ,---.
        //| .-. | '-.  .-' |  .-.  | | .-. : |  .--' (  .-'
        //' '-' '   |  |   |  | |  | \   --. |  |    .-'  `)
        // `---'    `--'   `--' `--'  `----' `--'    `----'
        //

        registerTrophy(TrophyProperties.builder().withFP(
                                overworldDeepslateFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND)))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withProgress(FishProperties.Rarity.RARE, new TrophyProperties.RarityProgress(1, 4))
        );

        registerTrophy(TrophyProperties.builder().withFP(
                                netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLD_BLOCK)))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withProgress(FishProperties.Rarity.LEGENDARY, new TrophyProperties.RarityProgress(3, 0))
                        .withChanceToCatch(33)
        );

        registerTrophy(TrophyProperties.builder().withFP(
                                netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_SCRAP)))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withProgress(FishProperties.Rarity.LEGENDARY, new TrophyProperties.RarityProgress(0, 10))
                        .withChanceToCatch(5)
                        .withRepeatable(true)
        );

        registerTrophy(TrophyProperties.builder().withFP(
                                netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );


        //naturalist
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldForestFish(ModItems.NATURALIST_ROD))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //magma forged
        registerTrophy(TrophyProperties.builder().withFP(
                                netherLavaBasaltDeltasFish(ModItems.MAGMAFORGED_ROD))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //bamboo rod
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldFish(ModItems.BAMBOO_ROD)
                                        .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                                                .withBiomes(Biomes.BAMBOO_JUNGLE.location())))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //bamboo rod
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldSurfaceLava(ModItems.OBSIDIAN_ROD))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //boner rod
        registerTrophy(TrophyProperties.builder().withFP(
                                netherLavaSoulSandValleyFish(ModItems.BONER_ROD))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //sky rod
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldFish(ModItems.SKY_ROD)
                                        .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                                                .withMustBeCaughtAboveY(300)))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //lush glowberry
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldLushCavesFish(ModItems.LUSH_GLOWBERRY_ROD))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //humble rod
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldRiverFish(ModItems.HUMBLE_ROD))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(1)
                        .withRepeatable(true)
        );

        //neptunium ingot
        registerTrophy(TrophyProperties.builder().withFP(
                                overworldColdOceanFish(U.holderItem("aquaculture", "neptunium_ingot")))
                        .setTrophyType(TrophyProperties.TrophyType.EXTRA)
                        .withChanceToCatch(2)
                        .withRepeatable(true)
        );




    }
}
