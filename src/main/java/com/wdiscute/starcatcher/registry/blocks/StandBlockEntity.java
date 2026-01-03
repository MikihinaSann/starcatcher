package com.wdiscute.starcatcher.registry.blocks;

import com.wdiscute.starcatcher.io.NBTCodecHelper;
import com.wdiscute.starcatcher.tournament.StandMenu;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StandBlockEntity extends AbstractMultiBlockEntity implements MenuProvider
{
    public Tournament tournament;
    private UUID uuid;

    public final ItemStackHandler entryCost = new ItemStackHandler(9)
    {
        @Override
        protected int getStackLimit(int slot, ItemStack stack)
        {
            return 64;
        }

    };

    public StandBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.STAND.get(), pos, blockState);
    }

    public UUID getUuid(){
        if (this.uuid == null){
            setUuid(UUID.randomUUID());
        }
        return this.uuid;
    }

    public void setUuid(UUID uuid){
        this.uuid = uuid;
        sync();
    }

    public void sync(){
        setChanged();

        if (level instanceof ServerLevel serverLevel){
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public Component getDisplayName()
    {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        return new StandMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag, registries);

        if (!isCenter()) return;

        if(uuid != null)
            tag.putUUID("tournament_uuid", uuid);

        NBTCodecHelper.encode(Tournament.CODEC, tournament, tag, "tournament");

    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {

        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.loadAdditional(tag, registries);

        if (!isCenter()) return;

        if(tag.contains("tournament_uuid") )
            uuid = tag.getUUID("tournament_uuid");

        tournament = NBTCodecHelper.decode(Tournament.CODEC, tag, "tournament");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
