package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ExtraComposites;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
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

public class ModifierShapelessRecipe implements CraftingRecipe
{
    final String group;
    final CraftingBookCategory category;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;
    private final List<ResourceLocation> modifiers;

    public ModifierShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients, List<ResourceLocation> modifiers)
    {
        this.group = group;
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.modifiers = modifiers;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.MODIFIER_SHAPELESS_RECIPE.get();
    }

    @Override
    public String getGroup()
    {
        return this.group;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public CraftingBookCategory category()
    {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries)
    {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return this.ingredients;
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess)
    {
        return this.result.copy();
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        StackedContents contents = new StackedContents();
        container.fillStackedContents(contents);

        if (container.getItems().size() != this.ingredients.size())
        {
            return false;
        }
        else if (!isSimple)
        {
            var nonEmptyItems = new java.util.ArrayList<ItemStack>(container.getItems().size());
            for (var item : container.getItems())
                if (!item.isEmpty())
                    nonEmptyItems.add(item);
            return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
        }
        else
        {
            return container.getItems().size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.get(0).test(container.getItems().get(0))
                    : contents.canCraft(this, null);
        }
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= this.ingredients.size();
    }

    public static class Serializer implements RecipeSerializer<ModifierShapelessRecipe>
    {

        public static final StreamCodec<ModifierShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
                StreamCodec.STRING, rec -> rec.group,
                StreamCodec.CRAFTING_BOOK_CATEGORY, rec -> rec.category,
                StreamCodec.ITEM_STACK, rec -> rec.result,
                StreamCodec.INGREDIENT.nonNullList(Ingredient.EMPTY), rec -> rec.ingredients,
                StreamCodec.RESOURCE_LOCATION.list(), rec -> rec.modifiers,
                ModifierShapelessRecipe::new
        );

        @Override
        public ModifierShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String source = GsonHelper.getAsString(json, "group");
            CraftingBookCategory bookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
            List<ResourceLocation> modifiers = locsFromJson(GsonHelper.getAsJsonArray(json, "modifiers"));

            return new ModifierShapelessRecipe(source, bookCategory, result, ingredients, modifiers);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i), false);
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
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
            return STREAM_CODEC.decode(buffer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ModifierShapelessRecipe recipe) {
            STREAM_CODEC.encode(buffer, recipe);
        }
    }


}
