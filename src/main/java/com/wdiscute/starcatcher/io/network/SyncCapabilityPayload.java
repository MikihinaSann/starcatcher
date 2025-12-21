package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.attachments.CapabilityType;
import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import com.wdiscute.starcatcher.io.attachments.NeoCapability;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;

public record SyncCapabilityPayload(CapabilityType type, long holderId, NeoCapability<?> capability) implements ToClientPacket{
    public static final StreamCodec<NeoCapability<?>> CAPABILITY_STREAM_CODEC =
            StreamCodec.of((buf, cap) -> {
                StreamCodec<NeoCapability<?>> streamCodec = (StreamCodec<NeoCapability<?>>) cap.getAttachment().streamCodec();
                DataAttachmentType.STREAM_CODEC_CODEC.encode(buf, streamCodec);
                streamCodec.encode(buf, cap);
            }, buf -> {
                StreamCodec<? extends NeoCapability<?>> codec = DataAttachmentType.STREAM_CODEC_CODEC.decode(buf);
                return codec.decode(buf);
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
