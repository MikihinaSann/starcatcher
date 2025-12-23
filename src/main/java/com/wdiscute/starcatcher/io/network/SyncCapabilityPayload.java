package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.attachments.CapabilityType;
import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import com.wdiscute.starcatcher.io.attachments.DataAttachment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;

public record SyncCapabilityPayload(CapabilityType type, long holderId, DataAttachment<?> capability) implements ToClientPacket{

    public static final StreamCodec<DataAttachment<?>> CAPABILITY_STREAM_CODEC =
            StreamCodec.of((buf, cap) -> {
                DataAttachmentType.STREAM_CODEC_CODEC.encode(buf,  cap);

                ((DataAttachment<Object>)cap).getStreamCodec().encode(buf, cap.getData());

            }, buf -> {
                DataAttachment<Object> attachment = (DataAttachment<Object>) DataAttachmentType.STREAM_CODEC_CODEC.decode(buf);
                Object data = attachment.getStreamCodec().decode(buf);

                attachment.setData(data);

                return attachment;
            });


    public static final StreamCodec<SyncCapabilityPayload> STREAM_CODEC = StreamCodec.composite(
            CapabilityType.STREAM_CODEC, SyncCapabilityPayload::type,
            StreamCodec.LONG, SyncCapabilityPayload::holderId,
            CAPABILITY_STREAM_CODEC, SyncCapabilityPayload::capability,
            SyncCapabilityPayload::new
    );


    @Override
    public void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player) {
        switch (type) {
            case ENTITY,PLAYER -> {
                int id = Math.toIntExact(holderId);
                Entity entity = level.getEntity(id);

                if (entity != null)
                    ModDataAttachments.setFrom(entity, capability);
            }

            case BLOCK_ENTITY -> {
                BlockEntity entity = level.getBlockEntity(BlockPos.of(holderId));

                if (entity != null)
                    ModDataAttachments.setFrom(entity, capability);
            }

            case CHUNK -> {

                LevelChunk chunk = level.getChunkAt(BlockPos.of(holderId));

                ModDataAttachments.setFrom(chunk, capability);
            }

            case LEVEL -> {
                ModDataAttachments.setFrom(level, capability);
            }

        }
    }
}
