package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.attachments.FishingBobAttachment;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import com.wdiscute.starcatcher.neoforged.bus.api.IEventBus;
import com.wdiscute.starcatcher.neoforged.neoforge.attachment.AttachmentType;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredRegister;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;
import com.wdiscute.starcatcher.neoforged.neoforge.attachment.AttachmentHandler;

public interface SCDataAttachments
{
    DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Starcatcher.MOD_ID);


    Supplier<AttachmentType<FishingBobAttachment>> FISHING_BOB = ATTACHMENT_TYPES.register(
            "fishing_bob", () -> AttachmentType.builder(() -> new FishingBobAttachment(""))
                    .sync(FishingBobAttachment.STREAM_CODEC)
                    .build()
    );

    Supplier<AttachmentType<List<Message>>> MESSAGES_CAUGHT = ATTACHMENT_TYPES.register(
            "messages_caught", () -> AttachmentType.builder(() -> List.<Message>of())
                    .sync(Message.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .build()
    );

    Supplier<AttachmentType<ResourceLocation>> TRACKED_FISH = ATTACHMENT_TYPES.register(
            "tracked_fish", () -> AttachmentType.builder(() -> Starcatcher.MISSINGNO)
                    .serialize(ResourceLocation.CODEC)
                    .sync(ResourceLocation.STREAM_CODEC)
                    .build()
    );

    Supplier<AttachmentType<FishingGuideAttachment>> FISHING_GUIDE = ATTACHMENT_TYPES.register(
            "fishing_guide", () -> AttachmentType.builder(FishingGuideAttachment::createDefault)
                    .serialize(FishingGuideAttachment.CODEC)
                    .sync(FishingGuideAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    Supplier<AttachmentType<ResourceLocation>> TACKLE_SKIN = ATTACHMENT_TYPES.register(
            "tackle_skin", () ->
                    AttachmentType.builder(() -> Starcatcher.BASE)
                            .serialize(ResourceLocation.CODEC)
                            .sync(ResourceLocation.STREAM_CODEC)
                            .build()
    );

    Supplier<AttachmentType<List<Modifier>>> MODIFIERS = ATTACHMENT_TYPES.register(
            "modifiers", () ->
                    AttachmentType.builder(() -> List.<Modifier>of())
                            .serialize(Modifier.CODEC.listOf())
                            .sync(ByteBufCodecs.fromCodec(Modifier.CODEC).apply(ByteBufCodecs.list()))
                            .build()
    );


    // sets the value to default
    static <T> void remove(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        if(holder == null) return;
        AttachmentHandler.removeData(holder, attachmentType);
    }

    // sets the value to default
    static <T> void remove(Entity holder, AttachmentType<T> attachmentType)
    {
        if(holder == null) return;
        AttachmentHandler.removeData(holder, attachmentType);
    }

    static <T> void set(Entity holder, Supplier<AttachmentType<T>> attachmentType, T data)
    {
        if(holder == null) return;
        AttachmentHandler.setData(holder, attachmentType, data);
    }

    static <T> void set(Entity holder, AttachmentType<T> attachmentType, T data)
    {
        if(holder == null) return;
        AttachmentHandler.setData(holder, attachmentType, data);
    }

    static <T> T get(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        if(holder == null) throw new RuntimeException("Called Starcatcher DataAttachments Get() with a null entity");
        return AttachmentHandler.getData(holder, attachmentType);
    }

    static <T> T get(Entity holder, AttachmentType<T> attachmentType)
    {
        if(holder == null) throw new RuntimeException("Called Starcatcher DataAttachments Get() with a null entity");
        return AttachmentHandler.getData(holder, attachmentType);
    }

    static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
