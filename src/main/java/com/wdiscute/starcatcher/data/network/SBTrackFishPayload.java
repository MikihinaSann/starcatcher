package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.attachment.AttachmentHandler;

public record SBTrackFishPayload(ResourceLocation fp) implements CustomPacketPayload
{

    public static final Type<SBTrackFishPayload> TYPE = new Type<>(Starcatcher.rl("track_fish"));

    public static final StreamCodec<ByteBuf, SBTrackFishPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            SBTrackFishPayload::fp,
            SBTrackFishPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            if (AttachmentHandler.getData(context.player(), SCDataAttachments.TRACKED_FISH).equals(fp))
                AttachmentHandler.setData(context.player(), SCDataAttachments.TRACKED_FISH, Starcatcher.MISSINGNO);
            else
                AttachmentHandler.setData(context.player(), SCDataAttachments.TRACKED_FISH, fp);
        });
    }
}
