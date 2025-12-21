package com.wdiscute.starcatcher.io.attachments;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.io.network.SyncCapabilityPayload;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class NeoCapability<C extends NeoCapability<C>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public abstract DataAttachmentType<C> getAttachment();
    protected final LazyOptional<C> optional = LazyOptional.of(this::getThis);

    public void setDefault(ICapabilityProvider holder){
        setAndSync(holder, getDefault());
    };

    /**
     * method from copying the variables from another capability of the same type,
     * Used for mimicking the set method from neo,
     * <p>
     * Automatically calls the sync method
     */
    public void setAndSync(ICapabilityProvider holder ,C capNew){
        setNoSync(capNew);
        sync(holder);
    }

    /**
     * method from copying the variables from another capability of the same type.
     * Used for mimicking the set method from neo.
     */
    public abstract void setNoSync(C capNew);

    /**
     * Returns an empty value for mimicking the remove method from neo.
     * @return this empty capability.
     */
    public abstract C getDefault();

    public abstract List<CapabilityType> getPotentialHolders();

    public void sync(ICapabilityProvider holder){
        CapabilityType capabilityType = CapabilityType.fromHolder(holder);
        long data = switch (capabilityType) {
            case ENTITY,PLAYER -> ((Entity) holder).getId();

            case BLOCK_ENTITY -> ((BlockEntity) holder).getBlockPos().asLong();

            case CHUNK -> ((LevelChunk) holder).getPos().getWorldPosition().asLong();

            case LEVEL -> 0;

        };

        PacketDistributor.PacketTarget packetDistributor = switch (capabilityType){
            case ENTITY,PLAYER -> PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> (Entity) holder);

            case BLOCK_ENTITY -> PacketDistributor.TRACKING_CHUNK.with(() -> {
                BlockEntity blockEntity = (BlockEntity) holder;
               return blockEntity.getLevel().getChunkAt(blockEntity.getBlockPos());
            });

            case CHUNK -> PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) holder);

            case LEVEL -> PacketDistributor.DIMENSION.with(() -> ((Level) holder).dimension());
        };

        if (getAttachment().streamCodec() != null){
            ModNetworking.CHANNEL.send(packetDistributor, new SyncCapabilityPayload(capabilityType, data, this));
        }
    };

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side){
        return cap == getAttachment().capability() ? optional.cast() : LazyOptional.empty() ;
    };

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        DataAttachmentType<C> attachment = getAttachment();

        if (attachment.codec() != null) {
            attachment.codec().encodeStart(NbtOps.INSTANCE, getThis()).resultOrPartial(Starcatcher.LOGGER::warn).ifPresent(tag -> compoundTag.put(attachment.name().getPath(), tag));
        }

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        DataAttachmentType<C> attachment = getAttachment();
        if (attachment.codec() == null) return;

        Tag tag = compoundTag.get(attachment.name().getPath());
        DataResult<Pair<C, Tag>> decode = attachment.codec().decode(NbtOps.INSTANCE, tag);

        C toReplace = decode.result()
                .map(Pair::getFirst)
                .orElse(null);

        setNoSync(toReplace);
    }

    public C getThis(){
        return ((C) this);
    }
}
