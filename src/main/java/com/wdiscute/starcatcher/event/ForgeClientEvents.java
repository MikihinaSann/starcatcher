package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.Tooltips;
import com.wdiscute.starcatcher.fishspotter.LayeredDraw;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.io.ItemStackDataComponentExtension;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SizeAndWeightInstance;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void renderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        LayeredDraw.renderAll(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event)
    {
        List<Component> comp = event.getToolTip();
        ItemStack stack = event.getItemStack();

        if (ModDataComponents.has(stack,ModDataComponents.MINIGAME_MODIFIERS) || ModDataComponents.has(stack,ModDataComponents.CATCH_MODIFIERS))
        {
            List<ResourceLocation> modifiers = new ArrayList<>();

            if (ModDataComponents.has(stack,ModDataComponents.CATCH_MODIFIERS)) {
                List<ResourceLocation> list = ModDataComponents.get(stack, ModDataComponents.CATCH_MODIFIERS);
                modifiers.addAll(list);
            }
            if (ModDataComponents.has(stack,ModDataComponents.MINIGAME_MODIFIERS)) {
                List<ResourceLocation> list = (ModDataComponents.get(stack, ModDataComponents.MINIGAME_MODIFIERS));
                modifiers.addAll(list);
            }

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
        if (ModDataComponents.has(stack,ModDataComponents.SIZE_AND_WEIGHT))
        {
            SizeAndWeightInstance sw = ModDataComponents.get(stack, ModDataComponents.SIZE_AND_WEIGHT);

            SettingsScreen.Units units = Config.UNIT.get();

            String size = units.getSizeAsString(sw.sizeInCentimeters());
            String weight = units.getWeightAsString(sw.weightInGrams());

            comp.add(1, Component.literal(size + " - " + weight).setStyle(Style.EMPTY.withColor(0x888888)));
        }

        //Cosmetic
        if (ModDataComponents.has(stack,ModDataComponents.BOBBER_SKIN))
        {
            ItemStack copy = ModDataComponents.get(stack, ModDataComponents.BOBBER_SKIN).stack().copy();

            if(!copy.isEmpty())
            {
                comp.add(1, copy.getDisplayName().copy().setStyle(Style.EMPTY.withColor(0x888888)));
                comp.add(1, Tooltips.decodeTranslationKey("tooltip.starcatcher.templates"));
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
        if (ModDataComponents.has(stack,ModDataComponents.FISH_PROPERTIES))
        {
            FishProperties fp = ModDataComponents.get(stack, ModDataComponents.FISH_PROPERTIES);

            String s = fp.rarity().getPre() + comp.get(0).getString(100) + fp.rarity().getPost();

            comp.remove(0);
            comp.add(0, Tooltips.decodeString(s));
        }

        //trophy stuff
        if (ModDataComponents.has(stack,ModDataComponents.TROPHY))
        {
            TrophyProperties tp = stack.get(ModDataComponents.TROPHY);

            if (tp.trophyType() == TrophyProperties.TrophyType.TROPHY)
                if (Minecraft.getInstance().player.isShiftKeyDown())
                {
                    comp.add(Component.translatable("tooltip.libtooltips.generic.shift_down"));
                    comp.add(Component.translatable("tooltip.libtooltips.generic.empty"));
                    comp.add(Component.translatable("tooltip.starcatcher.trophy.0"));
                    comp.add(Component.translatable("tooltip.starcatcher.trophy.1"));

                    List<Component> list = new ArrayList<>();

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
                                .append(list.get(0))
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
}
