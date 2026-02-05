package com.wdiscute.starcatcher.items;

import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties
{
    public static final FoodProperties BASIC_RAW_FISH = new FoodProperties.Builder()
            .nutrition(2)
            .alwaysEat()
            .saturationMod(0.1f)
            .build();

    public static final FoodProperties BASIC_COOKED_FISH = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(2f)
            .build();

}
