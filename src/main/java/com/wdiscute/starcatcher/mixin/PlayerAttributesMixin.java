package com.wdiscute.starcatcher.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import com.wdiscute.starcatcher.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Applies {@link EntityAttributeModificationEvent} registrations targeting
 * {@link net.minecraft.world.entity.EntityType#PLAYER} to Player#createAttributes.
 */
@Mixin(Player.class)
public abstract class PlayerAttributesMixin
{
    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void starcatcher$modifyPlayerAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir)
    {
        AttributeSupplier.Builder builder = cir.getReturnValue();
        for (EntityAttributeModificationEvent.Modification mod : EntityAttributeModificationEvent.REGISTRATIONS)
        {
            if (mod.type() == net.minecraft.world.entity.EntityType.PLAYER)
                builder.add(mod.attribute(), mod.value());
        }
    }
}
