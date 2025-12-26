package com.wdiscute.starcatcher.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.Tooltips;
import com.wdiscute.starcatcher.bob.FishingBobModel;
import com.wdiscute.starcatcher.bob.FishingBobRenderer;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.fishspotter.FishRadarLayer;
import com.wdiscute.starcatcher.fishspotter.LayeredDraw;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SizeAndWeightInstance;
import com.wdiscute.starcatcher.items.BucketTooltipRenderer;
import com.wdiscute.starcatcher.items.StarcaughtBucket;
import com.wdiscute.starcatcher.particles.FishingBitingLavaParticles;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.registry.custom.tackleskin.*;
import com.wdiscute.starcatcher.rod.FishingRodScreen;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT, bus =  Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents
{

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ModItemProperties.addCustomItemProperties();

        MenuScreens.register(ModMenuTypes.FISHING_ROD_MENU.get(), FishingRodScreen::new);
        MenuScreens.register(ModMenuTypes.STAND_MENU.get(), StandScreen::new);
        registerGuiLayers();
    }


    @SubscribeEvent
    public static void onRegisterEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FISHING_BOB.get(), FishingBobRenderer::new);
        event.registerEntityRenderer(ModEntities.BOTTLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.FISH.get(), FishRenderer::new);
    }


    public static void registerGuiLayers()
    {
        LayeredDraw.add(new FishRadarLayer());
        LayeredDraw.add(new TournamentOverlay());
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ModParticles.FISHING_NOTIFICATION.get(), FishingNotificationParticles.Provider::new);
        event.registerSpriteSet(ModParticles.FISHING_BITING.get(), FishingBitingParticles.Provider::new);
        event.registerSpriteSet(ModParticles.FISHING_BITING_LAVA.get(), FishingBitingLavaParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        //tackle skins
        event.registerLayerDefinition(new BaseTackleSkin().getLayerLocation(), BaseTackleSkin::createBodyLayer);
        event.registerLayerDefinition(new PearlTackleSkin().getLayerLocation(), PearlTackleSkin::createBodyLayer);
        event.registerLayerDefinition(new KimbeTackleSkin().getLayerLocation(), KimbeTackleSkin::createBodyLayer);
        event.registerLayerDefinition(new FrogTackleSkin().getLayerLocation(), FrogTackleSkin::createBodyLayer);
        event.registerLayerDefinition(new ColorfulTackleSkin().getLayerLocation(), ColorfulTackleSkin::createBodyLayer);
        event.registerLayerDefinition(new ClearTackleSkin().getLayerLocation(), ClearTackleSkin::createBodyLayer);

        //fishes
        event.registerLayerDefinition(AgaveBream.LAYER_LOCATION, AgaveBream::createBodyLayer);
        event.registerLayerDefinition(BigeyeTuna.LAYER_LOCATION, BigeyeTuna::createBodyLayer);
        event.registerLayerDefinition(Boreal.LAYER_LOCATION, Boreal::createBodyLayer);
        event.registerLayerDefinition(CactiFish.LAYER_LOCATION, CactiFish::createBodyLayer);
        event.registerLayerDefinition(Charfish.LAYER_LOCATION, Charfish::createBodyLayer);
        event.registerLayerDefinition(CrystalbackBoreal.LAYER_LOCATION, CrystalbackBoreal::createBodyLayer);
        event.registerLayerDefinition(CrystalbackMinnow.LAYER_LOCATION, CrystalbackMinnow::createBodyLayer);
        event.registerLayerDefinition(DeepjawHerring.LAYER_LOCATION, DeepjawHerring::createBodyLayer);
        event.registerLayerDefinition(DownfallBream.LAYER_LOCATION, DownfallBream::createBodyLayer);
        event.registerLayerDefinition(Driftfin.LAYER_LOCATION, Driftfin::createBodyLayer);
        event.registerLayerDefinition(DriftingBream.LAYER_LOCATION, DriftingBream::createBodyLayer);
        event.registerLayerDefinition(DusktailSnapper.LAYER_LOCATION, DusktailSnapper::createBodyLayer);
        event.registerLayerDefinition(LilySnapper.LAYER_LOCATION, LilySnapper::createBodyLayer);
        event.registerLayerDefinition(PinkKoi.LAYER_LOCATION, PinkKoi::createBodyLayer);
        event.registerLayerDefinition(SilverveilPerch.LAYER_LOCATION, SilverveilPerch::createBodyLayer);
        event.registerLayerDefinition(SludgeCatfish.LAYER_LOCATION, SludgeCatfish::createBodyLayer);
        event.registerLayerDefinition(Whiteveil.LAYER_LOCATION, Whiteveil::createBodyLayer);
        event.registerLayerDefinition(WillowBream.LAYER_LOCATION, WillowBream::createBodyLayer);
        event.registerLayerDefinition(WinteryPike.LAYER_LOCATION, WinteryPike::createBodyLayer);
        event.registerLayerDefinition(CrystalbackTrout.LAYER_LOCATION, CrystalbackTrout::createBodyLayer);
        event.registerLayerDefinition(Embergill.LAYER_LOCATION, Embergill::createBodyLayer);
        event.registerLayerDefinition(FrostgillChub.LAYER_LOCATION, FrostgillChub::createBodyLayer);
        event.registerLayerDefinition(FrostjawTrout.LAYER_LOCATION, FrostjawTrout::createBodyLayer);
        event.registerLayerDefinition(HollowbellyDarter.LAYER_LOCATION, HollowbellyDarter::createBodyLayer);
        event.registerLayerDefinition(IcetoothSturgeon.LAYER_LOCATION, IcetoothSturgeon::createBodyLayer);
        event.registerLayerDefinition(MistbackChub.LAYER_LOCATION, MistbackChub::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(ModKeymappings.MINIGAME_HIT);
    }

    @SubscribeEvent
    public static void onRegisterTooltips(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(StarcaughtBucket.BucketTooltip.class, BucketTooltipRenderer::new);
    }

}
