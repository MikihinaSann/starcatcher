package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.loot.AddItemModifier;
import com.wdiscute.starcatcher.neoforged.bus.api.IEventBus;
import com.wdiscute.starcatcher.neoforged.neoforge.common.loot.IGlobalLootModifier;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredRegister;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public interface SCLootModifiers
{
    DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Starcatcher.MOD_ID);

    Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> AddItemModifier.CODEC);

    static void register(IEventBus eventBus)
    {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
