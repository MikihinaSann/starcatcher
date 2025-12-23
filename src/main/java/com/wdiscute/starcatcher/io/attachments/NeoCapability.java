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
    public abstract @NotNull DataAttachmentType<C> getAttachment();
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
        if (getAttachment().streamCodec() == null) return;

        CapabilityType capabilityType = CapabilityType.fromHolder(holder);
        boolean isClient = true;
        long data = 0;
        PacketDistributor.PacketTarget packetDistributor = null;

         switch (capabilityType) {
            case ENTITY,PLAYER -> {
                Entity entity = (Entity) holder;
                data = entity.getId();
                isClient = entity.level().isClientSide();
                packetDistributor = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);
            }

            case BLOCK_ENTITY -> {
                BlockEntity blockEntity = (BlockEntity) holder;
                data = blockEntity.getBlockPos().asLong();
                isClient = blockEntity.getLevel().isClientSide();
                packetDistributor = PacketDistributor.TRACKING_CHUNK.with(() -> blockEntity.getLevel().getChunkAt(blockEntity.getBlockPos()));
            }

            case CHUNK -> {
                LevelChunk chunk = (LevelChunk) holder;
                data = chunk.getPos().getWorldPosition().asLong();
                isClient = chunk.getLevel().isClientSide();
                packetDistributor = PacketDistributor.TRACKING_CHUNK.with(() -> chunk);
            }

            case LEVEL -> {
                Level level = (Level) holder;
                // data isn't needed since clients have only 1 level
                isClient = level.isClientSide();
                packetDistributor = PacketDistributor.DIMENSION.with(level::dimension);
            }

        }

        if (isClient || packetDistributor == null) return;

        ModNetworking.CHANNEL.send(packetDistributor, new SyncCapabilityPayload(capabilityType, data, this));
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

        decode.result()
                .map(Pair::getFirst)
                .ifPresent(this::setNoSync);

    }

    public C getThis(){
        return ((C) this);
    }
}
