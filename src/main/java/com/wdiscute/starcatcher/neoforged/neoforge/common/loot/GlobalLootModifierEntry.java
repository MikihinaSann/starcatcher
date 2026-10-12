package com.wdiscute.starcatcher.neoforged.neoforge.common.loot;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Loot-pool entry that evaluates a {@link LootModifier}'s conditions and emits
 * its generated stacks, injected into every builtin table by
 * {@link GlobalLootModifierManager}.
 */
public class GlobalLootModifierEntry extends LootPoolSingletonContainer
{
    private final LootModifier modifier;

    protected GlobalLootModifierEntry(LootModifier modifier)
    {
        super(1, 0, java.util.List.of(modifier.conditions()), java.util.List.of());
        this.modifier = modifier;
    }

    public static GlobalLootModifierEntry of(LootModifier modifier)
    {
        return new GlobalLootModifierEntry(modifier);
    }

    /**
     * Wraps the entry in the builder form expected by {@code LootPool.Builder#add}.
     */
    public static LootPoolEntryContainer.Builder<?> builder(LootModifier modifier)
    {
        return new GLMBuilder(modifier);
    }

    private static class GLMBuilder extends LootPoolSingletonContainer.Builder<GLMBuilder>
    {
        private final LootModifier modifier;

        GLMBuilder(LootModifier modifier)
        {
            this.modifier = modifier;
        }

        @Override
        protected GLMBuilder getThis()
        {
            return this;
        }

        @Override
        public LootPoolEntryContainer build()
        {
            return GlobalLootModifierEntry.of(modifier);
        }
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext)
    {
        if (!Arrays.stream(modifier.conditions()).allMatch(c -> c.test(lootContext))) return;
        ObjectArrayList<ItemStack> out = modifier.apply(new ObjectArrayList<>(), lootContext);
        out.forEach(stackConsumer);
    }

    @Override
    public LootPoolEntryType getType()
    {
        return GlobalLootModifierManager.GLM_ENTRY_TYPE;
    }

    public static final MapCodec<GlobalLootModifierEntry> CODEC = MapCodec.unit(() -> null);
}
