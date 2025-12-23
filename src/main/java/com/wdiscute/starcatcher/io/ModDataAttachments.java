package com.wdiscute.starcatcher.io;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.io.attachments.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID)
public class ModDataAttachments
{
    public static final Capability<SingleStackContainer> BOOKER_SKIN_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<FishingBobAttachment> FISHING_BOB_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<FishingGuideAttachment> FISHING_GUIDE_CAP = CapabilityManager.get(new CapabilityToken<>() {});


    public static final DataAttachmentType<SingleStackContainer> BOBBER_SKIN = DataAttachmentType.register(
            BOOKER_SKIN_CAP, Starcatcher.rl("bobber_skin"), SingleStackContainer.STREAM_CODEC, SingleStackContainer.CODEC, false, new SingleStackContainer());

    public static final DataAttachmentType<FishingBobAttachment> FISHING_BOB = DataAttachmentType.register(
            FISHING_BOB_CAP, Starcatcher.rl("fishing_bob"), FishingBobAttachment.STREAM_CODEC, null, false, new FishingBobAttachment());

    public static final DataAttachmentType<FishingGuideAttachment> FISHING_GUIDE = DataAttachmentType.register(
            FISHING_GUIDE_CAP, Starcatcher.rl("fishing_guide"), FishingGuideAttachment.STREAM_CODEC, FishingGuideAttachment.CODEC, true, FishingGuideAttachment.createDefault());

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Player> event) {
        DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                .filter(attachment -> attachment.defaultValue().getPotentialHolders().contains(CapabilityType.PLAYER))
                .forEach(attachments -> event.addCapability(attachments.name(), attachments.defaultValue()));
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                .filter(attachment -> attachment.defaultValue().getPotentialHolders().contains(CapabilityType.ENTITY))
                .forEach(attachments -> event.addCapability(attachments.name(), attachments.defaultValue()));
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((loc, attachments) -> event.register(attachments.defaultValue().getClass()));
    }


    // sets the value to default
    public static <T extends NeoCapability<T>> void remove(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        holder.getCapability(attachmentType.capability()).orElseGet(attachmentType::defaultValue).setDefault(holder);
    }

    public static <T extends NeoCapability<T>> void set(ICapabilityProvider holder, DataAttachmentType<T> attachmentType, T data)
    {
        holder.getCapability(attachmentType.capability()).orElseGet(attachmentType::defaultValue).setAndSync(holder, data);
    }

    public static <T extends NeoCapability<T>> void setFrom(ICapabilityProvider holder, NeoCapability<T> capability)
    {
        holder.getCapability(capability.getAttachment().capability()).ifPresent(oldCap -> oldCap.setNoSync(capability.getThis()));
    }


    public static <T extends NeoCapability<T>> T get(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        return holder.getCapability(attachmentType.capability()).orElseGet(attachmentType::defaultValue);
    }

    public static <T extends NeoCapability<T>> void sync(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        holder.getCapability(attachmentType.capability()).orElseGet(attachmentType::defaultValue).sync(holder);
    }


}
