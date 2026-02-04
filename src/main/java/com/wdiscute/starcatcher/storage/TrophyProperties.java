package com.wdiscute.starcatcher.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ExtraComposites;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record TrophyProperties(
        FishProperties fp,
        boolean alwaysShow,
        TrophyType trophyType,
        RarityProgress all,
        Map<FishProperties.Rarity, RarityProgress> progress,
        float chanceToCatch,
        boolean repeatable
) implements UnloadedModRegistry
{

    public RarityProgress getProgress(FishProperties.Rarity rarity)
    {
        return progress.getOrDefault(rarity, RarityProgress.DEFAULT);
    }

    /**
     * @deprecated use Builder instead
     */
    @Deprecated(forRemoval = true)
    public static final TrophyProperties DEFAULT = new TrophyProperties(
            FishProperties.builder().withFish(ModItems.MISSINGNO.getId()).build(),
            false,
            TrophyType.EXTRA,
            RarityProgress.DEFAULT,
            Map.of(),
            100,
            false
    );


    @Override
    public boolean isPresent(){
        return fp.isPresent();
    }

    //region codec

    public static final Codec<TrophyProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.fieldOf("fish_properties").forGetter(TrophyProperties::fp),
                    Codec.BOOL.fieldOf("always_show").forGetter(TrophyProperties::alwaysShow),
                    TrophyType.CODEC.fieldOf("trophy_type").forGetter(TrophyProperties::trophyType),
                    RarityProgress.CODEC.fieldOf("all").forGetter(TrophyProperties::all),
                    Codec.unboundedMap(FishProperties.Rarity.CODEC, RarityProgress.CODEC).fieldOf("progress").forGetter(TrophyProperties::progress),
                    Codec.FLOAT.fieldOf("percentage_chance_to_catch").forGetter(TrophyProperties::chanceToCatch),
                    Codec.BOOL.fieldOf("repeatable").forGetter(TrophyProperties::alwaysShow)
            ).apply(instance, TrophyProperties::new)
    );

    public static final StreamCodec<TrophyProperties> STREAM_CODEC = ExtraComposites.composite(
            FishProperties.STREAM_CODEC, TrophyProperties::fp,
            StreamCodec.BOOL, TrophyProperties::alwaysShow,
            TrophyType.STREAM_CODEC, TrophyProperties::trophyType,
            RarityProgress.STREAM_CODEC, TrophyProperties::all,
            StreamCodec.map(HashMap::new, FishProperties.Rarity.STREAM_CODEC, RarityProgress.STREAM_CODEC), TrophyProperties::progress, //TODO make better ig
            StreamCodec.FLOAT, TrophyProperties::chanceToCatch,
            StreamCodec.BOOL, TrophyProperties::repeatable,
            TrophyProperties::new
    );

    public static final StreamCodec<List<TrophyProperties>> LIST_STREAM_CODEC = STREAM_CODEC.list();

    public static final Codec<List<TrophyProperties>> LIST_CODEC = TrophyProperties.CODEC.listOf();

    //endregion

    public static Builder builder()
    {
        return new Builder();
    }

    public Supplier<Item> fish()
    {
        return this.fp.catchInfo().fish();
    }

    public static class Builder
    {
        private FishProperties.Builder fp = FishProperties.builder().withFish(ModItems.MISSINGNO.getId());
        private boolean hide_until_caught = false;
        private TrophyType trophyType = TrophyType.EXTRA;
        private RarityProgress all = RarityProgress.DEFAULT;
        private final Map<FishProperties.Rarity, RarityProgress> progressMap = new EnumMap<>(FishProperties.Rarity.class);
        private float chanceToCatch = 100;
        private boolean repeatable = false;

        private Builder()
        {
        }

        public Builder hideUntilCaught()
        {
            this.hide_until_caught = true;
            return this;
        }

        public Builder setTrophyType(TrophyType type)
        {
            this.trophyType = type;
            return this;
        }

        public Builder setAllProgress(RarityProgress progress)
        {
            this.all = progress;
            return this;
        }

        public Builder withProgress(FishProperties.Rarity rarity, RarityProgress progress)
        {
            this.progressMap.put(rarity, progress);
            return this;
        }

        public Builder withChanceToCatch(float chanceToCatch)
        {
            this.chanceToCatch = chanceToCatch;
            return this;
        }

        public Builder withRepeatable(boolean repeatable)
        {
            this.repeatable = repeatable;
            return this;
        }

        public Builder withFP(FishProperties.Builder builder)
        {
            this.fp = builder;
            return this;
        }

        public Builder setFish(ResourceLocation fish)
        {
            this.fp.withFish(fish);
            return this;
        }

        public TrophyProperties build()
        {
            return new TrophyProperties(
                    fp.build(),
                    hide_until_caught,
                    trophyType,
                    all,
                    progressMap,
                    chanceToCatch,
                    repeatable
            );
        }
    }

    public record RarityProgress(int total, int unique)
    {
        public static final Codec<RarityProgress> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("total").forGetter(RarityProgress::total),
                        Codec.INT.fieldOf("unique").forGetter(RarityProgress::unique)
                ).apply(instance, RarityProgress::new));

        public static final StreamCodec<RarityProgress> STREAM_CODEC = StreamCodec.composite(
                StreamCodec.INT, RarityProgress::total,
                StreamCodec.INT, RarityProgress::unique,
                RarityProgress::new
        );

        public static final RarityProgress DEFAULT = new RarityProgress(0, 0);

        public static RarityProgress fromAttachment(Player player){
            return new RarityProgress(0, FishingGuideAttachment.getFishesCaught(player).size());
        }
    }

    public enum TrophyType implements StringRepresentable
    {
        TROPHY("trophy"),
        SECRET("secret"),
        EXTRA("extra");

        public static final Codec<TrophyType> CODEC = StringRepresentable.fromEnum(TrophyType::values);
        public static final StreamCodec<TrophyType> STREAM_CODEC = StreamCodec.enumCodec(TrophyType.class);
        private final String key;

        TrophyType(String key)
        {
            this.key = key;
        }

        public @NotNull String getSerializedName()
        {
            return this.key;
        }
    }
}
