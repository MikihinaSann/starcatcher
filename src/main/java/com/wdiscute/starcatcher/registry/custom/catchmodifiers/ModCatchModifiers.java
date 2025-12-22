package com.wdiscute.starcatcher.registry.custom.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.io.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface ModCatchModifiers
{
    DeferredRegister<Supplier<AbstractCatchModifier>> REGISTRY =
            DeferredRegister.create(Starcatcher.CATCH_MODIFIERS, Starcatcher.MOD_ID);

    //todo built-in modifiers to skip minigame for low rarity or something, using AbstractCatchModifier#forceSkipMinigame

    //every bait
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> DECREASES_LURE_TIME = registerCatchModifier(
            "decrease_lure_time",
            () -> new DecreaseLureTimeModifier(20, 100, 80));

    //every bait
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> BIG_DECREASES_LURE_TIME = registerCatchModifier(
            "big_decrease_lure_time",
            () -> new DecreaseLureTimeModifier(50, 170, 80));

    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> INCREASE_DECREASES_LURE_TIME = registerCatchModifier(
            "increase_lure_time",
            () -> new DecreaseLureTimeModifier(-20, -100, -80));

    //vanilla bobber
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> VANILLA_LOOT = registerCatchModifier("vanilla_loot", VanillaLootModifier::new);

    //almighty worm
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> FISH_ENTITY = registerCatchModifier("fish_entity", ForceFishEntityModifier::new);

    //seeking worm
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> GUARANTEE_NEW_FISH_ALWAYS = registerCatchModifier("guarantee_new_fish_always", () -> new GuaranteeNewFishModifier(101));
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> GUARANTEE_NEW_FISH_HALF = registerCatchModifier("guarantee_new_fish_half", () -> new GuaranteeNewFishModifier(50));

    //gold hook
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_EXP_BASED_ON_PERFORMANCE = registerCatchModifier("extra_exp_based_on_performance", ExtraExpBasedOnPerformanceModifier::new);

    //split hook
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_ITEM = registerCatchModifier("extra_item", () -> new ExtraItemsModifier(1));
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_TWO_ITEMS = registerCatchModifier("extra_two_item", () -> new ExtraItemsModifier(2));
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_THREE_ITEMS = registerCatchModifier("extra_three_item", () -> new ExtraItemsModifier(3));
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_FIVE_ITEMS = registerCatchModifier("extra_five_item", () -> new ExtraItemsModifier(5));
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_TEN_ITEMS = registerCatchModifier("extra_ten_item", () -> new ExtraItemsModifier(10));
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> EXTRA_THIRTY_TWO_ITEMS = registerCatchModifier("extra_thirty_two_item", () -> new ExtraItemsModifier(32));

    //meteorological bait
    Pair<ResourceLocation, Supplier<AbstractCatchModifier>> IGNORE_DAYTIME_AND_WEATHER_RESTRICTIONS = registerCatchModifier("ignore_daytime_and_weather_restrictions", IgnoreDaytimeWeatherRestrictions::new);


    static Pair<ResourceLocation, Supplier<AbstractCatchModifier>> registerCatchModifier(String name, Supplier<AbstractCatchModifier> sup)
    {
        REGISTRY.register(name, () -> sup);
        return Pair.of(Starcatcher.rl(name), sup);
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }


    static List<AbstractCatchModifier> getAllCatchModifiers(Level level, ItemStack is)
    {
        return getAllCatchModifiers(level, is, true);
    }

    static List<AbstractCatchModifier> getAllCatchModifiers(Level level, ItemStack is, boolean checkRodItemStack)
    {
        List<AbstractCatchModifier> modifiers = new ArrayList<>();

        if (is.is(StarcatcherTags.RODS) && checkRodItemStack)
        {
            modifiers.addAll(getAllCatchModifiers(level, ModDataComponents.get(is, ModDataComponents.BOBBER).stack(), false));
            modifiers.addAll(getAllCatchModifiers(level, ModDataComponents.get(is, ModDataComponents.BAIT).stack(), false));
            modifiers.addAll(getAllCatchModifiers(level, ModDataComponents.get(is, ModDataComponents.HOOK).stack(), false));
        }

        if (ModDataComponents.has(is, ModDataComponents.CATCH_MODIFIERS))
        {
            for (ResourceLocation rl : Objects.requireNonNull(ModDataComponents.get(is, ModDataComponents.CATCH_MODIFIERS)))
            {
                Supplier<AbstractCatchModifier> mod = Starcatcher.getRegistry(Starcatcher.CATCH_MODIFIERS).getValue(rl);

                if (mod != null)
                    modifiers.add(mod.get());
            }
        }
        return modifiers;
    }

    static boolean hasModifier(ItemStack is, ResourceLocation rl)
    {
        return hasModifier(is, rl, true);
    }

    static boolean hasModifier(ItemStack is, ResourceLocation rl, boolean checkRodItemStack)
    {

        if (is.is(StarcatcherTags.RODS) && checkRodItemStack)
        {
            return (
                    hasModifier(ModDataComponents.get(is, ModDataComponents.BOBBER).stack(), rl) ||
                            hasModifier(ModDataComponents.get(is, ModDataComponents.BAIT).stack(), rl) ||
                            hasModifier(ModDataComponents.get(is, ModDataComponents.HOOK).stack(), rl) ||
                            hasModifier(is, rl, false)
            );
        }

        if (ModDataComponents.has(is, ModDataComponents.CATCH_MODIFIERS))
        {
            return ModDataComponents.get(is, ModDataComponents.CATCH_MODIFIERS).contains(rl);
        }
        else
        {
            return false;
        }
    }
}

