package net.neoforged.neoforge.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T>
        implements net.minecraft.world.level.ItemLike
{
    public DeferredBlock(ResourceKey<Block> key, Holder<Block> holder)
    {
        super(key, holder);
    }

    public DeferredBlock(ResourceKey<Block> key, net.minecraft.core.Registry<Block> registry)
    {
        super(key, registry);
    }

    public BlockState defaultBlockState()
    {
        return get().defaultBlockState();
    }

    @Override
    public net.minecraft.world.item.Item asItem()
    {
        return get().asItem();
    }
}
