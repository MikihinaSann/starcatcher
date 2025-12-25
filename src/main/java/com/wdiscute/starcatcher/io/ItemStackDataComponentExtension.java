package com.wdiscute.starcatcher.io;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemStackDataComponentExtension {
   default  <T> void set(ModDataComponents.DataComponent<T> component, T data){};

   default  <T> @Nullable T get(ModDataComponents.DataComponent<T> component) {
       return null;
   };

   default  <T> @NotNull T getOrDefault(ModDataComponents.DataComponent<T> component, T defaultValue){
       return defaultValue;
   };

   default  <T> boolean has(ModDataComponents.DataComponent<T> component){
       return false;
   };
}
