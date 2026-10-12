package com.wdiscute.sellingbin;

import com.mojang.logging.LogUtils;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.processors.SBProcessors;
import com.wdiscute.sellingbin.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import com.wdiscute.starcatcher.neoforged.api.distmarker.Dist;
import com.wdiscute.starcatcher.neoforged.bus.api.IEventBus;
import com.wdiscute.starcatcher.neoforged.fml.ModContainer;
import com.wdiscute.starcatcher.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import java.util.Random;

@Mod(SellingBin.MOD_ID)
public class SellingBin
{
    public static final String MOD_ID = "selling_bin";
    public static final Random r = new Random();
    public static final Logger LOGGER = LogUtils.getLogger();

    //resource keys
    public static final ResourceKey<Registry<AbstractProcessor>> SELLING_BIN =
            ResourceKey.createRegistryKey(SellingBin.rl("selling_bin"));

    //registry
    public static final Registry<AbstractProcessor> SELLING_BIN_REGISTRY = new RegistryBuilder<>(SELLING_BIN)
            .sync(true)
            .create();

    public static ResourceLocation rl(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(SellingBin.MOD_ID, s);
    }

    public static ResourceLocation rl(String ns, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }

    public SellingBin(IEventBus modEventBus, ModContainer modContainer)
    {
        SBBlocks.register(modEventBus);
        SBBlockEntities.register(modEventBus);
        SBMenuTypes.register(modEventBus);
        SBProcessors.register(modEventBus);
        SBItemPredicate.register(modEventBus);
        SBDataComponents.register(modEventBus);
        SBCreativeModeTab.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, SBConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, SBConfig.SPEC_SERVER);
    }

    @Mod(value = SellingBin.MOD_ID, dist = Dist.CLIENT)
    public static class Client
    {
        public Client(ModContainer modContainer)
        {
        }
    }
}
