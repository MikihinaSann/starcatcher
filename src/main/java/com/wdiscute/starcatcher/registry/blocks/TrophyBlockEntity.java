package com.wdiscute.starcatcher.registry.blocks;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class TrophyBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private TrophyProperties trophyProperties;

    public TrophyBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TROPHY.get(), pPos, pBlockState);
    }

    @Override
    public void saveToItem(ItemStack stack) {
        super.saveToItem(stack);
        ModDataComponents.set(stack, ModDataComponents.TROPHY, this.trophyProperties);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (this.trophyProperties == null) return;

        TrophyProperties.CODEC.encode(this.trophyProperties, NbtOps.INSTANCE, tag)
                .resultOrPartial(LOGGER::warn).ifPresent(tag1 -> tag.put("trophy_properties", tag1));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("trophy_properties")) {
            CompoundTag trophyProperties = tag.getCompound("trophy_properties");
            DataResult<TrophyProperties> decode = TrophyProperties.CODEC.parse(NbtOps.INSTANCE, trophyProperties);
            this.trophyProperties = decode.result().orElse(TrophyProperties.builder().build());
        }
    }
}
