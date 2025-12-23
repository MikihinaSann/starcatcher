package com.wdiscute.starcatcher.io;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.attachments.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID)
public class ModDataAttachments
{
    public static final Capability<DataAttachment<SingleStackContainer>> BOOKER_SKIN_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<DataAttachment<FishingBobAttachment>> FISHING_BOB_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<DataAttachment<FishingGuideAttachment>> FISHING_GUIDE_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    public static final DataAttachmentType<SingleStackContainer> BOBBER_SKIN = DataAttachmentType.register(
            BOOKER_SKIN_CAP, Starcatcher.rl("bobber_skin"),
            DataAttachmentType.builder(SingleStackContainer::new)
                    .sync(SingleStackContainer.STREAM_CODEC)
                    .serialize(SingleStackContainer.CODEC)
                    .canAttachTo(CapabilityType.ENTITY));

    public static final DataAttachmentType<FishingBobAttachment> FISHING_BOB = DataAttachmentType.register(
            FISHING_BOB_CAP, Starcatcher.rl("fishing_bob"),
            DataAttachmentType.builder(FishingBobAttachment::new)
                    .sync(FishingBobAttachment.STREAM_CODEC)
                    .canAttachTo(CapabilityType.PLAYER));

    public static final DataAttachmentType<FishingGuideAttachment> FISHING_GUIDE = DataAttachmentType.register(
            FISHING_GUIDE_CAP, Starcatcher.rl("fishing_guide"),
            DataAttachmentType.builder(FishingGuideAttachment::createDefault)
                    .sync(FishingGuideAttachment.STREAM_CODEC)
                    .serialize(FishingGuideAttachment.CODEC)
                    .canAttachTo(CapabilityType.PLAYER)
                    .copyOnDeath());

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Player> event) {
        DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                .filter(type -> type.attachment().getPotentialHolders().contains(CapabilityType.PLAYER))
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                .filter(type -> type.attachment().getPotentialHolders().contains(CapabilityType.ENTITY))
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((loc, attachments) -> event.register(attachments.attachment().getClass()));
    }


    // sets the value to default
    public static <T> void remove(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        holder.getCapability(attachmentType.attachment().getCapabilityKey()).orElseGet(attachmentType.getAttachment()).setDefault(holder);
    }

    public static <T> void set(ICapabilityProvider holder, DataAttachmentType<T> attachmentType, T data)
    {
        holder.getCapability(attachmentType.attachment().getCapabilityKey()).orElseGet(attachmentType::getAttachment).setAndSync(holder, data);
    }

    public static <T> T get(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        return holder.getCapability(attachmentType.attachment().getCapabilityKey()).orElseGet(attachmentType::getAttachment).getData();
    }

    public static <T> void sync(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        holder.getCapability(attachmentType.attachment().getCapabilityKey()).orElseGet(attachmentType::getAttachment).sync(holder);
    }


    public static <T> void remove(ICapabilityProvider holder, Capability<DataAttachment<T>> capKey)
    {
        holder.getCapability(capKey).ifPresent(cap -> cap.setDefault(holder));
    }

    public static <T> void set(ICapabilityProvider holder, Capability<DataAttachment<T>> capKey, T data)
    {
        holder.getCapability(capKey).ifPresent(cap -> cap.setAndSync(holder, data));
    }

    public static <T> T get(ICapabilityProvider holder, Capability<DataAttachment<T>> capKey)
    {
        LazyOptional<DataAttachment<T>> capability = holder.getCapability(capKey);
        if (!capability.isPresent()) return null;

        return capability.orElseThrow(IllegalStateException::new).getData();
    }

    public static <T> void sync(ICapabilityProvider holder, Capability<DataAttachment<T>> capKey)
    {
        holder.getCapability(capKey).ifPresent(cap -> cap.sync(holder));
    }

    public static <T> void setFrom(ICapabilityProvider holder, DataAttachment<T> attachment){
        holder.getCapability(attachment.getCapabilityKey()).ifPresent(cap -> cap.setNoSync(attachment.getData()));
    }


    public static abstract class DataAttachment1<T> extends DataAttachment<T> {}
    public static abstract class DataAttachment2<T> extends DataAttachment<T> {}
    public static abstract class DataAttachment3<T> extends DataAttachment<T> {}
    public static abstract class DataAttachment4<T> extends DataAttachment<T> {}
    public static abstract class DataAttachment5<T> extends DataAttachment<T> {}

}
