package com.wdiscute.starcatcher.registry.blocks.display;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.blocks.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayBlockEntity extends BlockEntity
{
    ItemStack book = ItemStack.EMPTY;

    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;

    public static void bookAnimationTick(Level level, BlockPos pos, BlockState state, DisplayBlockEntity display)
    {
        display.oOpen = display.open;
        display.oRot = display.rot;
        Player player = level.getNearestPlayer((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 3.0, false);
        if (player != null)
        {
            double d0 = player.getX() - ((double) pos.getX() + 0.5);
            double d1 = player.getZ() - ((double) pos.getZ() + 0.5);
            display.tRot = (float) Mth.atan2(d1, d0);
            display.open += 0.1F;
            if (display.open < 0.8F || U.r.nextInt(40) == 0)
            {
                float f1 = display.flipT;

                do
                {
                    display.flipT = display.flipT + (float) (U.r.nextInt(4) - U.r.nextInt(4));
                } while (f1 == display.flipT);
            }
        } else
        {
            //display.tRot += 0.02F;
            display.open -= 0.1F;
        }

        while (display.rot >= (float) Math.PI) display.rot -= (float) (Math.PI * 2);
        while (display.rot < (float) -Math.PI) display.rot += (float) (Math.PI * 2);

        while (display.tRot >= (float) Math.PI) display.tRot -= (float) (Math.PI * 2);
        while (display.tRot < (float) -Math.PI) display.tRot += (float) (Math.PI * 2);

        float f2 = display.tRot - display.rot;

        while (f2 >= (float) Math.PI) f2 -= (float) (Math.PI * 2);

        while (f2 < (float) -Math.PI) f2 += (float) (Math.PI * 2);

        display.rot += f2 * 0.1F;
        display.open = Mth.clamp(display.open, 0.0F, 1.0F);
        display.time++;
        display.oFlip = display.flip;
        float f = (display.flipT - display.flip) * 0.1F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        display.flipA = display.flipA + (f - display.flipA) * 0.9F;
        display.flip = display.flip + display.flipA;
    }

    public DisplayBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.DISPLAY.get(), pos, blockState);
    }

    public ItemStack getBook()
    {
        return this.book;
    }

    public void setBook(ItemStack stack)
    {
        this.book = stack;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        if (tag.contains("Book", 10))
        {
            this.book = ItemStack.parse(registries, tag.getCompound("Book")).orElse(ItemStack.EMPTY);
        } else
        {
            this.book = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        if (!this.getBook().isEmpty())
        {
            tag.put("Book", this.getBook().save(registries));
        }
    }
}
