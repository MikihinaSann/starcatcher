package com.wdiscute.starcatcher;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class StarcatcherTags
{

    public static final ResourceLocation IS_COLD_LAKE = Starcatcher.rl("is_cold_lake");
    public static final ResourceLocation IS_COLD_RIVER = Starcatcher.rl("is_cold_river");
    public static final ResourceLocation IS_COLD_OCEAN = Starcatcher.rl("is_cold_ocean");

    public static final ResourceLocation IS_WARM_LAKE = Starcatcher.rl("is_warm");
    public static final ResourceLocation IS_WARM_RIVER = Starcatcher.rl("is_warm_river");
    public static final ResourceLocation IS_WARM_OCEAN = Starcatcher.rl("is_warm_ocean");

    public static final ResourceLocation IS_OCEAN = Starcatcher.rl("is_ocean");
    public static final ResourceLocation IS_DEEP_OCEAN = Starcatcher.rl("is_deep_ocean");
    public static final ResourceLocation IS_RIVER = Starcatcher.rl("is_river");

    public static final ResourceLocation IS_BEACH = Starcatcher.rl("is_beach");
    public static final ResourceLocation IS_SWAMP = Starcatcher.rl("is_swamp");
    public static final ResourceLocation IS_CHERRY_GROVE = Starcatcher.rl("is_cherry_grove");
    public static final ResourceLocation IS_MUSHROOM_FIELDS = Starcatcher.rl("is_mushroom_fields");
    public static final ResourceLocation IS_DARK_FOREST = Starcatcher.rl("is_dark_forest");
    public static final ResourceLocation IS_BIRCH_FOREST = Starcatcher.rl("is_birch_forest");

    public static final TagKey<Item> HOOKS = ItemTags.create(Starcatcher.rl("hooks"));
    public static final TagKey<Item> BOBBERS = ItemTags.create(Starcatcher.rl("bobbers"));
    public static final TagKey<Item> BAITS = ItemTags.create(Starcatcher.rl("baits"));
    public static final TagKey<Item> TEMPLATES = ItemTags.create(Starcatcher.rl("templates"));
    public static final TagKey<Item> EQUIPMENTS = ItemTags.create(Starcatcher.rl("equipments"));
    public static final TagKey<Item> GADGETS = ItemTags.create(Starcatcher.rl("gadgets"));

    public static final TagKey<Item> RODS = ItemTags.create(Starcatcher.rl("rods"));

    public static final TagKey<Item> BUCKETABLE_FISHES = ItemTags.create(Starcatcher.rl("bucketable_fishes"));
    public static final TagKey<Item> STARCAUGHT_FISHES = ItemTags.create(Starcatcher.rl("starcaught_fishes"));

    public static final TagKey<Item> COMMON_FISHES = ItemTags.create(Starcatcher.rl("common_fishes"));
    public static final TagKey<Item> UNCOMMON_FISHES = ItemTags.create(Starcatcher.rl("uncommon_fishes"));
    public static final TagKey<Item> RARE_FISHES = ItemTags.create(Starcatcher.rl("rare_fishes"));
    public static final TagKey<Item> EPIC_FISHES = ItemTags.create(Starcatcher.rl("epic_fishes"));
    public static final TagKey<Item> LEGENDARY_FISHES = ItemTags.create(Starcatcher.rl("legendary_fishes"));

    public static final TagKey<Item> HOOK_SURVIVES_LAVA = ItemTags.create(Starcatcher.rl("hook_survives_lava"));
}
