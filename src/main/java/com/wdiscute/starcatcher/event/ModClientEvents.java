package com.wdiscute.starcatcher.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bob.FishingBobModel;
import com.wdiscute.starcatcher.bob.FishingBobRenderer;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.fishspotter.FishRadarLayer;
import com.wdiscute.starcatcher.fishspotter.LayeredDraw;
import com.wdiscute.starcatcher.items.BucketTooltipRenderer;
import com.wdiscute.starcatcher.items.StarcaughtBucket;
import com.wdiscute.starcatcher.particles.FishingBitingLavaParticles;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.registry.custom.tackleskin.*;
import com.wdiscute.starcatcher.rod.FishingRodScreen;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT, bus =  Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents
{

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event)
    {
        List<Component> comp = event.getToolTip();
        ItemStack stack = event.getItemStack();

        if (ModDataComponents.has(stack, ModDataComponents.MINIGAME_MODIFIERS) || ModDataComponents.has(stack, ModDataComponents.CATCH_MODIFIERS))
        {
            List<ResourceLocation> modifiers = new ArrayList<>();

            if (ModDataComponents.has(stack, ModDataComponents.CATCH_MODIFIERS))
                modifiers.addAll(Objects.requireNonNull(ModDataComponents.get(stack, ModDataComponents.CATCH_MODIFIERS)));
            if (ModDataComponents.has(stack, ModDataComponents.MINIGAME_MODIFIERS))
                modifiers.addAll(Objects.requireNonNull(ModDataComponents.get(stack, ModDataComponents.MINIGAME_MODIFIERS)));

            if (!modifiers.isEmpty())
            {
                comp.add(Component.translatable("tooltip.starcatcher.modifiers").withStyle(ChatFormatting.GRAY));

                for (ResourceLocation rl : modifiers)
                {
                    for (int i = 0; i < 100; i++)
                    {
                        if (I18n.exists("tooltip.modifier." + rl.toLanguageKey() + "." + i))
                        {
                            MutableComponent start = i == 0 ? Component.literal("- ") : Component.literal("");
                            comp.add(start.append(Component.translatable("tooltip.modifier." + rl.toLanguageKey() + "." + i)).withStyle(ChatFormatting.DARK_GRAY));
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }

        //size and weight
        if (ModDataComponents.has(stack, ModDataComponents.SIZE_AND_WEIGHT))
        {
            SizeAndWeightInstance sw = ModDataComponents.get(stack, ModDataComponents.SIZE_AND_WEIGHT);

            SettingsScreen.Units units = Config.UNIT.get();

            String size = units.getSizeAsString(sw.sizeInCentimeters());
            String weight = units.getWeightAsString(sw.weightInGrams());

            comp.add(1, Component.literal(size + " - " + weight).withColor(0x888888));
        }

        //tackle skin
        if (ModDataComponents.has(stack, ModDataComponents.TACKLE_SKIN))
        {
            ResourceLocation rl = ModDataComponents.get(stack, ModDataComponents.TACKLE_SKIN);
            comp.add(Component.translatable("tooltip.starcatcher.tackle").withStyle(ChatFormatting.GRAY));

            for (int i = 0; i < 100; i++)
            {
                if (I18n.exists("tooltip.tackle." + rl.toLanguageKey() + "." + i))
                {
                    MutableComponent start = i == 0 ? Component.literal("- ") : Component.literal("");
                    comp.add(start.append(Component.translatable("tooltip.tackle." + rl.toLanguageKey() + "." + i)).withStyle(ChatFormatting.DARK_GRAY));
                }
                else break;
            }
        }

        //Netherite Upgrade
        if (ModDataComponents.has(stack, ModDataComponents.NETHERITE_UPGRADE))
        {
            if (ModDataComponents.get(stack, ModDataComponents.NETHERITE_UPGRADE))
            {
                comp.add(1, Tooltips.decodeTranslationKey("tooltip.starcatcher.rod.netherite"));
            }
        }

        //rarity name color
        if (ModDataComponents.has(stack, ModDataComponents.FISH_PROPERTIES))
        {
            FishProperties fp = ModDataComponents.get(stack, ModDataComponents.FISH_PROPERTIES);

            String s = fp.rarity().getPre() + comp.get(0).getString(100) + fp.rarity().getPost();

            comp.remove(0);
            comp.add(0, Tooltips.decodeString(s));
        }

        //trophy stuff
        if (ModDataComponents.has(stack, ModDataComponents.TROPHY))
        {
            TrophyProperties tp = ModDataComponents.get(stack, ModDataComponents.TROPHY);

            if (tp.trophyType() == TrophyProperties.TrophyType.TROPHY)
                if (event.getFlags().hasShiftDown())
                {
                    comp.add(Component.translatable("tooltip.libtooltips.generic.shift_down"));
                    comp.add(Component.translatable("tooltip.libtooltips.generic.empty"));
                    comp.add(Component.translatable("tooltip.starcatcher.trophy.0"));
                    comp.add(Component.translatable("tooltip.starcatcher.trophy.1"));

                    List<Component> list = new java.util.ArrayList<>();

                    //all
                    if (tp.all().total() != 0) list.add(Tooltips.decodeString(
                            I18n.get("tooltip.starcatcher.trophy.total")
                                    .replace("&", tp.all().total() + "")
                                    .replace("$", I18n.get("tooltip.starcatcher.trophy.all"))
                    ));

                    if (tp.all().unique() != 0) list.add(
                            Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                    .replace("&", tp.all().unique() + "")
                                    .replace("$", I18n.get("tooltip.starcatcher.trophy.all"))));

                    for (FishProperties.Rarity value : FishProperties.Rarity.values())
                    {
                        TrophyProperties.RarityProgress progress = tp.getProgress(value);
                        if (progress.total() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", progress.total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy." + value.getSerializedName()))));

                        if (progress.unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", progress.unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy." + value.getSerializedName()))));
                    }

                    if (list.size() == 1)
                    {
                        comp.add(Component.translatable("tooltip.starcatcher.trophy.once")
                                .append(list.getFirst())
                                .append(Component.translatable("tooltip.starcatcher.trophy.have_been_caught")));
                    }
                    else
                    {
                        comp.add(Component.translatable("tooltip.starcatcher.trophy.2"));
                        comp.addAll(list);
                    }

                }
                else
                {
                    comp.add(Component.translatable("tooltip.libtooltips.generic.shift_up"));
                }

        }
    }

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
        event.registerLayerDefinition(BaseTackleSkin.LAYER_LOCATION, BaseTackleSkin::createBodyLayer);
        event.registerLayerDefinition(PearlTackleSkin.LAYER_LOCATION, PearlTackleSkin::createBodyLayer);
        event.registerLayerDefinition(KimbeTackleSkin.LAYER_LOCATION, KimbeTackleSkin::createBodyLayer);
        event.registerLayerDefinition(FrogTackleSkin.LAYER_LOCATION, FrogTackleSkin::createBodyLayer);
        event.registerLayerDefinition(ColorfulTackleSkin.LAYER_LOCATION, ColorfulTackleSkin::createBodyLayer);
        event.registerLayerDefinition(ClearTackleSkin.LAYER_LOCATION, ClearTackleSkin::createBodyLayer);

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
