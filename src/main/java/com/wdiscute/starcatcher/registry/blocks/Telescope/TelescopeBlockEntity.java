package com.wdiscute.starcatcher.registry.blocks.Telescope;

import com.wdiscute.starcatcher.registry.blocks.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;

public class TelescopeBlockEntity extends AbstractMultiBlockEntity
{
    public TelescopeBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.TELESCOPE.get(), pos, blockState);
    }

}
