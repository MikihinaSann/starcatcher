package com.wdiscute.starcatcher.registry.blocks.display;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.blocks.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DisplayBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;
    public static final VoxelShape SHAPE_BASE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    public static final VoxelShape SHAPE_POST = Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 12.0);
    public static final VoxelShape SHAPE_TOP_PLATE = Block.box(0.0, 10.0, 0.0, 16.0, 14.0, 16.0);
    public static final VoxelShape SHAPE = Shapes.or(SHAPE_BASE, SHAPE_POST, SHAPE_TOP_PLATE);

    public DisplayBlock(BlockBehaviour.Properties properties)
    {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(POWERED, false).setValue(HAS_BOOK, false)
        );
    }

    public DisplayBlock()
    {
        super(BlockBehaviour.Properties.of());
        this.registerDefaultState(
                this.stateDefinition.any().setValue(POWERED, false).setValue(HAS_BOOK, false)
        );
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED, HAS_BOOK, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return ModBlockEntities.DISPLAY.get().create(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!state.is(newState.getBlock()))
        {
            if (state.getValue(HAS_BOOK))
            {
                this.popBook(state, level, pos);
            }

            super.onRemove(state, level, pos, newState, isMoving);
            if (state.getValue(POWERED))
            {
                level.updateNeighborsAt(pos.below(), this);
            }
        }
    }

    private void popBook(BlockState state, Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof LecternBlockEntity lecternblockentity)
        {
            ItemStack itemstack = lecternblockentity.getBook().copy();
            ItemEntity itementity = new ItemEntity(
                    level, (double) pos.getX() + 0.5, pos.getY() + 1, (double) pos.getZ() + 0.5, itemstack
            );
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
            lecternblockentity.clearContent();
        }
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        return side == Direction.UP && blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        if (blockState.getValue(HAS_BOOK))
        {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof LecternBlockEntity)
            {
                return ((LecternBlockEntity) blockentity).getRedstoneSignal();
            }
        }

        return 0;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        //if has book, open screen
        ItemStack stack = player.getItemInHand(hand);

        if (state.getValue(HAS_BOOK) && !stack.isEmpty())
        {
            //TODO SEND PACKET TO CLIENT TO OPEN SCREEN
            return InteractionResult.SUCCESS;
        }

        //place book
        if (!state.getValue(HAS_BOOK) && stack.is(ModItems.GUIDE.get()))
        {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
            {
                dbe.setBook(stack.copy());
                stack.shrink(1);

                level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            level.setBlockAndUpdate(pos, state.setValue(HAS_BOOK, true));
            return InteractionResult.SUCCESS;
        }

        //remove book
        if (stack.isEmpty() && state.getValue(HAS_BOOK) && level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
        {
            level.setBlockAndUpdate(pos, state.setValue(HAS_BOOK, false));
            player.addItem(dbe.getBook());
            dbe.setBook(ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return !state.getValue(HAS_BOOK) ? null : super.getMenuProvider(state, level, pos);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return level.isClientSide ? createTickerHelper(blockEntityType, ModBlockEntities.DISPLAY.get(), DisplayBlockEntity::bookAnimationTick) : null;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
