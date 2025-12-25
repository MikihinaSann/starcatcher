package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.ItemPropertiesComponentExtension;
import com.wdiscute.starcatcher.io.ModDataComponents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin implements ItemPropertiesComponentExtension {
    static {
        System.out.println("NIKDO53 mixin yippee");
    }

    @Unique
    private final List<ModDataComponents.DataDefault<?>> DEFAULT_COMPONENTS = new ArrayList<>();


    @Inject(method = "stacksTo", at = @At("HEAD"))
    public void stacksTo(int maxStackSize, CallbackInfoReturnable<Item.Properties> cir) {
        Starcatcher.LOGGER.info("IT WORKS!!!! ");
    }

    @Override
    public <T> Item.Properties component(ModDataComponents.DataComponent<T> component, T value) {
        addDefaultComponent(new ModDataComponents.DataDefault<>(component, value));
        return self();
    }

    @Override
    public List<ModDataComponents.DataDefault<?>> getDefaultComponents() {
        return DEFAULT_COMPONENTS;
    }

    @Override
    public void addDefaultComponent(ModDataComponents.DataDefault<?> component) {
        DEFAULT_COMPONENTS.add(component);
    }

    private Item.Properties self(){
        return ((Item.Properties) (Object) this);
    }

}
