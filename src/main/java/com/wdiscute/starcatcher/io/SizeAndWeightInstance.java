package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SizeAndWeightInstance(
        int sizeInCentimeters,
        int weightInGrams,
        float percentile
)
{

    public static final Codec<SizeAndWeightInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("size").forGetter(SizeAndWeightInstance::sizeInCentimeters),
                    Codec.INT.fieldOf("weight").forGetter(SizeAndWeightInstance::weightInGrams),
                    Codec.FLOAT.optionalFieldOf("percentile", 0f).forGetter(SizeAndWeightInstance::percentile)
            ).apply(instance, SizeAndWeightInstance::new));

}
