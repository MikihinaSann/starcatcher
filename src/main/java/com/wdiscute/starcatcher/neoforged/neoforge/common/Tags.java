package com.wdiscute.starcatcher.neoforged.neoforge.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

/**
 * Fabric port shim of NeoForge's {@code Tags}. Conventional NeoForge tags map
 * to Fabric's {@code c:} namespace equivalents.
 */
public class Tags
{
    private static TagKey<Item> itemTag(String name)
    {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Block> blockTag(String name)
    {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Biome> biomeTag(String name)
    {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static class Items
    {
        public static final TagKey<Item> BUCKETS_EMPTY = itemTag("buckets/empty");
        public static final TagKey<Item> DYES = itemTag("dyes");
        public static final TagKey<Item> FOODS = itemTag("foods");
        public static final TagKey<Item> FOODS_RAW_FISH = itemTag("foods/raw_fish");
        public static final TagKey<Item> GLASS_PANES = itemTag("glass_panes");
        public static final TagKey<Item> RODS = itemTag("rods");
        public static final TagKey<Item> TOOLS = itemTag("tools");
        public static final TagKey<Item> TOOLS_FISHING_ROD = itemTag("tools/fishing_rod");
        public static final TagKey<Item> STORAGE_BLOCKS = itemTag("storage_blocks");
    }

    public static class Blocks
    {
        public static final TagKey<Block> VILLAGER_FARMLANDS = blockTag("villager_farmlands");
        public static final TagKey<Block> STORAGE_BLOCKS = blockTag("storage_blocks");
    }

    public static class Biomes
    {
        public static final TagKey<Biome> IS_BADLANDS = biomeTag("is_badlands");
        public static final TagKey<Biome> IS_BEACH = biomeTag("is_beach");
        public static final TagKey<Biome> IS_BIRCH_FOREST = biomeTag("is_birch_forest");
        public static final TagKey<Biome> IS_DEEP_OCEAN = biomeTag("is_deep_ocean");
        public static final TagKey<Biome> IS_DESERT = biomeTag("is_desert");
        public static final TagKey<Biome> IS_MUSHROOM = biomeTag("is_mushroom");
        public static final TagKey<Biome> IS_SWAMP = biomeTag("is_swamp");
        public static final TagKey<Biome> IS_WET = biomeTag("is_wet");
        public static final TagKey<Biome> IS_DRY = biomeTag("is_dry");
        public static final TagKey<Biome> IS_COLD = biomeTag("is_cold");
        public static final TagKey<Biome> IS_HOT = biomeTag("is_hot");
        public static final TagKey<Biome> IS_SNOWY = biomeTag("is_snowy");
        public static final TagKey<Biome> IS_PLAINS = biomeTag("is_plains");
        public static final TagKey<Biome> IS_FOREST = biomeTag("is_forest");
        public static final TagKey<Biome> IS_OCEAN = biomeTag("is_ocean");
        public static final TagKey<Biome> IS_RIVER = biomeTag("is_river");
        public static final TagKey<Biome> IS_JUNGLE = biomeTag("is_jungle");
        public static final TagKey<Biome> IS_MOUNTAIN = biomeTag("is_mountain");
        public static final TagKey<Biome> IS_NETHER = biomeTag("is_nether");
        public static final TagKey<Biome> IS_END = biomeTag("is_end");
        public static final TagKey<Biome> IS_OVERWORLD = biomeTag("is_overworld");
        public static final TagKey<Biome> IS_VOID = biomeTag("is_void");
        public static final TagKey<Biome> IS_UNDERGROUND = biomeTag("is_underground");
        public static final TagKey<Biome> IS_CAVE = biomeTag("is_cave");
        public static final TagKey<Biome> IS_AQUATIC = biomeTag("is_aquatic");
        public static final TagKey<Biome> IS_ICY = biomeTag("is_icy");
        public static final TagKey<Biome> IS_SANDY = biomeTag("is_sandy");
        public static final TagKey<Biome> IS_WASTELAND = biomeTag("is_wasteland");
        public static final TagKey<Biome> IS_DEAD = biomeTag("is_dead");
        public static final TagKey<Biome> IS_LUSH = biomeTag("is_lush");
        public static final TagKey<Biome> IS_SPOOKY = biomeTag("is_spooky");
        public static final TagKey<Biome> IS_MAGICAL = biomeTag("is_magical");
        public static final TagKey<Biome> IS_RARE = biomeTag("is_rare");
        public static final TagKey<Biome> IS_PLATEAU = biomeTag("is_plateau");
        public static final TagKey<Biome> IS_MODIFIED = biomeTag("is_modified");
        public static final TagKey<Biome> IS_FLORAL = biomeTag("is_floral");
        public static final TagKey<Biome> IS_CONIFEROUS = biomeTag("is_coniferous");
        public static final TagKey<Biome> IS_OUTER_END_ISLAND = biomeTag("is_outer_end_island");
        public static final TagKey<Biome> NO_DEFAULT_MONSTERS = biomeTag("no_default_monsters");
        public static final TagKey<Biome> HIDDEN_FROM_LOCATOR_SELECTION = biomeTag("hidden_from_locator_selection");
        public static final TagKey<Biome> IS_CHERRY_GROVE = biomeTag("is_cherry_grove");
        public static final TagKey<Biome> IS_PALE_GARDEN = biomeTag("is_pale_garden");
        public static final TagKey<Biome> IS_OLD_GROWTH = biomeTag("is_old_growth");
        public static final TagKey<Biome> IS_STONY_SHORES = biomeTag("is_stony_shores");
        public static final TagKey<Biome> IS_CLIMATE_TEMPERATE = biomeTag("is_climate/temperate");
    }
}
