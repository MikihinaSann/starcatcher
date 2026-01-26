package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.fishing.DGStarcatcherFishes;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DGModItemsTagProvider extends ItemTagsProvider
{

    public DGModItemsTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTags, Starcatcher.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        //fishes, cat_food, foods/raw_fish
        for (var item : ModItems.FISH_REGISTRY.getEntries())
        {
            tag(ItemTags.FISHES).add(item.get());
            tag(TagKey.create(Registries.ITEM, new ResourceLocation("forge", "foods/raw_fish"))).add(item.get());
            tag(StarcatcherTags.BUCKETABLE_FISHES).add(item.get());
            tag(StarcatcherTags.STARCAUGHT_FISHES).add(item.get());
        }

        //todo figure out what to do with crabs/eels tags?

        for (FishProperties fp : DGStarcatcherFishes.STARCATCHER_FISHES)
        {
            switch (fp.rarity())
            {
                case COMMON -> tag(StarcatcherTags.COMMON_FISHES).add(fp.catchInfo().fish().value());
                case UNCOMMON -> tag(StarcatcherTags.UNCOMMON_FISHES).add(fp.catchInfo().fish().value());
                case RARE -> tag(StarcatcherTags.RARE_FISHES).add(fp.catchInfo().fish().value());
                case EPIC -> tag(StarcatcherTags.EPIC_FISHES).add(fp.catchInfo().fish().value());
                case LEGENDARY -> tag(StarcatcherTags.LEGENDARY_FISHES).add(fp.catchInfo().fish().value());
            }
        }


        //baits tag
        for (RegistryObject<Item> item : ModItems.BAITS_REGISTRY.getEntries())
        {
            tag(StarcatcherTags.BAITS).add(item.get());
        }

        //templates tag
        for (RegistryObject<Item> item : ModItems.TEMPLATES_REGISTRY.getEntries())
        {
            tag(StarcatcherTags.TEMPLATES).add(item.get());
        }

        //gadgets
        tag(StarcatcherTags.GADGETS).add(ModItems.FISH_RADAR.get());

        //extra baits
        tag(StarcatcherTags.BAITS).add(Items.BUCKET);
        tag(StarcatcherTags.BAITS).add(Items.WITHER_SKELETON_SKULL);

        tag(StarcatcherTags.BAITS).addOptional(rl("fishofthieves", "earthworms"));
        tag(StarcatcherTags.BAITS).addOptional(rl("fishofthieves", "grubs"));
        tag(StarcatcherTags.BAITS).addOptional(rl("fishofthieves", "leeches"));

        tag(StarcatcherTags.BAITS).addOptional(rl("tfc", "food/bluegill"));
        tag(StarcatcherTags.BAITS).addOptional(rl("tfc", "food/cod"));
        tag(StarcatcherTags.BAITS).addOptional(rl("tfc", "food/salmon"));
        tag(StarcatcherTags.BAITS).addOptional(rl("tfc", "food/tropical_fish"));

        //hooks tag
        for (RegistryObject<Item> item : ModItems.HOOKS_REGISTRY.getEntries())
        {
            tag(StarcatcherTags.HOOKS).add(item.get());
        tag(StarcatcherTags.HOOKS).addOptional(rl("tide", "void_fishing_hook"));

        //bobbers tag
        for (RegistryObject<Item> item : ModItems.BOBBERS_REGISTRY.getEntries())
        {
            tag(StarcatcherTags.BOBBERS).add(item.get());
        }

        //rods and tools/fishing_rod
        for (RegistryObject<Item> item : ModItems.RODS_REGISTRY.getEntries())
        {
            tag(Tags.Items.TOOLS_FISHING_RODS).add(item.get());
            tag(StarcatcherTags.RODS).add(item.get());
        }
    }


    public static ResourceLocation rl(String ns, String path)
    {
        return new ResourceLocation(ns, path);
    }
}
