package com.wdiscute.starcatcher.io;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.items.ColorfulSmithingTemplate;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModDataComponents
{

    private static final Map<RegistryObject<Item>, List<DataDefault<?>>> DEFAULT_DATA_COMPONENTS = new HashMap<>();
    private static Map<Item, List<DataDefault<?>>> DEFAULT_DATA_COMPONENTS_REGISTERED = new HashMap<>();

    //smithing templates
    public static final DataComponent<Boolean> NETHERITE_UPGRADE = register("netherite_upgraded", Codec.BOOL);

    public static final DataComponent<SingleStackContainer> BOBBER_SKIN = register("bobber_skin", SingleStackContainer.CODEC);

    //bucketed fish
    public static final DataComponent<SingleStackContainer> BUCKETED_FISH = register("bucketed_fish", SingleStackContainer.CODEC);

    //rod menu
    public static final DataComponent<SingleStackContainer> BOBBER = register("bobber", SingleStackContainer.CODEC);

    public static final DataComponent<SingleStackContainer> BAIT = register("bait", SingleStackContainer.CODEC);

    public static final DataComponent<SingleStackContainer> HOOK = register("hook", SingleStackContainer.CODEC);

    public static final DataComponent<ColorfulSmithingTemplate.BobberColor> BOBBER_COLOR = register("color", ColorfulSmithingTemplate.BobberColor.CODEC);

    public static final DataComponent<TrophyProperties> TROPHY = register("trophy", TrophyProperties.CODEC);

    public static final DataComponent<FishProperties> FISH_PROPERTIES = register("fish_properties", FishProperties.CODEC);

    public static final DataComponent<SecretNote.Note> SECRET_NOTE = register("secret", SecretNote.Note.CODEC);

    public static final DataComponent<SizeAndWeightInstance> SIZE_AND_WEIGHT = register("size_weight", SizeAndWeightInstance.CODEC);

    public static final DataComponent<List<ResourceLocation>> MINIGAME_MODIFIERS = register("minigame_modifiers", ResourceLocation.CODEC.listOf());

    public static final DataComponent<List<ResourceLocation>> CATCH_MODIFIERS = register("catch_modifiers", ResourceLocation.CODEC.listOf());


    public static <T> DataComponent<T> register(String name, Codec<T> codec){
        if (DataComponent.NAMES.contains(name)){
            throw new IllegalArgumentException("Tried registering a DataComponent with a duplicate name " + name);
        }

        DataComponent.NAMES.add(name);
        return new DataComponent<>(name, codec);
    }


    public static  <T> void set(ItemStack stack, DataComponent<T> component, T data){
        component.setOn(stack, data);
    }

    @Nullable
    public static <T> T get(ItemStack stack, DataComponent<T> component){
        T ret = component.getOn(stack);

        if (ret == null && getDefaults().containsKey(stack.getItem())){
            List<DataDefault<?>> dataDefaults = getDefaults().get(stack.getItem());

            for (DataDefault<?> def : dataDefaults) {
                if (def.component.equals(component)){
                    ret = (T) def.data;
                    break;
                }
            }
        }

        return ret;
    }

    public static <T> boolean has(ItemStack stack, DataComponent<T> component){
        if (getDefaults().containsKey(stack.getItem())){

            if (getDefaults().get(stack.getItem()).stream().anyMatch(def -> def.component.equals(component))){
                return true;
            }
        }

        return component.isOn(stack);
    }

    public static  <T> void remove(ItemStack stack, DataComponent<T> component){
        component.removeFrom(stack);
    }

    @Nonnull
    public static <T> T getOrDefault(ItemStack stack, DataComponent<T> component, T defaultValue) {
        T value = get(stack, component);
        return value == null ? defaultValue : value;
    }

    public static Map<Item, List<DataDefault<?>>> getDefaults(){
        if (DEFAULT_DATA_COMPONENTS_REGISTERED == null){
            populateMap();
        }

        return DEFAULT_DATA_COMPONENTS_REGISTERED;
    }

    private static void populateMap(){
        DEFAULT_DATA_COMPONENTS_REGISTERED = new HashMap<>();

        DEFAULT_DATA_COMPONENTS.forEach((itemRegistryObject, dataComponent) ->
                DEFAULT_DATA_COMPONENTS_REGISTERED.put(itemRegistryObject.get(), dataComponent));

        DEFAULT_DATA_COMPONENTS.clear();
    }

    public static <T> void registerDefault(Item item, DataComponent<T> component, T data){
        registerDefault(item, List.of(new DataDefault<>(component, data)));
    }

    public static void registerDefault(Item item, List<DataDefault<?>> dataDefault){
        DEFAULT_DATA_COMPONENTS_REGISTERED.put(item, dataDefault);
    }


    public record DataComponent<T>(String name, Codec<T> codec){
        public static final List<String> NAMES = new ArrayList<>();

        private void setOn(ItemStack stack, T data){
            CompoundTag compoundTag = stack.getOrCreateTag();
            codec.encodeStart(NbtOps.INSTANCE, data).resultOrPartial(Starcatcher.LOGGER::warn).ifPresent(tag -> compoundTag.put(name, tag));
        }

        private void removeFrom(ItemStack stack){
            CompoundTag compoundTag = stack.getOrCreateTag();

            if (compoundTag.contains(name)){
                compoundTag.remove(name);
            }
        }

        private boolean isOn(ItemStack stack){
            return stack.getOrCreateTag().contains(name);
        }

        @Nullable
        private T getOn(ItemStack stack){
            Tag tag = stack.getOrCreateTag().get(name);
            DataResult<Pair<T, Tag>> decode = codec.decode(NbtOps.INSTANCE, tag);

            return decode.result()
                    .map(Pair::getFirst)
                    .orElse(null);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DataComponent<?> dataComponent){
                return dataComponent.name.equals(name);
            }
            return false;
        }
    }

    public record DataDefault<T>(DataComponent<T> component, T data){}

}
