package net.neoforged.neoforge.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * Fabric port shim of NeoForge's LootModifier base class.
 */
public abstract class LootModifier implements IGlobalLootModifier
{
    protected final LootItemCondition[] conditions;

    protected LootModifier(LootItemCondition[] conditionsIn)
    {
        this.conditions = conditionsIn;
    }

    protected abstract ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext);

    @Override
    public final ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        return doApply(generatedLoot, context);
    }

    @Override
    public LootItemCondition[] conditions()
    {
        return conditions;
    }

    public static <T extends LootModifier> com.mojang.datafixers.Products.P1<RecordCodecBuilder.Mu<T>, LootItemCondition[]> codecStart(RecordCodecBuilder.Instance<T> instance)
    {
        return instance.group(LootItemCondition.DIRECT_CODEC.listOf()
                .<LootItemCondition[]>xmap(l -> l.toArray(new LootItemCondition[0]), java.util.Arrays::asList)
                .fieldOf("conditions").forGetter(m -> m.conditions));
    }
}
