package net.neoforged.neoforge.common.extensions;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.function.Consumer;

/**
 * Replaces NeoForge's patched {@code ServerPlayer#openMenu} overloads that pass
 * extra data to the client menu factory. Wraps the provider in an
 * {@link ExtendedScreenHandlerFactory} so Fabric's extended screen packet
 * carries the bytes, matching what {@link IMenuTypeExtension#create} decodes.
 */
public class MenuHelper
{
    public static void openMenu(ServerPlayer player, MenuProvider provider)
    {
        openMenu(player, provider, buf -> {});
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider, BlockPos pos)
    {
        openMenu(player, provider, buf -> buf.writeBlockPos(pos));
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider,
                                Consumer<RegistryFriendlyByteBuf> writer)
    {
        player.openMenu(new ExtendedScreenHandlerFactory<byte[]>()
        {
            @Override
            public byte[] getScreenOpeningData(ServerPlayer p)
            {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(
                        Unpooled.buffer(), p.registryAccess());
                writer.accept(buf);
                byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                buf.release();
                return data;
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player p)
            {
                return provider.createMenu(containerId, inventory, p);
            }

            @Override
            public Component getDisplayName()
            {
                return provider.getDisplayName();
            }
        });
    }
}
