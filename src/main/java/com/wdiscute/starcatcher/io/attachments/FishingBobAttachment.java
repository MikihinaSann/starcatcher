package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.StreamCodec;

import java.util.UUID;

public class FishingBobAttachment {
    private String uuid;

    public FishingBobAttachment(String uuid) {
        this.uuid = uuid;
    }

    public static final Codec<FishingBobAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("uuid").forGetter(data -> data.uuid)
            ).apply(instance, FishingBobAttachment::new)
    );

    public static final StreamCodec<FishingBobAttachment> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.STRING_UTF8, data -> data.uuid,
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

}
