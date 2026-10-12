package net.neoforged.neoforge.event.entity.player;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemTooltipEvent extends PlayerEvent
{
    private final ItemStack itemStack;
    private final List<Component> toolTip;
    private final TooltipFlag flags;
    private final net.minecraft.world.item.Item.TooltipContext context;

    public ItemTooltipEvent(ItemStack itemStack, Player player, List<Component> toolTip,
                            TooltipFlag flags, net.minecraft.world.item.Item.TooltipContext context)
    {
        super(player);
        this.itemStack = itemStack;
        this.toolTip = toolTip;
        this.flags = flags;
        this.context = context;
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public List<Component> getToolTip()
    {
        return toolTip;
    }

    public TooltipFlag getFlags()
    {
        return flags;
    }

    public net.minecraft.world.item.Item.TooltipContext getContext()
    {
        return context;
    }
}
