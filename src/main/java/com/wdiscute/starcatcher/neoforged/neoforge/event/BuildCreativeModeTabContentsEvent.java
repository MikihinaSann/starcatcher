package com.wdiscute.starcatcher.neoforged.neoforge.event;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

/**
 * Fabric port shim of NeoForge's BuildCreativeModeTabContentsEvent. Backed by
 * Fabric's {@code ItemGroupEvents#modifyEntriesEvent}.
 */
public class BuildCreativeModeTabContentsEvent extends Event
{
    private final ResourceKey<CreativeModeTab> tabKey;
    private final CreativeModeTab tab;
    private final net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries entries;

    public BuildCreativeModeTabContentsEvent(ResourceKey<CreativeModeTab> tabKey,
                                             net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries entries)
    {
        this.tabKey = tabKey;
        this.tab = null;
        this.entries = entries;
    }

    public BuildCreativeModeTabContentsEvent(CreativeModeTab tab,
                                             net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries entries)
    {
        this.tab = tab;
        this.tabKey = net.minecraft.core.registries.BuiltInRegistries.CREATIVE_MODE_TAB
                .getResourceKey(tab).orElse(null);
        this.entries = entries;
    }

    public ResourceKey<CreativeModeTab> getTabKey()
    {
        return tabKey;
    }

    public CreativeModeTab getTab()
    {
        return tab != null ? tab
                : net.minecraft.core.registries.BuiltInRegistries.CREATIVE_MODE_TAB.get(tabKey);
    }

    public void accept(ItemLike itemLike)
    {
        entries.accept(itemLike);
    }

    public void accept(Holder<ItemLike> holder)
    {
        entries.accept(holder.value());
    }

    public void accept(com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredHolder<ItemLike, ?> holder)
    {
        entries.accept(holder.value());
    }

    public void accept(ItemStack stack)
    {
        entries.accept(stack);
    }

    public void accept(com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredBlock<?> block)
    {
        entries.accept(block.asItem());
    }

    public void insertAfter(ItemStack pivot, ItemStack stack, CreativeModeTab.TabVisibility visibility)
    {
        entries.addAfter(pivot, java.util.List.of(stack), visibility);
    }

    public void insertAfter(ItemLike pivot, ItemStack stack, CreativeModeTab.TabVisibility visibility)
    {
        entries.addAfter(pivot, java.util.List.of(stack), visibility);
    }

    public FeatureFlagSet getFlags()
    {
        return entries.getEnabledFeatures();
    }

    public boolean hasPermissions()
    {
        return true;
    }

    public net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries getEntries()
    {
        return entries;
    }
}
