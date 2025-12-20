package com.wdiscute.starcatcher.items.cheater;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.AbstractCatchModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CatchModifierItem extends Item
{
    @SafeVarargs
    public CatchModifierItem(Pair<ResourceLocation, Supplier<AbstractCatchModifier>>... modifiers)
    {
        this(1, modifiers);
    }

    @SafeVarargs
    public CatchModifierItem(int maxStackSize, Pair<ResourceLocation, Supplier<AbstractCatchModifier>>... modifiers)
    {
        super(new Item.Properties()
                .component(ModDataComponents.CATCH_MODIFIERS, getAsList(modifiers))
                .stacksTo(maxStackSize)
        );
    }

    @SafeVarargs
    static List<ResourceLocation> getAsList(Pair<ResourceLocation, Supplier<AbstractCatchModifier>>... modifiers)
    {
        List<ResourceLocation> list = new ArrayList<>();
        for (Pair<ResourceLocation, Supplier<AbstractCatchModifier>> p : modifiers)
        {
            list.add(p.getFirst());
        }
        return list;
    }

}
