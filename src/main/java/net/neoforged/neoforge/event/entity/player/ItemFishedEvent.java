package net.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.List;

public class ItemFishedEvent extends PlayerEvent implements ICancellableEvent
{
    private final List<ItemStack> drops;
    private final FishingHook hook;
    private boolean canceled;
    private int rodDamage = 1;

    public ItemFishedEvent(List<ItemStack> drops, int rodDamage, FishingHook hook)
    {
        super(hook.getPlayerOwner());
        this.drops = drops;
        this.rodDamage = rodDamage;
        this.hook = hook;
    }

    public List<ItemStack> getDrops()
    {
        return drops;
    }

    public FishingHook getHookEntity()
    {
        return hook;
    }

    public int getRodDamage()
    {
        return rodDamage;
    }

    public void damageRodBy(int damage)
    {
        this.rodDamage = damage;
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
}
