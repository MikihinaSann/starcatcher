package net.neoforged.neoforge.common.extensions;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

import java.util.function.Consumer;

/**
 * Fabric port shim of NeoForge's IMenuTypeExtension. The extra-data channel is
 * encoded as a raw byte array; servers write it through
 * {@link net.neoforged.neoforge.common.extensions.MenuHelper#encodeExtraData}.
 */
public interface IMenuTypeExtension<T extends AbstractContainerMenu>
{
    static <T extends AbstractContainerMenu> MenuType<T> create(IContainerFactory<T> factory)
    {
        StreamCodec<RegistryFriendlyByteBuf, byte[]> codec =
                ByteBufCodecs.BYTE_ARRAY.mapStream(b -> b);
        return new ExtendedScreenHandlerType<>((syncId, inventory, data) ->
                factory.create(syncId, inventory,
                        new RegistryFriendlyByteBuf(
                                data == null ? Unpooled.EMPTY_BUFFER : Unpooled.wrappedBuffer(data),
                                inventory.player.level().registryAccess())),
                codec);
    }

    static <T extends AbstractContainerMenu> MenuType<T> create(IContainerFactory<T> factory,
                                                                net.minecraft.world.flag.FeatureFlagSet featureFlags)
    {
        return create(factory);
    }
}
