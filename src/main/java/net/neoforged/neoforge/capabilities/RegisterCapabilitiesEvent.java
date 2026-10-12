package net.neoforged.neoforge.capabilities;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric port shim of NeoForge's RegisterCapabilitiesEvent. Block-entity item
 * handlers are exposed through Fabric's ItemStorage API.
 */
public class RegisterCapabilitiesEvent extends Event
{
    @FunctionalInterface
    public interface BlockEntityCapabilityProvider<T extends BlockEntity>
    {
        @Nullable
        IItemHandler getCapability(T blockEntity, @Nullable Direction side);
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> void registerBlockEntity(Capabilities.ItemHandler.BlockCapabilityMark capability,
                                                          BlockEntityType<T> type,
                                                          BlockEntityCapabilityProvider<T> provider)
    {
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) ->
        {
            IItemHandler handler = provider.getCapability(blockEntity, direction);
            return handler == null ? null : new ItemHandlerStorage(handler);
        }, type);
    }

    public <T extends BlockEntity> void registerBlockEntity(Object capability,
                                                          BlockEntityType<T> type,
                                                          BlockEntityCapabilityProvider<T> provider)
    {
        if (capability instanceof Capabilities.ItemHandler.BlockCapabilityMark)
            registerBlockEntity((Capabilities.ItemHandler.BlockCapabilityMark) capability, type, provider);
    }
}
