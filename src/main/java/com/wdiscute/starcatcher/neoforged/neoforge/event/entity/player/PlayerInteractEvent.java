package com.wdiscute.starcatcher.neoforged.neoforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.wdiscute.starcatcher.neoforged.bus.api.ICancellableEvent;

public abstract class PlayerInteractEvent extends PlayerEvent implements ICancellableEvent
{
    private final ItemStack itemStack;
    private final InteractionHand hand;
    private final BlockPos pos;
    private boolean canceled;

    public PlayerInteractEvent(Player player, InteractionHand hand, BlockPos pos)
    {
        super(player);
        this.itemStack = player.getItemInHand(hand);
        this.hand = hand;
        this.pos = pos;
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public InteractionHand getHand()
    {
        return hand;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public Level getLevel()
    {
        return getEntity().level();
    }

    @Override
    public boolean isCanceled()
    {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled)
    {
        this.canceled = canceled;
    }

    public static class RightClickBlock extends PlayerInteractEvent
    {
        private final net.minecraft.world.phys.BlockHitResult hitVec;
        private final net.minecraft.core.Direction face;

        public RightClickBlock(Player player, InteractionHand hand, BlockPos pos,
                               net.minecraft.world.phys.BlockHitResult hitVec)
        {
            super(player, hand, pos);
            this.hitVec = hitVec;
            this.face = hitVec != null ? hitVec.getDirection() : null;
        }

        public net.minecraft.world.phys.BlockHitResult getHitVec()
        {
            return hitVec;
        }

        public net.minecraft.core.Direction getFace()
        {
            return face;
        }
    }

    public static class RightClickItem extends PlayerInteractEvent
    {
        public RightClickItem(Player player, InteractionHand hand, BlockPos pos)
        {
            super(player, hand, pos);
        }
    }

    public static class EntityInteract extends PlayerInteractEvent
    {
        private final net.minecraft.world.entity.Entity target;

        public EntityInteract(Player player, InteractionHand hand, net.minecraft.world.entity.Entity target)
        {
            super(player, hand, target.blockPosition());
            this.target = target;
        }

        public net.minecraft.world.entity.Entity getTarget()
        {
            return target;
        }
    }
}
