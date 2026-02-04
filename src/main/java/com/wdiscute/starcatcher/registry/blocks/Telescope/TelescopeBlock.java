package com.wdiscute.starcatcher.registry.blocks.Telescope;

import com.wdiscute.starcatcher.registry.blocks.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import net.nikdo53.tinymultiblocklib.components.IBlockPosOffsetEnum;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class TelescopeBlock extends AbstractMultiBlock implements IPreviewableMultiblock
{
    public TelescopeBlock()
    {
        super(BlockBehaviour.Properties.of());
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
        return List.of(center, center.above());
    }

    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean c)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable DirectionProperty getDirectionProperty()
    {
        return FACING;
    }

    @Override
    public BlockState getStateForEachBlock(BlockState state, BlockPos pos, BlockPos centerOffset, Level level, @Nullable Direction direction)
    {
        state = state.setValue(TELESCOPE_PART, IBlockPosOffsetEnum.fromOffset(
                TelescopePart.class,
                centerOffset,
                direction,
                TelescopePart.TOP
        ));

        return state;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return getStateForPlacementHelper(context, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState getDefaultStateForPreviews(Direction direction)
    {
        return IPreviewableMultiblock.super.getDefaultStateForPreviews(direction.getOpposite());
    }


    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<TelescopePart> TELESCOPE_PART = EnumProperty.create("part", TelescopePart.class);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TELESCOPE_PART);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) screen();

        return super.use(state, level, pos, player, hand, hit);
    }

    @OnlyIn(Dist.CLIENT)
    private void screen()
    {
        Minecraft.getInstance().setScreen(new TelescopeScreen(Component.empty()));
    }

    public enum TelescopePart implements StringRepresentable, IBlockPosOffsetEnum
    {
        BOTTOM("bottom", pos -> pos),
        TOP("top", BlockPos::above);

        private final String name;
        public final Function<BlockPos, BlockPos> offset;

        TelescopePart(String name, Function<BlockPos, BlockPos> offset)
        {
            this.name = name;
            this.offset = offset;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }

        @Override
        public Function<BlockPos, BlockPos> getOffsetFunction() {
            return offset;
        }
    }
}
