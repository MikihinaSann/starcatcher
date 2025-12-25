package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.StreamCodec;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class FishingBobAttachment{
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

    public void setUuid(ICapabilityProvider holder, UUID uuid) {
        ModDataAttachments.set(holder, ModDataAttachments.FISHING_BOB, new FishingBobAttachment(uuid.toString()));
    }

    public UUID getUuid() {
        if (isEmpty()) return UUID.randomUUID();
        return UUID.fromString(uuid);
    }

}
