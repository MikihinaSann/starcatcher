package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

public record FishingRodSmithingRecipe(
        Ingredient template,
        Ingredient rod
)
        implements SmithingRecipe
{

    @Override
    public boolean matches(Container container, Level level) {
        //netherite upgrade
        if(input.template().is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                && !ModDataComponents.has(input.base(), ModDataComponents.NETHERITE_UPGRADE)
                && input.addition().is(Items.NETHERITE_INGOT)
        ) return true;

        //bobber skins
        if(input.template().is(StarcatcherTags.TEMPLATES) && input.addition().isEmpty())
        {
            SingleStackContainer singleStackContainer = ModDataComponents.get(input.base(), ModDataComponents.BOBBER_SKIN);
            if(singleStackContainer == null) return true;

            //if bobber skin is the template, can not craft
            return !singleStackContainer.stack().is(input.template().getItem()) || singleStackContainer.stack().is(ModItems.COLORFUL_BOBBER_SMITHING_TEMPLATE);
        }

        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        ItemStack newRod = input.base().copy();

        if(input.template().is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE) && input.addition().is(Items.NETHERITE_INGOT))
        {
            ModDataComponents.set(newRod, ModDataComponents.NETHERITE_UPGRADE, true);
            return newRod;
        }

        if(input.template().is(StarcatcherTags.TEMPLATES))
        {
            ModDataComponents.set(newRod, ModDataComponents.BOBBER_SKIN, new SingleStackContainer(input.template().copy()));
            return newRod;
        }

        throw new RuntimeException("starcatcher - that template is not supported >:( talk to @wdiscute on discord");

    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack)
    {
        return this.template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack)
    {
        return this.rod.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries)
    {
        return Arrays.stream(this.rod.getItems()).findFirst().get();
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.FISHING_ROD_SMITHING.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return RecipeType.SMITHING;
    }

    @Override
    public boolean isIncomplete()
    {
        return Stream.of(this.template, this.rod).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer implements RecipeSerializer<FishingRodSmithingRecipe>
    {
        private static final MapCodec<FishingRodSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC.fieldOf("template").forGetter(FishingRodSmithingRecipe::template),
                        Ingredient.CODEC.fieldOf("rod").forGetter(FishingRodSmithingRecipe::rod)
                ).apply(instance, FishingRodSmithingRecipe::new)
        );

        public static final StreamCodec<FishingRodSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
                StreamCodec.INGREDIENT, FishingRodSmithingRecipe::template,
                StreamCodec.INGREDIENT, FishingRodSmithingRecipe::rod,
                FishingRodSmithingRecipe::new
        );

        @Override
        public FishingRodSmithingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            return null;
        }

        @Override
        public @Nullable FishingRodSmithingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return STREAM_CODEC.decode(buffer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FishingRodSmithingRecipe recipe) {
            STREAM_CODEC.encode(buffer, recipe);
        }
    }
}
