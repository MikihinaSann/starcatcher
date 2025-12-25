package com.wdiscute.starcatcher.io.attachments;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.io.network.SyncCapabilityPayload;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
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
import java.util.function.Supplier;

public abstract class DataAttachment<C> implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    protected final LazyOptional<DataAttachment<C>> optional = LazyOptional.of(() -> this);
    private C data = null;

    public void setDefault(ICapabilityProvider holder){
        setAndSync(holder, getDefault().get());
    };

    /**
     * method from copying the variables from another capability of the same type,
     * Used for mimicking the set method from neo,
     * <p>
     * Automatically calls the sync method
     */
    public void setAndSync(ICapabilityProvider holder , C dataNew){
        setNoSync(dataNew);
        sync(holder);
    }

    /**
     * method from copying the variables from another capability of the same type.
     * Used for mimicking the set method from neo.
     */
    public void setNoSync(C dataNew){
        data = dataNew;
    };

    /**
     * Returns an empty value for mimicking the remove method from neo.
     * @return this empty capability.
     */
    @NotNull
    public abstract Supplier<C> getDefault();

    public abstract @Nullable StreamCodec<C> getStreamCodec();
    public abstract @Nullable Codec<C> getCodec();

    public abstract boolean isCopyOnDeath();
    public abstract ResourceLocation getId();
    public abstract Capability<? extends DataAttachment<C>> getCapabilityKey();

    public abstract List<CapabilityType> getPotentialHolders();


    public C getData() {
        if (data == null)
            return getDefault().get();

        return data;
    }

    public void setData(C data) {
        this.data = data;
    }


    public void sync(ICapabilityProvider holder){
        if (getStreamCodec() == null) return;

        CapabilityType capabilityType = CapabilityType.fromHolderSimple(holder);
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
        return getCapabilityKey().orEmpty(cap, optional.cast()) ;
    };

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        if (getCodec() != null) {
            getCodec().encodeStart(NbtOps.INSTANCE, getData()).resultOrPartial(Starcatcher.LOGGER::warn).ifPresent(tag -> compoundTag.put(getId().getPath(), tag));
        }

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        if (getCodec() == null) return;

        Tag tag = compoundTag.get(getId().getPath());
        DataResult<Pair<C, Tag>> decode = getCodec().decode(NbtOps.INSTANCE, tag);

        decode.result()
                .map(Pair::getFirst)
                .ifPresent(this::setNoSync);

    }
}
