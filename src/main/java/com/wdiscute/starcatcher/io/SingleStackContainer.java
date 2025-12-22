package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.io.attachments.CapabilityType;
import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import com.wdiscute.starcatcher.io.attachments.NeoCapability;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SingleStackContainer extends NeoCapability<SingleStackContainer> {
    ItemStack stack;

    public SingleStackContainer(ItemStack stack) {
        this.stack = stack;
    }

    public SingleStackContainer() {
        this.stack = ItemStack.EMPTY;
    }


    public static final Codec<SingleStackContainer> CODEC = ItemStack.CODEC.xmap(SingleStackContainer::new, SingleStackContainer::stack);

    public static final Codec<List<SingleStackContainer>> LIST_CODEC = SingleStackContainer.CODEC.listOf();

    public static final StreamCodec<SingleStackContainer> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.ITEM_STACK, SingleStackContainer::stack,
            SingleStackContainer::new
    );

    public ItemStack stack() {
        return stack;
    }

    public static final StreamCodec<List<SingleStackContainer>> STREAM_CODEC_LIST = STREAM_CODEC.list();

    public static List<SingleStackContainer> fromItemStackHandler(ItemStackHandler prizePool)
    {
        List<SingleStackContainer> list = new ArrayList<>();

        for (int i = 0; i < prizePool.getSlots(); i++)
        {
            list.add(new SingleStackContainer(prizePool.getStackInSlot(i)));
        }

        return list;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        SingleStackContainer other = (SingleStackContainer) o;
        return ItemStack.matches(this.stack, other.stack);
    }

    public static final SingleStackContainer EMPTY = new SingleStackContainer(ItemStack.EMPTY);
    public static final List<SingleStackContainer> EMPTY_LIST = List.of();

    @Override
    public @NotNull DataAttachmentType<SingleStackContainer> getAttachment() {
        return ModDataAttachments.BOBBER_SKIN;
    }

    @Override
    public void setNoSync(SingleStackContainer capNew) {
        this.stack = capNew.stack;
    }

    @Override
    public SingleStackContainer getDefault() {
        return new SingleStackContainer();
    }

    @Override
    public List<CapabilityType> getPotentialHolders() {
        return List.of(CapabilityType.PLAYER);
    }
}
