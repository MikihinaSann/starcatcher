package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.registry.ModRecipes;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.AbstractMinigameModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModifierShapedRecipe extends ShapedRecipe
{
   public List<ResourceLocation> modifiers;
   public final ResourceLocation bobberSkin;

    public ModifierShapedRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result, List<ResourceLocation> modifiers, ResourceLocation bobberSkin) {
        super(id, group, category, width, height, recipeItems, result);
        this.modifiers = modifiers;
        this.bobberSkin = bobberSkin;
    }

    public ModifierShapedRecipe(ShapedRecipe recipe, List<ResourceLocation> modifiers, ResourceLocation bobberSkin) {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.result);
        this.modifiers = modifiers;
        this.bobberSkin = bobberSkin;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.MODIFIER_SHAPED_RECIPE.get();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        var itemstack = super.assemble(container, registryAccess);

        List<ResourceLocation> catchModifiers = new ArrayList<>();
        List<ResourceLocation> minigameModifiers = new ArrayList<>();

        for (ResourceLocation rl : this.modifiers)
        {
            ResourceKey<Supplier<AbstractCatchModifier>> catchRK = ResourceKey.create(Starcatcher.CATCH_MODIFIERS, rl);
            ResourceKey<Supplier<AbstractMinigameModifier>> minigameRK = ResourceKey.create(Starcatcher.MINIGAME_MODIFIERS, rl);

            if(registryAccess.lookupOrThrow(Starcatcher.CATCH_MODIFIERS).get(catchRK).isPresent())
                catchModifiers.add(rl);

            if(registryAccess.lookupOrThrow(Starcatcher.MINIGAME_MODIFIERS).get(minigameRK).isPresent())
                minigameModifiers.add(rl);
        }

        if (!catchModifiers.isEmpty())
            ModDataComponents.set(itemstack, ModDataComponents.CATCH_MODIFIERS, catchModifiers);
        if (!minigameModifiers.isEmpty())
            ModDataComponents.set(itemstack, ModDataComponents.MINIGAME_MODIFIERS, minigameModifiers);

        return itemstack;

    }

    public static class Serializer implements RecipeSerializer<ModifierShapedRecipe>
    {

        @Override
        public ModifierShapedRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ShapedRecipe shapedRecipe = ShapedRecipe.Serializer.SHAPED_RECIPE.fromJson(recipeId, serializedRecipe);
            var locs = locsFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "modifiers"));
            ResourceLocation bobber = new ResourceLocation(GsonHelper.getAsString(serializedRecipe, "bobber_skin"));

            return new ModifierShapedRecipe(shapedRecipe, locs, bobber);
        }

        private static List<ResourceLocation> locsFromJson(JsonArray ingredientArray) {
            List<ResourceLocation> list = new ArrayList<>();

            for(int i = 0; i < ingredientArray.size(); ++i) {
                ResourceLocation loc =  new ResourceLocation(ingredientArray.get(i).getAsString());
                list.add(loc);
            }

            return list;
        }


        @Override
        public @Nullable ModifierShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new ModifierShapedRecipe(ShapedRecipe.Serializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer), StreamCodec.RESOURCE_LOCATION.list().decode(buffer), StreamCodec.RESOURCE_LOCATION.decode(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ModifierShapedRecipe recipe) {
            ShapedRecipe.Serializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            StreamCodec.RESOURCE_LOCATION.list().encode(recipe.modifiers, buffer);
            StreamCodec.RESOURCE_LOCATION.encode(recipe.bobberSkin, buffer);
        }
    }

}
