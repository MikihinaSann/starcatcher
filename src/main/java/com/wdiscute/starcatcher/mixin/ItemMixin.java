package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.io.ItemPropertiesComponentExtension;
import com.wdiscute.starcatcher.io.ItemStackDataComponentExtension;
import com.wdiscute.starcatcher.io.ModDataComponents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Properties properties, CallbackInfo ci){
        for (ModDataComponents.DataDefault<?> defaultComponent : properties.getDefaultComponents()) {
                ModDataComponents.registerDefault(self(), defaultComponent);
        }
    }

    private Item self(){
        return ((Item) (Object) this);
    }

}
