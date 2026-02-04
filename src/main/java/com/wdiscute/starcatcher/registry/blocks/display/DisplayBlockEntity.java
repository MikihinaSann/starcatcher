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
import net.minecraft.world.phys.AABB;

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

    public static void bookAnimationTick(Level level, BlockPos pos, BlockState state, DisplayBlockEntity enchantingTable) {
        enchantingTable.oOpen = enchantingTable.open;
        enchantingTable.oRot = enchantingTable.rot;
        Player player = level.getNearestPlayer((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 3.0, false);
        if (player != null) {
            double d0 = player.getX() - ((double)pos.getX() + 0.5);
            double d1 = player.getZ() - ((double)pos.getZ() + 0.5);
            enchantingTable.tRot = (float) Mth.atan2(d1, d0);
            enchantingTable.open += 0.1F;
            if (enchantingTable.open < 0.5F || U.r.nextInt(40) == 0) {
                float f1 = enchantingTable.flipT;

                do {
                    enchantingTable.flipT = enchantingTable.flipT + (float)(U.r.nextInt(4) - U.r.nextInt(4));
                } while (f1 == enchantingTable.flipT);
            }
        } else {
            //enchantingTable.tRot += 0.02F;
            enchantingTable.open -= 0.1F;
        }

        while (enchantingTable.rot >= (float) Math.PI) {
            enchantingTable.rot -= (float) (Math.PI * 2);
        }

        while (enchantingTable.rot < (float) -Math.PI) {
            enchantingTable.rot += (float) (Math.PI * 2);
        }

        while (enchantingTable.tRot >= (float) Math.PI) {
            enchantingTable.tRot -= (float) (Math.PI * 2);
        }

        while (enchantingTable.tRot < (float) -Math.PI) {
            enchantingTable.tRot += (float) (Math.PI * 2);
        }

        float f2 = enchantingTable.tRot - enchantingTable.rot;

        while (f2 >= (float) Math.PI) {
            f2 -= (float) (Math.PI * 2);
        }

        while (f2 < (float) -Math.PI) {
            f2 += (float) (Math.PI * 2);
        }

        enchantingTable.rot += f2 * 0.4F;
        enchantingTable.open = Mth.clamp(enchantingTable.open, 0.0F, 1.0F);
        enchantingTable.time++;
        enchantingTable.oFlip = enchantingTable.flip;
        float f = (enchantingTable.flipT - enchantingTable.flip) * 0.4F;
        float f3 = 0.2F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        enchantingTable.flipA = enchantingTable.flipA + (f - enchantingTable.flipA) * 0.9F;
        enchantingTable.flip = enchantingTable.flip + enchantingTable.flipA;
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
    public AABB getRenderBoundingBox() {
        BlockPos pos = getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        if (tag.contains("Book", 10))
        {
            this.book = ItemStack.of(tag.getCompound("Book"));
        } else
        {
            this.book = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        if (!this.getBook().isEmpty())
        {
            tag.put("Book", this.getBook().save(new CompoundTag()));
        }
    }
}
