package com.wdiscute.starcatcher.neoforged.neoforge.network.codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class NeoForgeStreamCodecs
{
    public static <E extends Enum<E>, B extends FriendlyByteBuf> StreamCodec<B, E> enumCodec(Class<E> enumClass)
    {
        E[] values = enumClass.getEnumConstants();
        return (StreamCodec) ByteBufCodecs.idMapper(i -> values[i], Enum::ordinal);
    }

    public static StreamCodec<FriendlyByteBuf, ItemStack> uncheckedSingletonItemStackCodec()
    {
        return (StreamCodec) ItemStack.OPTIONAL_STREAM_CODEC;
    }

    public static <B extends FriendlyByteBuf, V> StreamCodec<B, Optional<V>> optional(StreamCodec<B, V> codec)
    {
        return (StreamCodec) ByteBufCodecs.optional(codec);
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ItemStack> singletonItemStackCodec()
    {
        return ItemStack.OPTIONAL_STREAM_CODEC;
    }
}
