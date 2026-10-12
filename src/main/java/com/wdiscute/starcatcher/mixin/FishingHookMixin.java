package com.wdiscute.starcatcher.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.wdiscute.starcatcher.event.SCEvents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Fires the NeoForge-style {@link ItemFishedEvent} when a vanilla fishing hook
 * retrieves its loot, mirroring NeoForge's own patch of FishingHook.
 */
@Mixin(FishingHook.class)
public abstract class FishingHookMixin
{
    @SuppressWarnings("unchecked")
    @ModifyExpressionValue(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    private ObjectArrayList<ItemStack> starcatcher$itemFished(ObjectArrayList<ItemStack> original)
    {
        FishingHook hook = (FishingHook) (Object) this;
        ItemFishedEvent event = new ItemFishedEvent((List<ItemStack>) original, 1, hook);
        SCEvents.itemFished(event);
        if (event.isCanceled())
            return new ObjectArrayList<>();
        return new ObjectArrayList<>(event.getDrops());
    }
}
