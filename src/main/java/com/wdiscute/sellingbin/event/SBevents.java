package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.bin.SellingBinBlockEntity;
import com.wdiscute.sellingbin.registry.SBBlockEntities;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.wdiscute.starcatcher.neoforged.bus.api.SubscribeEvent;
import com.wdiscute.starcatcher.neoforged.fml.common.EventBusSubscriber;
import com.wdiscute.starcatcher.neoforged.neoforge.capabilities.Capabilities;
import com.wdiscute.starcatcher.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.AddPackFindersEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.NewRegistryEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = SellingBin.MOD_ID)
public class SBevents
{
    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        event.register(SellingBin.SELLING_BIN_REGISTRY);
    }

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
        {
            event.accept(SBBlocks.SELLING_BIN);
        }
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
        {
            event.accept(SBBlocks.SELLING_BIN);
        }
    }

    @SubscribeEvent
    public static void addCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SBBlockEntities.SELLING_BIN.get(),
                (container, side) ->
                {
                    if(container instanceof SellingBinBlockEntity binBlockEntity)
                    {
                        if(binBlockEntity.isCenter())
                        {
                            return new SidedInvWrapper(container, side);
                        }
                        else
                        {
                            BlockEntity blockEntity = binBlockEntity.getLevel().getBlockEntity(binBlockEntity.getCenter());
                            if(blockEntity instanceof SellingBinBlockEntity center)
                            {
                                return new SidedInvWrapper(center, side);
                            }
                        }
                    }
                    return null;
                }
        );
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event)
    {
        PackSource packSource = new DefaultPackSource();

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_currency_emeralds"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Emeralds"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_sellable_foods"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Foods"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_sellable_enchanted_books"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Enchanted Books"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_quality_foods_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Quality Food"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_vinery_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Vinery Wine"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_brewery_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Brewery Beer"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_currency_numismatics_coins"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Numismatics"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_currency_lightmanscurrency_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Lightmans's Currency"),
                packSource,
                false,
                Pack.Position.TOP
        );
    }


    @SubscribeEvent
    public static void registerAttributed(RegisterDataMapTypesEvent event)
    {
        event.register(SBDataMaps.SELLING_BIN_VALUE);
        event.register(SBDataMaps.SELLING_BIN_CURRENCIES);
    }

    public static class DefaultPackSource implements PackSource
    {
        @Override
        public Component decorate(Component name)
        {
            return name;
        }

        @Override
        public boolean shouldAddAutomatically()
        {
            return false;
        }
    }

}
