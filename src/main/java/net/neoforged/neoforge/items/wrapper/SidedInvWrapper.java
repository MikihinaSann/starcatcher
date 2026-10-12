package net.neoforged.neoforge.items.wrapper;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric port shim of NeoForge's SidedInvWrapper backed by a WorldlyContainer.
 */
public class SidedInvWrapper extends InvWrapper
{
    private final WorldlyContainer sidedInv;
    private final Direction side;

    public SidedInvWrapper(WorldlyContainer inv, Direction side)
    {
        super(inv);
        this.sidedInv = inv;
        this.side = side;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        return sidedInv.canPlaceItemThroughFace(slot, stack, side)
                ? super.insertItem(slot, stack, simulate)
                : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return sidedInv.canTakeItemThroughFace(slot, getStackInSlot(slot), side)
                ? super.extractItem(slot, amount, simulate)
                : ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return sidedInv.canPlaceItemThroughFace(slot, stack, side);
    }
}
