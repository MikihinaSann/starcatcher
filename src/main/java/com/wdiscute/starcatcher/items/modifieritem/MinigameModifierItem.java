package com.wdiscute.starcatcher.items.modifieritem;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.AbstractMinigameModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MinigameModifierItem extends Item
{
    @SafeVarargs
    public MinigameModifierItem(Pair<ResourceLocation, Supplier<AbstractMinigameModifier>>... modifiers)
    {
        this(1, modifiers);
    }

    @SafeVarargs
    public MinigameModifierItem(int maxStackSize, Pair<ResourceLocation, Supplier<AbstractMinigameModifier>>... modifiers)
    {
        super(new Item.Properties().stacksTo(maxStackSize));
        ModDataComponents.registerDefault(this, ModDataComponents.MINIGAME_MODIFIERS, getAsList(modifiers));
    }

    @SafeVarargs
    static List<ResourceLocation> getAsList(Pair<ResourceLocation, Supplier<AbstractMinigameModifier>>... modifiers)
    {
        List<ResourceLocation> list = new ArrayList<>();
        for (Pair<ResourceLocation, Supplier<AbstractMinigameModifier>> p : modifiers)
        {
            list.add(p.getFirst());
        }
        return list;
    }

}
