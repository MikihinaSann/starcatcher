package com.wdiscute.starcatcher.io;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.attachments.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModDataAttachments {
    //Don't do this wrong, read the comment at the bottom of this class
    public static final Capability<DataAttachmentBobberSkin> BOOKER_SKIN_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<DataAttachmentFishingBob> FISHING_BOB_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<DataAttachmentFishingGuide> FISHING_GUIDE_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<DataAttachmentTackleSkin> TACKLE_SKIN_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    // Attaching capabilities to anything other than entities (like levels, chunks, BEs) isn't set up yet since it isn't needed
    public static final DataAttachmentType<FishingBobAttachment> FISHING_BOB = DataAttachmentType.register(
            FISHING_BOB_CAP, Starcatcher.rl("fishing_bob"),
            DataAttachmentType.builder(FishingBobAttachment::new)
                    .sync(FishingBobAttachment.STREAM_CODEC)
                    .canAttachTo(CapabilityType.LIVING_ENTITY));

    public static final DataAttachmentType<FishingGuideAttachment> FISHING_GUIDE = DataAttachmentType.register(
            FISHING_GUIDE_CAP, Starcatcher.rl("fishing_guide"),
            DataAttachmentType.builder(FishingGuideAttachment::createDefault)
                    .sync(FishingGuideAttachment.STREAM_CODEC)
                    .serialize(FishingGuideAttachment.CODEC)
                    .canAttachTo(CapabilityType.PLAYER)
                    .copyOnDeath());

    public static final DataAttachmentType<ResourceLocation> TACKLE_SKIN = DataAttachmentType.register(
            TACKLE_SKIN_CAP, Starcatcher.rl("tackle_skin"),
            DataAttachmentType.builder(() -> Starcatcher.rl("base"))
                    .sync(StreamCodec.RESOURCE_LOCATION)
                    .serialize(ResourceLocation.CODEC)
                    .canAttachTo(CapabilityType.NON_LIVING_ENTITY));



    // sets the value to default
    public static <T> void remove(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        if (holder == null) return;
        if (CapabilityType.checkWithHolder(holder, attachmentType)) return;

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.attachment().getCapabilityKey());
        if (!capability.isPresent())
            Starcatcher.LOGGER.error("can't remove capability: {} as it is not present for some reason?", attachmentType.name().toString());

        capability.ifPresent(cap -> cap.setDefault(holder));
    }

    public static <T> void set(ICapabilityProvider holder, DataAttachmentType<T> attachmentType, T data)
    {
        if (holder == null) return;
        if (CapabilityType.checkWithHolder(holder, attachmentType)) return;

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.attachment().getCapabilityKey());
        if (!capability.isPresent())
            Starcatcher.LOGGER.error("can't set capability: {} as it is not present for some reason?", attachmentType.name().toString());

        capability.ifPresent(cap -> cap.setAndSync(holder, data));
    }

    @NotNull
    public static <T> T get(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        if (holder == null)
            throw new NullPointerException("tried to get capability: " + attachmentType.name().toString() + " for a null holder");

        //Removed the error checking since just getting the default value should be fine
        return holder.getCapability(attachmentType.attachment().getCapabilityKey()).orElseGet(attachmentType::getAttachment).getData();
    }

    public static <T> void sync(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        if (holder == null) return;

        holder.getCapability(attachmentType.attachment().getCapabilityKey()).ifPresent(cap -> cap.sync(holder));
    }

    public static <T> void setFrom(ICapabilityProvider holder, DataAttachment<T> attachment){
        holder.getCapability(attachment.getCapabilityKey()).ifPresent(cap -> cap.setNoSync(attachment.getData()));
    }


    public static void init(){

    }

    /**
     * For some ungodly reason, forge doesn't actually register capabilities under the resourceLocation you give them.
     * Instead, it registers them as their "real name" - meaning the name of the data type they hold.
     * <p>
     * For example, my DataAttachment<> type would always be named as wdiscute/starcatcher/DataAttachment, and they would conflict (generics get ignored too)
     */
    public static abstract class DataAttachmentBobberSkin extends DataAttachment<SingleStackContainer>{}
    public static abstract class DataAttachmentFishingBob extends DataAttachment<FishingBobAttachment>{}
    public static abstract class DataAttachmentFishingGuide extends DataAttachment<FishingGuideAttachment>{}
    public static abstract class DataAttachmentTackleSkin extends DataAttachment<ResourceLocation>{}

}
