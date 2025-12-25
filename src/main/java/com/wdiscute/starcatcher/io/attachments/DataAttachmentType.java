package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.io.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public record DataAttachmentType<T>(
        ResourceLocation name,
        DataAttachment<T> attachment
) {

    public static Map<ResourceLocation, DataAttachmentType<?>> DATA_ATTACHMENTS = new HashMap<>();
    public static List<String> NAMES = new ArrayList<>();

    public static StreamCodec<DataAttachment<?>> STREAM_CODEC_CODEC =
            StreamCodec.RESOURCE_LOCATION.remap(
                    loc -> DataAttachmentType.DATA_ATTACHMENTS.get(loc).attachment,
                    DataAttachment::getId);


    public <B extends DataAttachment<?>> B getAttachment(){
        return (B) attachment;
    }

    public static <R> DataAttachmentType<R> register(
            Capability<? extends DataAttachment<R>> capability,
            ResourceLocation name,
            Builder<R> builder
    ) {

        if (builder.validHolders.isEmpty()){
            throw new IllegalStateException("Tried registering a DataAttachmentType without a any Holders!");
        }

        DataAttachmentType<R> dataAttachment = new DataAttachmentType<>(name, new DataAttachment<>() {
            @Override
            public @NotNull Supplier<R> getDefault() {
                return builder.defaultValue;
            }

            @Override
            public @Nullable StreamCodec<R> getStreamCodec() {
                return builder.streamCodec;
            }

            @Override
            public @Nullable Codec<R> getCodec() {
                return builder.codec;
            }

            @Override
            public boolean isCopyOnDeath() {
                return builder.copyOnDeath;
            }

            @Override
            public ResourceLocation getId() {
                return name;
            }

            @Override
            public Capability<? extends DataAttachment<R>> getCapabilityKey() {
                return capability;
            }

            @Override
            public List<CapabilityType> getPotentialHolders() {
                return builder.validHolders;
            }
        });

        if (NAMES.contains(name.getPath())){
            // not even 2 mods with different namespaces can have the same name (for easier saving/loading)
            throw new IllegalStateException("Duplicate name for data attachment type " + name.getPath());
        }

        NAMES.add(name.getPath());

        if (builder.streamCodec != null) {
            DATA_ATTACHMENTS.put(name, dataAttachment);
        }

        return dataAttachment;
    }

    public static <D> Builder<D> builder(Supplier<D> defaultValue){
        Builder<D> builder = new Builder<>();
        builder.defaultValue = defaultValue;
        return builder;
    }


    public static class Builder<B> {
        @Nullable StreamCodec<B> streamCodec = null;
        @Nullable Codec<B> codec = null;
        boolean copyOnDeath = false;
        Supplier<B> defaultValue;
        List<CapabilityType> validHolders = new ArrayList<>();

        public static <D> Builder<D> of(Supplier<D> defaultValue){
            Builder<D> builder = new Builder<>();
            builder.defaultValue = defaultValue;
            return builder;
        }

        public Builder<B> serialize(Codec<B> codec){
            this.codec = codec;
            return this;
        }

        public Builder<B> sync(StreamCodec<B> streamCodec){
            this.streamCodec = streamCodec;
            return this;
        }

        public Builder<B> copyOnDeath(){
            this.copyOnDeath = true;
            return this;
        }

        public Builder<B> canAttachTo(CapabilityType... holders){
            validHolders.addAll(Arrays.asList(holders));
            for (CapabilityType holder : holders) {
                holder.validateRepeats(validHolders);
            }
            return this;
        }

    }
}
