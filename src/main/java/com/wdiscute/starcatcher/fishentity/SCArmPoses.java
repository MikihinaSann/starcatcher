package com.wdiscute.starcatcher.fishentity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import com.wdiscute.starcatcher.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public interface SCArmPoses
{
    static void applyHoldAboveHead(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm)
    {
        model.rightArm.xRot = -(float) Math.PI * 0.95F;
        model.rightArm.yRot = 0.0F;
        model.rightArm.zRot = 0.0F;

        model.leftArm.xRot = -(float) Math.PI * 0.95F;
        model.leftArm.yRot = 0.0F;
        model.leftArm.zRot = 0.0F;
    }

    static boolean shouldHoldAboveHead(LivingEntity entity)
    {
        if (!(entity instanceof Player)) return false;
        return IClientItemExtensions.of(entity.getMainHandItem()).isHoldAboveHead()
                || IClientItemExtensions.of(entity.getOffhandItem()).isHoldAboveHead();
    }
}
