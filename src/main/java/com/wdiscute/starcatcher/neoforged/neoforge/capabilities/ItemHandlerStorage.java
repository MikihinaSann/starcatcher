package com.wdiscute.starcatcher.neoforged.neoforge.capabilities;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import com.wdiscute.starcatcher.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Adapts the NeoForge-style {@link IItemHandler} shim to Fabric's
 * {@link SlottedStorage} so existing providers can be exposed through
 * ItemStorage.SIDED.
 */
public class ItemHandlerStorage implements SlottedStorage<ItemVariant>
{
    private final IItemHandler handler;

    public ItemHandlerStorage(IItemHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public int getSlotCount()
    {
        return handler.getSlots();
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot)
    {
        return new Slot(slot);
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction)
    {
        if (maxAmount <= 0) return 0;
        long remaining = maxAmount;
        for (int slot = 0; slot < handler.getSlots() && remaining > 0; slot++)
        {
            ItemStack toInsert = resource.toStack((int) remaining);
            ItemStack sim = handler.insertItem(slot, toInsert, true);
            int insertable = toInsert.getCount() - sim.getCount();
            if (insertable <= 0) continue;
            int inserted = insertable;
            int slotF = slot;
            transaction.addOuterCloseCallback(result ->
            {
                if (result.wasCommitted())
                    handler.insertItem(slotF, resource.toStack(inserted), false);
            });
            remaining -= insertable;
        }
        return maxAmount - remaining;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction)
    {
        if (maxAmount <= 0) return 0;
        long remaining = maxAmount;
        List<int[]> ops = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots() && remaining > 0; slot++)
        {
            ItemStack sim = handler.extractItem(slot, (int) remaining, true);
            if (sim.isEmpty() || !resource.matches(sim)) continue;
            int extractable = sim.getCount();
            ops.add(new int[]{slot, extractable});
            remaining -= extractable;
        }
        long extracted = maxAmount - remaining;
        transaction.addOuterCloseCallback(result ->
        {
            if (result.wasCommitted())
                for (int[] op : ops)
                    handler.extractItem(op[0], op[1], false);
        });
        return extracted;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterator<StorageView<ItemVariant>> iterator()
    {
        List<SingleSlotStorage<ItemVariant>> slots = new ArrayList<>();
        for (int i = 0; i < getSlotCount(); i++) slots.add(getSlot(i));
        return (Iterator) slots.iterator();
    }

    private class Slot implements SingleSlotStorage<ItemVariant>
    {
        private final int index;

        Slot(int index)
        {
            this.index = index;
        }

        @Override
        public boolean isResourceBlank()
        {
            return getResource().isBlank();
        }

        @Override
        public ItemVariant getResource()
        {
            return ItemVariant.of(handler.getStackInSlot(index));
        }

        @Override
        public long getAmount()
        {
            return handler.getStackInSlot(index).getCount();
        }

        @Override
        public long getCapacity()
        {
            return handler.getSlotLimit(index);
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction)
        {
            ItemStack sim = handler.insertItem(index, resource.toStack((int) maxAmount), true);
            int inserted = (int) maxAmount - sim.getCount();
            transaction.addOuterCloseCallback(result ->
            {
                if (result.wasCommitted())
                    handler.insertItem(index, resource.toStack(inserted), false);
            });
            return inserted;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction)
        {
            ItemStack sim = handler.extractItem(index, (int) maxAmount, true);
            if (sim.isEmpty() || !resource.matches(sim)) return 0;
            int extracted = sim.getCount();
            transaction.addOuterCloseCallback(result ->
            {
                if (result.wasCommitted())
                    handler.extractItem(index, extracted, false);
            });
            return extracted;
        }
    }
}
