package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.storage.FishProperties;

public class DGUnusualFishFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {

        //
        //  ,---.   ,--.                    ,--.              ,-----.
        // /  O  \  |  |  ,---.  ,--.  ,--. |  |  ,---.      '  .--./  ,--,--. ,--.  ,--.  ,---.   ,---.
        //|  .-.  | |  | | .-. :  \  `'  /  `-'  (  .-'      |  |     ' ,-.  |  \  `'  /  | .-. : (  .-'
        //|  | |  | |  | \   --.  /  /.  \       .-'  `)     '  '--'\ \ '-'  |   \    /   \   --. .-'  `)
        //`--' `--' `--'  `----' '--'  '--'      `----'       `-----'  `--`--'    `--'     `----' `----'
        //


        //ocean
        register(fish(U.holderItem("unusualfishmod", "wizard_jelly_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "wizard_jelly_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "wizard_jelly"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDaytime(FishProperties.Daytime.NIGHT)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_aero_mono"))
                .withBucketedFish(U.holderItem("unusualfishmod", "aero_mono_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "aero_mono"))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_beaked_herring"))
                .withBucketedFish(U.holderItem("unusualfishmod", "beaked_herring_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "beaked_herring"))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "brick_snail_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "brick_snail_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "brick_snail"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "celestial_fish_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "celestial"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDaytime(FishProperties.Daytime.NIGHT)
        );

        //deep oceans
        register(fish(U.holderItem("unusualfishmod", "tribble_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "tribble"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("demon_herring")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "sea_spider_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sea_spider"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //cold oceans
        register(fish(U.holderItem("unusualfishmod", "volt_angler_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "volt_angler"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "blizzardfin_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "blizzardfin"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_snowflake"))
                .withBucketedFish(U.holderItem("unusualfishmod", "snowflake_tail_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "snowflaketail"))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //lukewarm oceans
        register(fish(U.holderItem("unusualfishmod", "trumpet_squid_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "trumpet_squid_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "trumpet_squid"))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "squoddle_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "squoddle_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "squoddle"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "spoon_shark_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "spoon_shark"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("circus")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "sea_pancake_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sea_pancake"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_copperflame_anthias"))
                .withBucketedFish(U.holderItem("unusualfishmod", "copperflame_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "copperflame"))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("forkfish")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "picklefish_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "picklefish_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "picklefish"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "porcupine_lobsta_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "porcupine_lobsta_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "porcupine_lobsta"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //warm ocean
        register(fish(U.holderItem("unusualfishmod", "tiger_puffer_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "tiger_puffer_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "tiger_puffer"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "zebra_cornetfish_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "zebra_cornetfish"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("amber_goby")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_sneep_snorp"))
                .withBucketedFish(U.holderItem("unusualfishmod", "sneepsnorp_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sneep_snorp"))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "sea_mosquito_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "sea_mosquito_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sea_mosquito"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "clownthorn_shark_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "clownthorn_shark_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "clownthorn_shark"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "coral_skrimp_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "coral_skrimp_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "coral_skrimp"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "crimsonshell_squid_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "crimsonshell_squid_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "crimsonshell"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("duality_damselfish")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("spindlefish")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );


        //river
        register(helper("triple_twirl_pleco")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("blackcap_snail")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("ripper")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "pinkfin_idol_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "pinkfin_idol_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "pinkfin"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_RIVER)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );


        //swamp(s)
        register(helper("bark_angelfish")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP_ONLY)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "roughback_guitarfish_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "roughback_guitarfish_spawn_egg"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP_ONLY)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("stout_bichir")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("drooping_gourami")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "lobed_skipper_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "lobed_skipper_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "skipper"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "muddytop_snail_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "muddytop_snail_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "muddytop"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("sailor_barb")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //jungle
        register(fish(U.holderItem("unusualfishmod", "tiger_jungle_shark_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "jungleshark"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("eyelash")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("freshwater_mantis")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "gnasher_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "gnasher"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDaytime(FishProperties.Daytime.NIGHT)
        );

        //savanna
        register(fish(U.holderItem("unusualfishmod", "rhino_tetra_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "rhino_tetra"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SAVANNA)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //mushroom fields
        register(fish(U.holderItem("unusualfishmod", "kalappa_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "kalappa"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );


        //underground
        register(fish(U.holderItem("unusualfishmod", "raw_blind_sailfin"))
                .withBucketedFish(U.holderItem("unusualfishmod", "blind_sailfin_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "blindsailfin"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                        .withMustBeCaughtBelowY(30))
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("deep_crawler")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                        .withMustBeCaughtBelowY(20))
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_hatchetfish"))
                .withBucketedFish(U.holderItem("unusualfishmod", "hatchet_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "hatchet_fish"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEPSLATE)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("mossthorn")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES
                        .withMustBeCaughtBelowY(40))
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "prawn_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "prawn"))
                .withAlwaysSpawnEntity()
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DRIPSTONE_CAVES)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("shockcat")
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                        .withMustBeCaughtBelowY(10))
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );
    }

    public static FishProperties.Builder helperOnlyBucket(String s)
    {
        return fish(U.holderItem("unusualfishmod", s + "_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", s + "_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", s))
                .withAlwaysSpawnEntity()
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000)
                );
    }

    public static FishProperties.Builder helper(String s)
    {
        return fish(U.holderItem("unusualfishmod", "raw_" + s))
                .withBucketedFish(U.holderItem("unusualfishmod", s + "_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", s))
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000)
                );
    }
}
