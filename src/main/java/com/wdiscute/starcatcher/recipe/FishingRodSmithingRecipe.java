package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.ModRecipes;
import com.wdiscute.starcatcher.registry.custom.tackleskin.AbstractTackleSkin;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record FishingRodSmithingRecipe(
        ResourceLocation id,
        Ingredient template,
        Ingredient rod
)
        implements SmithingRecipe
{

    @Override
    public boolean matches(Container container, Level level) {
        //netherite upgrade
        if(template(container).is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                && !ModDataComponents.has(base(container), ModDataComponents.NETHERITE_UPGRADE)
                && addition(container).is(Items.NETHERITE_INGOT)
        ) return true;

        //bobber skins
        if(ModDataComponents.has(template(container), ModDataComponents.TACKLE_SKIN) && addition(container).isEmpty())
        {
            ResourceLocation rl = ModDataComponents.get(template(container), ModDataComponents.TACKLE_SKIN);

            Optional<Supplier<AbstractTackleSkin>> optional = level.registryAccess().registryOrThrow(Starcatcher.TACKLE_SKIN).getOptional(rl);

            //if bobber skin is the template, can not craft
            return optional.isPresent();
        }

        return false;
    }

    private static @NotNull ItemStack template(Container container) {
        return container.getItem(0);
    }

    private static @NotNull ItemStack addition(Container container) {
        return container.getItem(2);
    }

    private static @NotNull ItemStack base(Container container) {
        return container.getItem(1);
    }


    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        ItemStack newRod = base(container).copy();

        if(template(container).is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE) && addition(container).is(Items.NETHERITE_INGOT))
        {
            ModDataComponents.set(newRod, ModDataComponents.NETHERITE_UPGRADE, true);
            return newRod;
        }

        //assemble bobber skin
        if (ModDataComponents.has(template(container), ModDataComponents.TACKLE_SKIN) && addition(container).isEmpty())
        {
            ModDataComponents.set(newRod, ModDataComponents.TACKLE_SKIN, ModDataComponents.get(template(container), ModDataComponents.TACKLE_SKIN));
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
        return Arrays.stream(this.rod.getItems()).findFirst().get();
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
        return Stream.of(this.template, this.rod).anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<FishingRodSmithingRecipe>
    {
        public static final StreamCodec<FishingRodSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
                StreamCodec.RESOURCE_LOCATION, FishingRodSmithingRecipe::id,
                StreamCodec.INGREDIENT, FishingRodSmithingRecipe::template,
                StreamCodec.INGREDIENT, FishingRodSmithingRecipe::rod,
                FishingRodSmithingRecipe::new
        );

        @Override
        public FishingRodSmithingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient template = Ingredient.fromJson(serializedRecipe.getAsJsonObject("template"));
            Ingredient rod = Ingredient.fromJson(serializedRecipe.getAsJsonObject("rod"));

            return new FishingRodSmithingRecipe(recipeId, template, rod);
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
