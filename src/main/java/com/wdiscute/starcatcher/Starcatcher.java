package com.wdiscute.starcatcher;

import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.registry.blocks.ModBlockEntities;
import com.wdiscute.starcatcher.registry.blocks.ModBlocks;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.ModCatchModifiers;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.ModMinigameModifiers;
import com.wdiscute.starcatcher.registry.custom.sweetspotbehaviour.AbstractSweetSpotBehaviour;
import com.wdiscute.starcatcher.registry.custom.sweetspotbehaviour.ModSweetSpotsBehaviour;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.slf4j.Logger;

import java.util.Random;
import java.util.function.Supplier;

@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<TrophyProperties>> TROPHY_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("trophy"));

    public static final ResourceKey<Registry<Supplier<? extends AbstractSweetSpotBehaviour>>> SWEET_SPOT_BEHAVIOUR =
            ResourceKey.createRegistryKey(Starcatcher.rl("sweet_spot_behaviour"));

    public static final ResourceKey<Registry<Supplier<AbstractMinigameModifier>>> MINIGAME_MODIFIERS =
            ResourceKey.createRegistryKey(Starcatcher.rl("minigame_modifiers"));

    public static final ResourceKey<Registry<Supplier<AbstractCatchModifier>>> CATCH_MODIFIERS =
            ResourceKey.createRegistryKey(Starcatcher.rl("catch_modifiers"));

    public static final Random r = new Random();

    public static double truncatedNormal(double mean, double deviation)
    {
        while (true)
        {
            double value = mean + deviation * r.nextGaussian();
            if (value >= mean - deviation && value <= mean + deviation)
            {
                return value;
            }
        }
    }

    public static ResourceLocation rl(String s)
    {
        return new ResourceLocation(Starcatcher.MOD_ID, s);
    }

    @OnlyIn(Dist.CLIENT)
    public static void fishCaughtToast(FishProperties fp, boolean newFish, int sizeCM, int weightCM)
    {
        if (newFish) Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(fp));

        SettingsScreen.Units units = Config.UNIT.get();

        String size = units.getSizeAsString(sizeCM);
        String weight = units.getWeightAsString(weightCM);

        Minecraft.getInstance().player.displayClientMessage(
                Component.literal("")
                        .append(Component.translatable(fp.catchInfo().fish().get().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight))
                , true);

        Minecraft.getInstance().gui.overlayMessageTime = 180;
    }


    public Starcatcher()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC_SERVER);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.ITEMS_REGISTRY.register(modEventBus);

        ModItems.BAITS_REGISTRY.register(modEventBus);
        ModItems.HOOKS_REGISTRY.register(modEventBus);
        ModItems.BOBBERS_REGISTRY.register(modEventBus);

        ModItems.FISH_REGISTRY.register(modEventBus);
        ModItems.TRASH_REGISTRY.register(modEventBus);

        ModItems.BLOCKITEMS_REGISTRY.register(modEventBus);
        ModItems.RODS_REGISTRY.register(modEventBus);
        ModItems.TEMPLATES_REGISTRY.register(modEventBus);

        ModItems.DEV_REGISTRY.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModSweetSpotsBehaviour.register(modEventBus);
        ModMinigameModifiers.register(modEventBus);
        ModCatchModifiers.register(modEventBus);
        ModCriterionTriggers.register(modEventBus);

        ModNetworking.init();
    }
}
