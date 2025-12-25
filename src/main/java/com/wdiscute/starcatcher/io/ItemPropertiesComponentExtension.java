package com.wdiscute.starcatcher.io;

import net.minecraft.world.item.Item;

import java.util.List;

public interface ItemPropertiesComponentExtension {
    default <T> Item.Properties component(ModDataComponents.DataComponent<T> component, T value) {
        return null;
    }

    default List<ModDataComponents.DataDefault<?>> getDefaultComponents(){
        return List.of();
    }

    default void addDefaultComponent(ModDataComponents.DataDefault<?> component){
    }
}
