package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.recipe.FishingRodSmithingRecipe;
import com.wdiscute.starcatcher.registry.ModItems;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class StarcatcherEmiSmithingRecipe implements EmiRecipe
{
    protected final EmiIngredient template;
    protected final EmiIngredient input;
    protected final EmiStack netheriteIngot = EmiStack.of(Items.NETHERITE_INGOT);
    protected final boolean isNetheriteUpgrade;
    protected final EmiStack output;

    public StarcatcherEmiSmithingRecipe(FishingRodSmithingRecipe recipe)
    {
        this.template = EmiIngredient.of(recipe.template());
        this.input = EmiIngredient.of(recipe.rod());

        ItemStack stack = Arrays.stream(recipe.rod().getItems()).findFirst().get().copy();

        ModDataComponents.set(stack, ModDataComponents.NETHERITE_UPGRADE, true);
        isNetheriteUpgrade = true;

        this.output = EmiStack.of(stack);
    }

    public StarcatcherEmiSmithingRecipe(Item item)
    {
        this.template = EmiIngredient.of(Ingredient.of(item));
        this.input = EmiIngredient.of(Ingredient.of(StarcatcherTags.RODS));

        ItemStack is = ModItems.ROD.get().getDefaultInstance();

        ResourceLocation wadd = ModDataComponents.get(item.getDefaultInstance(), ModDataComponents.TACKLE_SKIN);

        ModDataComponents.set(is, ModDataComponents.TACKLE_SKIN, wadd);
        isNetheriteUpgrade = false;

        this.output = EmiStack.of(is);
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return VanillaEmiRecipeCategories.SMITHING;
    }

    @Override
    public ResourceLocation getId()
    {
        return Starcatcher.rl("/" + BuiltInRegistries.ITEM.getKey(template.getEmiStacks().get(0).getItemStack().getItem()).getPath());
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return List.of(template, input);
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth()
    {
        return 112;
    }

    @Override
    public int getDisplayHeight()
    {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
        widgets.addSlot(template, 0, 0);
        widgets.addSlot(input, 18, 0);
        if (isNetheriteUpgrade) widgets.addSlot(netheriteIngot, 36, 0);
        widgets.addSlot(output, 94, 0).recipeContext(this);
    }
}
