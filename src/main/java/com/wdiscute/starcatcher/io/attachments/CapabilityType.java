package com.wdiscute.starcatcher.io.attachments;

import com.wdiscute.starcatcher.io.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.stream.Stream;

public enum CapabilityType {
    PLAYER,
    ENTITY,
    BLOCK_ENTITY,
    LEVEL,
    CHUNK;

    public static final StreamCodec<CapabilityType> STREAM_CODEC = StreamCodec.enumCodec(CapabilityType.class);

    public static CapabilityType fromHolder(ICapabilityProvider holder){
        if (holder instanceof Entity){
            return ENTITY;
        }

        if (holder instanceof BlockEntity){
            return BLOCK_ENTITY;
        }

        if (holder instanceof Level){
            return LEVEL;
        }

        if (holder instanceof LevelChunk){
            return CHUNK;
        }

        throw new RuntimeException("Invalid capability provider type");
    }

}
