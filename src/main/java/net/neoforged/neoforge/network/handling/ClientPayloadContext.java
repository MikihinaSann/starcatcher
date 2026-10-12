package net.neoforged.neoforge.network.handling;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class ClientPayloadContext implements IPayloadContext
{
    private final net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context ctx;

    public ClientPayloadContext(net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public Player player()
    {
        return ctx.player();
    }

    @Override
    public void enqueueWork(Runnable work)
    {
        ctx.client().execute(work);
    }

    @Override
    public void reply(CustomPacketPayload payload)
    {
        ctx.responseSender().sendPacket(payload);
    }

    @Override
    public void disconnect(net.minecraft.network.chat.Component reason)
    {
        ctx.responseSender().disconnect(reason);
    }
}
