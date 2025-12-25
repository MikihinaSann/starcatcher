package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.items.*;
import com.wdiscute.starcatcher.items.cheater.*;
import com.wdiscute.starcatcher.items.helper.BasicItem;
import com.wdiscute.starcatcher.items.helper.FireResistantBasicItem;
import com.wdiscute.starcatcher.items.helper.SingleStackBasicItem;
import com.wdiscute.starcatcher.items.modifieritem.CatchModifierItem;
import com.wdiscute.starcatcher.items.modifieritem.MinigameModifierItem;
import com.wdiscute.starcatcher.items.modifieritem.TackleSkinItem;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.ModCatchModifiers;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.ModMinigameModifiers;
import com.wdiscute.starcatcher.registry.custom.tackleskin.ModTackleSkins;
import com.wdiscute.starcatcher.rod.StarcatcherFishingRodItem;
import com.wdiscute.starcatcher.secretnotes.NoteContainer;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModItems
{

    static void registerExtra()
    {
        //this works!
        if (ModList.get().isLoaded("tide"))
        {
            //DeferredItem<Item> FISH = ITEMS_REGISTRY.register("fish", FishItem::new);
        }
    }


    //fishes which have a model and swim in water
    DeferredRegister<Item> FISH_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> TRASH_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);

    DeferredRegister<Item> TEMPLATES_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> HOOKS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> BLOCKITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> BOBBERS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> BAITS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);
    DeferredRegister<Item> RODS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);

    DeferredRegister<Item> DEV_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);

    RegistryObject<Item> SETTINGS = DEV_REGISTRY.register(
            "settings", () -> new Item(new Item.Properties())
            {
                //dev stuff

            });

    RegistryObject<Item> MISSINGNO = DEV_REGISTRY.register("missingno", BasicItem::new);
    RegistryObject<Item> UNKNOWN_FISH = DEV_REGISTRY.register("unknown_fish", BasicItem::new);

    RegistryObject<Item> GUIDE = ITEMS_REGISTRY.register("starcatcher_guide", FishingGuideItem::new);

    RegistryObject<Item> FISH_RADAR = ITEMS_REGISTRY.register("fish_radar", SingleStackBasicItem::new);

    RegistryObject<Item> STARCATCHER_TWINE = ITEMS_REGISTRY.register("starcatcher_twine", SingleStackBasicItem::new);

    //hooks
    RegistryObject<Item> HOOK = HOOKS_REGISTRY.register("hook", SingleStackBasicItem::new);
    RegistryObject<Item> SHINY_HOOK = HOOKS_REGISTRY.register("shiny_hook", () -> new MinigameModifierItem(ModMinigameModifiers.SPAWN_TREASURE_ON_THREE_HITS));
    RegistryObject<Item> GOLD_HOOK = HOOKS_REGISTRY.register("gold_hook", () -> new CatchModifierItem(ModCatchModifiers.EXTRA_EXP_BASED_ON_PERFORMANCE));
    RegistryObject<Item> MOSSY_HOOK = HOOKS_REGISTRY.register("mossy_hook", () -> new MinigameModifierItem(ModMinigameModifiers.HARDER_WITH_TREASURE_ON_PERFECT));
    RegistryObject<Item> STONE_HOOK = HOOKS_REGISTRY.register("stone_hook", () -> new MinigameModifierItem(ModMinigameModifiers.STOP_DECAY_ON_HIT));
    RegistryObject<Item> SPLIT_HOOK = HOOKS_REGISTRY.register("split_hook", () -> new CatchModifierItem(ModCatchModifiers.EXTRA_ITEM));
    //TODO add stabilizing hook, no idea what for
    //RegistryObject<Item> STABILIZING_HOOK = HOOKS_REGISTRY.register("stabilizing_hook", () -> new MinigameModifierItem(ModMinigameModifiers.NO_FLIP));
    RegistryObject<Item> HEAVY_HOOK = HOOKS_REGISTRY.register("heavy_hook", () -> new MinigameModifierItem(ModMinigameModifiers.SLOWER_MOVING_SWEET_SPOTS));

    //bobbers
    RegistryObject<Item> BOBBER = BOBBERS_REGISTRY.register("bobber", SingleStackBasicItem::new);
    RegistryObject<Item> STEADY_BOBBER = BOBBERS_REGISTRY.register("steady_bobber", () -> new MinigameModifierItem(ModMinigameModifiers.BIGGER_GREEN_SWEET_SPOTS));
    RegistryObject<Item> CLEAR_BOBBER = BOBBERS_REGISTRY.register("clear_bobber", () -> new MinigameModifierItem(ModMinigameModifiers.SLOWER_VANISHING));
    RegistryObject<Item> AQUA_BOBBER = BOBBERS_REGISTRY.register("aqua_bobber", () -> new MinigameModifierItem(ModMinigameModifiers.ADD_AQUA_SWEET_SPOT));
    RegistryObject<Item> VANILLA_BOBBER = BOBBERS_REGISTRY.register("vanilla_bobber", () -> new CatchModifierItem(ModCatchModifiers.VANILLA_LOOT));

    //baits
    RegistryObject<Item> WORM = BAITS_REGISTRY.register("worm", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> ALMIGHTY_WORM = BAITS_REGISTRY.register("almighty_worm", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME, ModCatchModifiers.FISH_ENTITY));
    RegistryObject<Item> SEEKING_WORM = BAITS_REGISTRY.register("seeking_worm", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME, ModCatchModifiers.GUARANTEE_NEW_FISH_ALWAYS));

    RegistryObject<Item> GUNPOWDER_BAIT = BAITS_REGISTRY.register("gunpowder_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> CHERRY_BAIT = BAITS_REGISTRY.register("cherry_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> LUSH_BAIT = BAITS_REGISTRY.register("lush_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> SCULK_BAIT = BAITS_REGISTRY.register("sculk_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> DRIPSTONE_BAIT = BAITS_REGISTRY.register("dripstone_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> MURKWATER_BAIT = BAITS_REGISTRY.register("murkwater_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> LEGENDARY_BAIT = BAITS_REGISTRY.register("legendary_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME));
    RegistryObject<Item> METEOROLOGICAL_BAIT = BAITS_REGISTRY.register("meteorological_bait", () -> new CatchModifierItem(64, ModCatchModifiers.DECREASES_LURE_TIME,  ModCatchModifiers.IGNORE_DAYTIME_AND_WEATHER_RESTRICTIONS));


    //bobber skin templates
    RegistryObject<Item> PEARL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("pearl_smithing_template", () -> new TackleSkinItem(ModTackleSkins.PEARL_TACKLE_SKIN));
    RegistryObject<Item> KIMBE_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("kimbe_smithing_template", () -> new TackleSkinItem(ModTackleSkins.KIMBE_TACKLE_SKIN));
    RegistryObject<Item> COLORFUL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("colorful_smithing_template", () -> new TackleSkinItem(ModTackleSkins.COLORFUL_TACKLE_SKIN));
    RegistryObject<Item> CLEAR_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("clear_smithing_template", () -> new TackleSkinItem(ModTackleSkins.CLEAR_TACKLE_SKIN));
    RegistryObject<Item> FROG_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("frog_smithing_template", () -> new TackleSkinItem(ModTackleSkins.FROG_TACKLE_SKIN));
    RegistryObject<Item> KING_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("king_smithing_template", () -> new TackleSkinItem(ModTackleSkins.KING_TACKLE_SKIN));

    //rods
    RegistryObject<Item> ROD = RODS_REGISTRY.register("starcatcher_rod", StarcatcherFishingRodItem::new);

    //fishing rod skins
    RegistryObject<Item> NATURALIST_ROD = RODS_REGISTRY.register("naturalist_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> ICEBORN_ROD = RODS_REGISTRY.register("iceborn_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> MAGMAFORGED_ROD = RODS_REGISTRY.register("magmaforged_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> SLIMED_ROD = RODS_REGISTRY.register("slimed_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> SHARKTOOTH_ROD = RODS_REGISTRY.register("sharktooth_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> AZURE_CRYSTAL_ROD = RODS_REGISTRY.register("azure_crystal_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> GOOD_OLD_ROD = RODS_REGISTRY.register("good_old_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> BAMBOO_ROD = RODS_REGISTRY.register("bamboo_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> OBSIDIAN_ROD = RODS_REGISTRY.register("obsidian_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> ALPHA_ROD = RODS_REGISTRY.register("alpha_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> BONER_ROD = RODS_REGISTRY.register("boner_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> SKY_ROD = RODS_REGISTRY.register("sky_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> LUSH_GLOWBERRY_ROD = RODS_REGISTRY.register("lush_glowberry_rod", StarcatcherFishingRodItem::new);
    RegistryObject<Item> HUMBLE_ROD = RODS_REGISTRY.register("humble_rod", StarcatcherFishingRodItem::new);

    //secrets
    RegistryObject<Item> SECRET_NOTE = ITEMS_REGISTRY.register("secret_note", SecretNote::new);
    RegistryObject<Item> BROKEN_BOTTLE = ITEMS_REGISTRY.register("broken_bottle", BrokenBottle::new);

    //notes
    RegistryObject<Item> DRIFTING_WATERLOGGED_BOTTLE = ITEMS_REGISTRY.register("drifting_waterlogged_bottle", () -> new NoteContainer(SecretNote.Note.CRYSTAL_HOOK));

    RegistryObject<Item> SCALDING_BOTTLE = ITEMS_REGISTRY.register("scalding_bottle", () -> new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_1));

    RegistryObject<Item> BURNING_BOTTLE = ITEMS_REGISTRY.register("burning_bottle", () -> new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_2));

    RegistryObject<Item> HOPEFUL_BOTTLE = ITEMS_REGISTRY.register("hopeful_bottle", () -> new NoteContainer(SecretNote.Note.HOPEFUL_NOTE));

    RegistryObject<Item> HOPELESS_BOTTLE = ITEMS_REGISTRY.register("hopeless_bottle", () -> new NoteContainer(SecretNote.Note.HOPELESS_NOTE));

    RegistryObject<Item> TRUE_BLUE_BOTTLE = ITEMS_REGISTRY.register("true_blue_bottle", () -> new NoteContainer(SecretNote.Note.TRUE_BLUE));

    RegistryObject<Item> WITHERED_BOTTLE = ITEMS_REGISTRY.register("withered_bottle", () -> new NoteContainer(SecretNote.Note.WITHER));


    //cheater items
    RegistryObject<Item> AWARD_ALL_FISHES = DEV_REGISTRY.register("award_all_fishes", AwardAllFishes::new);
    RegistryObject<Item> AWARD_ONE_FISH = DEV_REGISTRY.register("award_one_fish", AwardOneFish::new);
    RegistryObject<Item> REVOKE_ALL_FISHES = DEV_REGISTRY.register("revoke_all_fishes", RevokeAllFishes::new);

    RegistryObject<Item> AWARD_ALL_TROPHIES = DEV_REGISTRY.register("award_all_trophies", AwardAllTrophies::new);
    RegistryObject<Item> REVOKE_ALL_TROPHIES = DEV_REGISTRY.register("revoke_all_trophies", RevokeAllTrophies::new);

    RegistryObject<Item> AWARD_ALL_SECRETS = DEV_REGISTRY.register("award_all_secrets", AwardAllSecrets::new);
    RegistryObject<Item> REVOKE_ALL_SECRETS = DEV_REGISTRY.register("revoke_all_secrets", RevokeAllSecrets::new);

    RegistryObject<Item> REVOKE_ALL_EXTRAS = DEV_REGISTRY.register("revoke_all_extras", RevokeAllExtras::new);

    //treasure
    RegistryObject<Item> WATERLOGGED_SATCHEL = ITEMS_REGISTRY.register("waterlogged_satchel", WaterloggedSatchel::new);

    RegistryObject<Item> FISH_BONES = ITEMS_REGISTRY.register("fish_bones", BasicItem::new);

    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //

    //lake
    RegistryObject<Item> OBIDONTIEE = FISH_REGISTRY.register("obidontiee", FishItem::new);
    RegistryObject<Item> SILVERVEIL_PERCH = FISH_REGISTRY.register("silverveil_perch", FishItem::new);
    RegistryObject<Item> ELDERSCALE = FISH_REGISTRY.register("elderscale", FishItem::new);
    RegistryObject<Item> DRIFTFIN = FISH_REGISTRY.register("driftfin", FishItem::new);
    RegistryObject<Item> TWILIGHT_KOI = FISH_REGISTRY.register("twilight_koi", FishItem::new);
    RegistryObject<Item> THUNDER_BASS = FISH_REGISTRY.register("thunder_bass", FishItem::new);
    RegistryObject<Item> LIGHTNING_BASS = FISH_REGISTRY.register("lightning_bass", FishItem::new);
    RegistryObject<Item> BOOT = TRASH_REGISTRY.register("boot", BasicItem::new);

    //swamp
    RegistryObject<Item> SLUDGE_CATFISH = FISH_REGISTRY.register("sludge_catfish", FishItem::new);
    RegistryObject<Item> LILY_SNAPPER = FISH_REGISTRY.register("lily_snapper", FishItem::new);
    RegistryObject<Item> SAGE_CATFISH = FISH_REGISTRY.register("sage_catfish", FishItem::new);
    RegistryObject<Item> MOSSY_BOOT = TRASH_REGISTRY.register("mossy_boot", BasicItem::new);

    //darkoak_forest
    RegistryObject<Item> PALE_CARP = FISH_REGISTRY.register("pale_carp", FishItem::new);
    RegistryObject<Item> PALE_PINFISH = FISH_REGISTRY.register("pale_pinfish", FishItem::new);
    RegistryObject<Item> PINFISH = FISH_REGISTRY.register("pinfish", FishItem::new);

    //icy lake
    RegistryObject<Item> FROSTJAW_TROUT = FISH_REGISTRY.register("frostjaw_trout", FishItem::new);
    RegistryObject<Item> CRYSTALBACK_TROUT = FISH_REGISTRY.register("crystalback_trout", FishItem::new);
    RegistryObject<Item> AURORA = FISH_REGISTRY.register("aurora", FishItem::new);
    RegistryObject<Item> WINTERY_PIKE = FISH_REGISTRY.register("wintery_pike", FishItem::new);

    //warm lake (desert/savanna etc)
    RegistryObject<Item> SANDTAIL = FISH_REGISTRY.register("sandtail", FishItem::new);
    RegistryObject<Item> MIRAGE_CARP = FISH_REGISTRY.register("mirage_carp", FishItem::new);
    RegistryObject<Item> SCORCHFISH = FISH_REGISTRY.register("scorchfish", FishItem::new);
    RegistryObject<Item> CACTIFISH = FISH_REGISTRY.register("cactifish", FishItem::new);
    RegistryObject<Item> AGAVE_BREAM = FISH_REGISTRY.register("agave_bream", FishItem::new);

    //mountain
    RegistryObject<Item> SUNNY_STURGEON = FISH_REGISTRY.register("sunny_sturgeon", FishItem::new);
    RegistryObject<Item> ROCKGILL = FISH_REGISTRY.register("rockgill", FishItem::new);
    RegistryObject<Item> PEAKDWELLER = FISH_REGISTRY.register("peakdweller", FishItem::new);
    RegistryObject<Item> SUN_SEEKING_CARP = FISH_REGISTRY.register("sun_seeking_carp", FishItem::new);

    //cherry grove
    RegistryObject<Item> BLOSSOMFISH = FISH_REGISTRY.register("blossomfish", FishItem::new);
    RegistryObject<Item> PETALDRIFT_CARP = FISH_REGISTRY.register("petaldrift_carp", FishItem::new);
    RegistryObject<Item> PINK_KOI = FISH_REGISTRY.register("pink_koi", FishItem::new);
    RegistryObject<Item> MORGANITE = FISH_REGISTRY.register("morganite", FishItem::new);
    RegistryObject<Item> ROSE_SIAMESE_FISH = FISH_REGISTRY.register("rose_siamese_fish", FishItem::new);
    RegistryObject<Item> VESANI = FISH_REGISTRY.register("vesani", FishItem::new);

    //icy mountain
    RegistryObject<Item> CRYSTALBACK_STURGEON = FISH_REGISTRY.register("crystalback_sturgeon", FishItem::new);
    RegistryObject<Item> ICETOOTH_STURGEON = FISH_REGISTRY.register("icetooth_sturgeon", FishItem::new);
    RegistryObject<Item> BOREAL = FISH_REGISTRY.register("boreal", FishItem::new);
    RegistryObject<Item> CRYSTALBACK_BOREAL = FISH_REGISTRY.register("crystalback_boreal", FishItem::new);

    //rivers
    RegistryObject<Item> SILVERFIN_PIKE = FISH_REGISTRY.register("silverfin_pike", FishItem::new);
    RegistryObject<Item> CARPENJOE = FISH_REGISTRY.register("carpenjoe", FishItem::new);
    RegistryObject<Item> WILLOW_BREAM = FISH_REGISTRY.register("willow_bream", FishItem::new);
    RegistryObject<Item> DRIFTING_BREAM = FISH_REGISTRY.register("drifting_bream", FishItem::new);
    RegistryObject<Item> DOWNFALL_BREAM = FISH_REGISTRY.register("downfall_bream", FishItem::new);
    RegistryObject<Item> HOLLOWBELLY_DARTER = FISH_REGISTRY.register("hollowbelly_darter", FishItem::new);
    RegistryObject<Item> MISTBACK_CHUB = FISH_REGISTRY.register("mistback_chub", FishItem::new);
    RegistryObject<Item> BLUEGIGI = FISH_REGISTRY.register("bluegigi", FishItem::new);
    RegistryObject<Item> DRIED_SEAWEED = TRASH_REGISTRY.register("dried_seaweed", FishItem::new);

    //icy river
    RegistryObject<Item> FROSTGILL_CHUB = FISH_REGISTRY.register("frostgill_chub", FishItem::new);
    RegistryObject<Item> CRYSTALBACK_MINNOW = FISH_REGISTRY.register("crystalback_minnow", FishItem::new);
    RegistryObject<Item> AZURE_CRYSTALBACK_MINNOW = FISH_REGISTRY.register("azure_crystalback_minnow", FishItem::new);
    RegistryObject<Item> BLUE_CRYSTAL_FIN = FISH_REGISTRY.register("blue_crystal_fin", FishItem::new);

    //saltwater
    RegistryObject<Item> IRONJAW_HERRING = FISH_REGISTRY.register("ironjaw_herring", FishItem::new);
    RegistryObject<Item> DEEPJAW_HERRING = FISH_REGISTRY.register("deepjaw_herring", FishItem::new);
    RegistryObject<Item> DUSKTAIL_SNAPPER = FISH_REGISTRY.register("dusktail_snapper", FishItem::new);
    RegistryObject<Item> JOEL = FISH_REGISTRY.register("joel", FishItem::new);
    RegistryObject<Item> REDSCALED_TUNA = FISH_REGISTRY.register("redscaled_tuna", FishItem::new);
    RegistryObject<Item> BIGEYE_TUNA = FISH_REGISTRY.register("bigeye_tuna", FishItem::new);
    RegistryObject<Item> SEA_BASS = FISH_REGISTRY.register("sea_bass", FishItem::new);
    RegistryObject<Item> WATERLOGGED_BOTTLE = TRASH_REGISTRY.register("waterlogged_bottle", BasicItem::new);

    //beaches
    RegistryObject<Item> CONCH = TRASH_REGISTRY.register("conch", BasicItem::new);
    RegistryObject<Item> CLAM = TRASH_REGISTRY.register("clam", BasicItem::new);

    //mushroom islands
    RegistryObject<Item> SHROOMFISH = FISH_REGISTRY.register("shroomfish", FishItem::new);
    RegistryObject<Item> SPOREFISH = FISH_REGISTRY.register("sporefish", FishItem::new);

    //underground
    RegistryObject<Item> GOLD_FAN = FISH_REGISTRY.register("gold_fan", FishItem::new);
    RegistryObject<Item> GEODE_EEL = FISH_REGISTRY.register("geode_eel", FishItem::new);

    //caves
    RegistryObject<Item> WHITEVEIL = FISH_REGISTRY.register("whiteveil", FishItem::new);
    RegistryObject<Item> BLACK_EEL = FISH_REGISTRY.register("black_eel", FishItem::new);
    RegistryObject<Item> AMETHYSTBACK = FISH_REGISTRY.register("amethystback", FishItem::new);
    RegistryObject<Item> STONEFISH = FISH_REGISTRY.register("stonefish", FishItem::new);

    //dripstone caves
    RegistryObject<Item> FOSSILIZED_ANGELFISH = FISH_REGISTRY.register("fossilized_angelfish", FishItem::new);
    RegistryObject<Item> DRIPFIN = FISH_REGISTRY.register("dripfin", FishItem::new);
    RegistryObject<Item> YELLOWSTONE_FISH = FISH_REGISTRY.register("yellowstone_fish", FishItem::new);

    //lush caves
    RegistryObject<Item> LUSH_PIKE = FISH_REGISTRY.register("lush_pike", FishItem::new);
    RegistryObject<Item> VIVID_MOSS = FISH_REGISTRY.register("vivid_moss", FishItem::new);
    RegistryObject<Item> THE_QUARRISH = FISH_REGISTRY.register("the_quarrish", FishItem::new);

    //deepslate
    RegistryObject<Item> GHOSTLY_PIKE = FISH_REGISTRY.register("ghostly_pike", FishItem::new);
    RegistryObject<Item> AQUAMARINE_PIKE = FISH_REGISTRY.register("aquamarine_pike", FishItem::new);
    RegistryObject<Item> GARNET_MACKEREL = FISH_REGISTRY.register("garnet_mackerel", FishItem::new);
    RegistryObject<Item> BRIGHT_AMETHYST_SNAPPER = FISH_REGISTRY.register("bright_amethyst_snapper", FishItem::new);
    RegistryObject<Item> DARK_AMETHYST_SNAPPER = FISH_REGISTRY.register("dark_amethyst_snapper", FishItem::new);
    RegistryObject<Item> DEEPSLATEFISH = FISH_REGISTRY.register("deepslatefish", FishItem::new);

    //deep dark
    RegistryObject<Item> SCULKFISH = FISH_REGISTRY.register("sculkfish", FishItem::new);
    RegistryObject<Item> WARD = FISH_REGISTRY.register("ward", FishItem::new);
    RegistryObject<Item> GLOWING_DARK = FISH_REGISTRY.register("glowing_dark", FishItem::new);

    //overworld surface lava
    RegistryObject<Item> SUNEATER = FISH_REGISTRY.register("suneater", FireResistantBasicItem::new);
    RegistryObject<Item> PYROTROUT = FISH_REGISTRY.register("pyrotrout", FireResistantBasicItem::new);
    RegistryObject<Item> OBSIDIAN_EEL = FISH_REGISTRY.register("obsidian_eel", FireResistantBasicItem::new);

    //overworld underground lava
    RegistryObject<Item> MOLTEN_SHRIMP = FISH_REGISTRY.register("molten_shrimp", FireResistantBasicItem::new);
    RegistryObject<Item> OBSIDIAN_CRAB = FISH_REGISTRY.register("obsidian_crab", FireResistantBasicItem::new);

    //overworld deepslate lava
    RegistryObject<Item> SCORCHED_BLOODSUCKER = FISH_REGISTRY.register("scorched_bloodsucker", FireResistantBasicItem::new);
    RegistryObject<Item> MOLTEN_DEEPSLATE_CRAB = FISH_REGISTRY.register("molten_deepslate_crab", FireResistantBasicItem::new);

    //nether
    RegistryObject<Item> EMBERGILL = FISH_REGISTRY.register("embergill", FireResistantBasicItem::new);
    RegistryObject<Item> SCALDING_PIKE = FISH_REGISTRY.register("scalding_pike", FireResistantBasicItem::new);
    RegistryObject<Item> CINDER_SQUID = FISH_REGISTRY.register("cinder_squid", FireResistantBasicItem::new);
    RegistryObject<Item> LAVA_CRAB = FISH_REGISTRY.register("lava_crab", FireResistantBasicItem::new);
    RegistryObject<Item> MAGMA_FISH = FISH_REGISTRY.register("magma_fish", FireResistantBasicItem::new);
    RegistryObject<Item> GLOWSTONE_SEEKER = FISH_REGISTRY.register("glowstone_seeker", FireResistantBasicItem::new);
    RegistryObject<Item> GLOWSTONE_PUFFERFISH = FISH_REGISTRY.register("glowstone_pufferfish", FireResistantBasicItem::new);
    RegistryObject<Item> WILLISH = FISH_REGISTRY.register("willish", FireResistantBasicItem::new);

    RegistryObject<Item> CERBERAY = FISH_REGISTRY.register("cerberay", FireResistantBasicItem::new);

    RegistryObject<Item> LAVA_CRAB_CLAW = TRASH_REGISTRY.register("lava_crab_claw", FireResistantBasicItem::new);

    //the end
    RegistryObject<Item> CHARFISH = FISH_REGISTRY.register("charfish", FishItem::new);
    RegistryObject<Item> CHORUS_CRAB = FISH_REGISTRY.register("chorus_crab", FishItem::new);
    RegistryObject<Item> END_GLOW = FISH_REGISTRY.register("end_glow", FishItem::new);
    RegistryObject<Item> VOIDBITER = FISH_REGISTRY.register("voidbiter", FishItem::new);

    //bucket
    RegistryObject<Item> STARCAUGHT_BUCKET = ITEMS_REGISTRY.register("starcaught_bucket", () -> new StarcaughtBucket(Fluids.WATER));
}
