package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.fishentity.SCArmPoses;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity>
{
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void starcatcher$holdAboveHeadPose(T entity, float limbSwing, float limbSwingAmount,
                                               float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if (SCArmPoses.shouldHoldAboveHead(entity))
            SCArmPoses.applyHoldAboveHead((HumanoidModel<?>) (Object) this, entity, HumanoidArm.RIGHT);
    }
}
