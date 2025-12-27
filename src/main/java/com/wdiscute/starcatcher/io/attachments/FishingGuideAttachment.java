package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishingGuideAttachment{
    public Map<ResourceLocation, FishCaughtCounter> fishesCaught;
    public Map<ResourceLocation, Integer> trophiesCaught;
    public boolean receivedGuide;

    public FishingGuideAttachment(Map<ResourceLocation, FishCaughtCounter> fishesCaught, Map<ResourceLocation, Integer> trophiesCaught, boolean receivedGuide ) {
        this.fishesCaught = new HashMap<>(fishesCaught); //guarantees the map is mutable
        this.trophiesCaught = new HashMap<>(trophiesCaught);
        this.receivedGuide = receivedGuide;
    }

    public static FishingGuideAttachment createDefault() {
        return new FishingGuideAttachment(
                new HashMap<>(),
                new HashMap<>(),
                false);
    }

    public static final Codec<FishingGuideAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(ResourceLocation.CODEC, FishCaughtCounter.CODEC).fieldOf("fishes_caught").forGetter(data -> data.fishesCaught),
                    Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("trophies_caught").forGetter(data -> data.trophiesCaught),
                    Codec.BOOL.optionalFieldOf("received_guide", false).forGetter(data -> data.receivedGuide)
            ).apply(instance, FishingGuideAttachment::new)
    );

    public static final StreamCodec<FishingGuideAttachment> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.map(HashMap::new, StreamCodec.RESOURCE_LOCATION, FishCaughtCounter.STREAM_CODEC), data -> data.fishesCaught,
            StreamCodec.map(HashMap::new, StreamCodec.RESOURCE_LOCATION, StreamCodec.INT), data -> data.trophiesCaught,
            StreamCodec.BOOL, data -> data.receivedGuide,

            FishingGuideAttachment::new
    );

    public static Map<ResourceLocation, Integer> getTrophiesCaught(Player player) {
        return get(player).trophiesCaught;
    }

    public static void setTrophiesCaught(Player player, Map<ResourceLocation, Integer> trophiesCaught) {
        get(player).trophiesCaught = trophiesCaught;
        sync(player);
    }

    public static Map<ResourceLocation, FishCaughtCounter> getFishesCaught(Player player) {
        return get(player).fishesCaught;
    }

    public static void setFishesCaught(Player player, Map<ResourceLocation, FishCaughtCounter> fishesCaught) {
        get(player).fishesCaught = fishesCaught;
        sync(player);
    }

    public static boolean getReceivedGuide(Player player) {
        return get(player).receivedGuide;
    }

    public static void setReceivedGuide(Player player, boolean receivedGuide) {
        FishingGuideAttachment fishingGuideAttachment = get(player);
        fishingGuideAttachment.receivedGuide = receivedGuide;
        ModDataAttachments.set(player, ModDataAttachments.FISHING_GUIDE, fishingGuideAttachment);
    }

    public static FishingGuideAttachment get(Entity holder){
        return ModDataAttachments.get(holder, ModDataAttachments.FISHING_GUIDE);
    }

    public static void sync(Player player){
        ModDataAttachments.sync(player, ModDataAttachments.FISHING_GUIDE);
    }
}
