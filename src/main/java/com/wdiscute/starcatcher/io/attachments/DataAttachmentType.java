package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.io.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record DataAttachmentType<T extends NeoCapability<T>>(
        Capability<T> capability, ResourceLocation name,
        @Nullable StreamCodec<T> streamCodec, @Nullable Codec<T> codec,
        boolean copyOnDeath, T defaultValue
) {

    public static Map<ResourceLocation, DataAttachmentType<? extends NeoCapability<?>>> DATA_ATTACHMENTS = new HashMap<>();
    public static Map<StreamCodec<? extends NeoCapability<?>>, ResourceLocation> DATA_ATTACHMENTS_CODECS = new HashMap<>();
    public static List<String> NAMES = new ArrayList<>();

    public static StreamCodec<StreamCodec<? extends NeoCapability<?>>> STREAM_CODEC_CODEC =
            StreamCodec.RESOURCE_LOCATION.remap(loc -> DataAttachmentType.DATA_ATTACHMENTS.get(loc).streamCodec, streamCodec -> DataAttachmentType.DATA_ATTACHMENTS_CODECS.get(streamCodec));


    public static <R extends NeoCapability<R>> DataAttachmentType<R> register(
            Capability<R> capability, ResourceLocation name,
            @Nullable StreamCodec<R> streamCodec, @Nullable Codec<R> codec,
            boolean copyOnDeath, R defaultValue
    ) {
        DataAttachmentType<R> dataAttachment = new DataAttachmentType<>(capability, name, streamCodec, codec, copyOnDeath, defaultValue);

        if (NAMES.contains(name.getPath())){
            // not even 2 mods with different namespaces can have the same name (for easier saving/loading)
            throw new IllegalStateException("Duplicate name for data attachment type " + name.getPath());
        }

        NAMES.add(name.getPath());

        if (streamCodec != null) {
            DATA_ATTACHMENTS.put(name, dataAttachment);
            DATA_ATTACHMENTS_CODECS.put(streamCodec, name);
        }

        return dataAttachment;
    }
}
