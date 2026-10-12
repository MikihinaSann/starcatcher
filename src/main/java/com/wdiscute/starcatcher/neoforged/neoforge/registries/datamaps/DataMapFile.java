package com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

/**
 * Codec for a NeoForge-format data map json:
 * {@code { "replace": false, "values": { "<key|#tag>": value, ... } }}
 */
public record DataMapFile<T>(boolean replace, Map<String, T> values)
{
    public static <R, T> Codec<DataMapFile<T>> codec(ResourceKey<? extends Registry<R>> registryKey, Codec<T> valueCodec)
    {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.BOOL.optionalFieldOf("replace", false).forGetter(DataMapFile::replace),
                Codec.unboundedMap(Codec.STRING, valueCodec).optionalFieldOf("values", Map.of()).forGetter(DataMapFile::values)
        ).apply(inst, DataMapFile::new));
    }
}
