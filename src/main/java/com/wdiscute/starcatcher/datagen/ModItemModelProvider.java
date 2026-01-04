package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Starcatcher.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> item : ModItems.ITEMS_REGISTRY.getEntries()) {
            simpleItem(item);
        }

        for (RegistryObject<Item> item : ModItems.HOOKS_REGISTRY.getEntries()) {
            simpleItem( item);
        }

        for (RegistryObject<Item> item : ModItems.BAITS_REGISTRY.getEntries()) {
            simpleItem( item);
        }

        for (RegistryObject<Item> item : ModItems.BOBBERS_REGISTRY.getEntries()) {
            simpleItem( item);
        }

        for (RegistryObject<Item> item : ModItems.TEMPLATES_REGISTRY.getEntries()) {
            simpleItem( item);
        }

        for (RegistryObject<Item> item : ModItems.DEV_REGISTRY.getEntries()) {
            simpleItem( item);
        }

        for (RegistryObject<Item> item : ModItems.FISH_REGISTRY.getEntries()) {
            simpleItem( item);
        }

        for (RegistryObject<Item> item : ModItems.KINDA_BUT_NOT_REALLY_FISH_REGISTRY.getEntries()) {
            simpleItem(item);
        }

        for (RegistryObject<Item> item : ModItems.TRASH_REGISTRY.getEntries()) {
            simpleItem( item);
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                mcLoc("item/generated")).texture("layer0",
                modLoc("item/" + item.getId().getPath()));
    }
}
