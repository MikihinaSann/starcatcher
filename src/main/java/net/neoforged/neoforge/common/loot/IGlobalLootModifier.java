package net.neoforged.neoforge.common.loot;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * Fabric port shim of NeoForge's IGlobalLootModifier.
 */
public interface IGlobalLootModifier
{
    ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext);

    MapCodec<? extends IGlobalLootModifier> codec();

    default LootItemCondition[] conditions()
    {
        return new LootItemCondition[0];
    }
}
