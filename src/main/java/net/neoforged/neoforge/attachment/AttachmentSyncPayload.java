package net.neoforged.neoforge.attachment;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * S2C payload syncing a single entity attachment value to tracking clients.
 */
public record AttachmentSyncPayload(int entityId, ResourceLocation attachmentId, byte[] data) implements CustomPacketPayload
{
    public static final Type<AttachmentSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("neoforge", "attachment_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AttachmentSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, AttachmentSyncPayload::entityId,
            ResourceLocation.STREAM_CODEC, AttachmentSyncPayload::attachmentId,
            ByteBufCodecs.BYTE_ARRAY, AttachmentSyncPayload::data,
            AttachmentSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
