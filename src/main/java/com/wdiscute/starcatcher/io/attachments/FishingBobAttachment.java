package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.StreamCodec;

import java.util.List;
import java.util.UUID;

public class FishingBobAttachment extends NeoCapability<FishingBobAttachment> {
    private String uuid;

    public FishingBobAttachment(String uuid) {
        this.uuid = uuid;
    }

    public FishingBobAttachment() {
        this.uuid = "";
    }

    public static final Codec<FishingBobAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("uuid").forGetter(data -> data.uuid)
            ).apply(instance, FishingBobAttachment::new)
    );

    public static final StreamCodec<FishingBobAttachment> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.STRING, data -> data.uuid,
            FishingBobAttachment::new
    );

    public boolean isEmpty() {
        return uuid.isEmpty();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    @Override
    public DataAttachmentType<FishingBobAttachment> getAttachment() {
        return ModDataAttachments.FISHING_BOB;
    }

    @Override
    public void setNoSync(FishingBobAttachment capNew) {
        uuid = capNew.uuid;
    }

    @Override
    public FishingBobAttachment getDefault() {
        return new FishingBobAttachment();
    }

    @Override
    public List<CapabilityType> getPotentialHolders() {
        return List.of(CapabilityType.PLAYER);
    }

}
