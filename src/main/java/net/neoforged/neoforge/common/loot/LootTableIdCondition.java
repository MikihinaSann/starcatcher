package net.neoforged.neoforge.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Fabric port shim of NeoForge's loot_table_id loot condition.
 */
public record LootTableIdCondition(ResourceLocation targetLootTableId) implements LootItemCondition
{
    public static final MapCodec<LootTableIdCondition> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(ResourceLocation.CODEC.fieldOf("loot_table_id").forGetter(LootTableIdCondition::targetLootTableId))
                    .apply(inst, LootTableIdCondition::new));

    public static final LootItemConditionType TYPE = new LootItemConditionType(CODEC);

    @Override
    public LootItemConditionType getType()
    {
        return TYPE;
    }

    @Override
    public boolean test(LootContext lootContext)
    {
        // fabric port: vanilla LootContext has no queried table id (NeoForge patch);
        // GlobalLootModifierManager pre-filters by table id at injection time
        return true;
    }

    public ResourceLocation getTargetLootTableId()
    {
        return targetLootTableId;
    }

    public static class Builder implements LootItemCondition.Builder
    {
        private final ResourceLocation targetLootTableId;

        public Builder(ResourceLocation targetLootTableId)
        {
            this.targetLootTableId = targetLootTableId;
        }

        @Override
        public LootItemCondition build()
        {
            return new LootTableIdCondition(targetLootTableId);
        }
    }
}
