package net.neoforged.neoforge.network.handling;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

/**
 * Fabric port shim of NeoForge's IPayloadContext.
 */
public interface IPayloadContext
{
    Player player();

    void enqueueWork(Runnable work);

    default void reply(CustomPacketPayload payload)
    {
    }

    default void disconnect(net.minecraft.network.chat.Component reason)
    {
    }

    default net.minecraft.network.PacketListener listener()
    {
        return null;
    }
}
