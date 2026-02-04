package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.registry.blocks.ModBlocks;
import com.wdiscute.starcatcher.registry.blocks.display.DisplayBlock;
import com.wdiscute.starcatcher.registry.blocks.display.DisplayBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FishingGuideItem extends Item
{
    public FishingGuideItem()
    {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        //if clicked on empty lectern
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(clickedPos);
        if(blockState.is(Blocks.LECTERN))
        {
            if(!blockState.getValue(LecternBlock.HAS_BOOK))
            {
                level.setBlockAndUpdate(clickedPos, ModBlocks.DISPLAY.get().defaultBlockState()
                        .setValue(DisplayBlock.HAS_BOOK, true)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
                );

                if(level.getBlockEntity(clickedPos) instanceof DisplayBlockEntity dbe)
                {
                    dbe.setBook(context.getItemInHand().copy());
                    if (context.getPlayer() != null && !context.getPlayer().isCreative())
                        context.getItemInHand().shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(level.isClientSide) openScreen();
        level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen()
    {
        Minecraft.getInstance().setScreen(new FishingGuideScreen());
    }
}
