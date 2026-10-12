package com.wdiscute.starcatcher.neoforged.neoforge.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface IArmPoseTransformer
{
    void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm);
}
