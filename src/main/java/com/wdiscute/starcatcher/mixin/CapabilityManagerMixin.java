package com.wdiscute.starcatcher.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapabilityManager.class)
public abstract class CapabilityManagerMixin {
    @Unique
    int id = 0;


    @Inject(method = "get(Ljava/lang/String;Z)Lnet/minecraftforge/common/capabilities/Capability;", at = @At("HEAD"), remap = false)
    <T> void get(String realName, boolean registering, CallbackInfoReturnable<Capability<T>> cir, @Local(argsOnly = true) LocalRef<String> name){
        if (realName.endsWith("DataAttachment")){
            name.set(realName + id);
            id++;
        }
    }

}
