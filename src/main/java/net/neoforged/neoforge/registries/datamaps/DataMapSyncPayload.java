package net.neoforged.neoforge.registries.datamaps;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * S2C payload carrying resolved data-map values. Keys starting with '#' are
 * tag entries ({@code #namespace:tag}); the rest are plain registry keys.
 */
public record DataMapSyncPayload(Map<ResourceLocation, Map<String, byte[]>> values) implements CustomPacketPayload
{
    public static final Type<DataMapSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("neoforge", "data_map_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DataMapSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC,
                    ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.BYTE_ARRAY)),
            DataMapSyncPayload::values,
            DataMapSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
