package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.registry.ModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModifierShapelessRecipe extends ShapelessRecipe
{
    private final List<ResourceLocation> modifiers;
    private final List<ResourceLocation> catchModifiers;
    private final ResourceLocation bobberSkin;
    private final boolean isSimple;

    public ModifierShapelessRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, List<ResourceLocation> modifiers, List<ResourceLocation> catchModifiers, ResourceLocation bobberSkin) {
        super(id, group, category, result, ingredients);
        this.modifiers = modifiers;
        this.catchModifiers = catchModifiers;
        this.bobberSkin = bobberSkin;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    public ModifierShapelessRecipe(ShapelessRecipe recipe, List<ResourceLocation> modifiers, List<ResourceLocation> catchModifiers, ResourceLocation bobberSkin) {
        this(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.result, recipe.getIngredients(), modifiers, catchModifiers, bobberSkin);
    }



    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.MODIFIER_SHAPELESS_RECIPE.get();
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        StackedContents contents = new StackedContents();
        container.fillStackedContents(contents);

        long count = container.getItems().stream().filter(o -> !o.isEmpty()).count();
        if (count != this.getIngredients().size())
        {
            return false;
        }
        else if (!isSimple)
        {
            var nonEmptyItems = new java.util.ArrayList<ItemStack>((int) count);
            for (var item : container.getItems())
                if (!item.isEmpty())
                    nonEmptyItems.add(item);
            return RecipeMatcher.findMatches(nonEmptyItems, this.getIngredients()) != null;
        }
        else
        {
            return count == 1 && this.getIngredients().size() == 1
                    ? this.getIngredients().get(0).test(container.getItems().get(0))
                    : contents.canCraft(this, null);
        }
    }

    public static class Serializer implements RecipeSerializer<ModifierShapelessRecipe>
    {

        @Override
        public ModifierShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ShapelessRecipe original = ShapelessRecipe.Serializer.SHAPELESS_RECIPE.fromJson(recipeId, serializedRecipe);
            var modifiers = locsFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "minigame_modifiers", new JsonArray()));
            var catchModifiers = locsFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "catch_modifiers", new JsonArray()));

            ResourceLocation bobber = new ResourceLocation(GsonHelper.getAsString(serializedRecipe, "bobber_skin", Starcatcher.rl("missingno").toString()));

            return new ModifierShapelessRecipe(original,  modifiers, catchModifiers, bobber );
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
        public @Nullable ModifierShapelessRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new ModifierShapelessRecipe(
                    ShapelessRecipe.Serializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer),
                    StreamCodec.RESOURCE_LOCATION.list().decode(buffer),
                    StreamCodec.RESOURCE_LOCATION.list().decode(buffer),
                    StreamCodec.RESOURCE_LOCATION.decode(buffer)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ModifierShapelessRecipe recipe) {
            ShapelessRecipe.Serializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
            StreamCodec.RESOURCE_LOCATION.list().encode(buffer, recipe.modifiers);
            StreamCodec.RESOURCE_LOCATION.list().encode(buffer, recipe.catchModifiers);
            StreamCodec.RESOURCE_LOCATION.encode(buffer, recipe.bobberSkin);
        }
    }


}
