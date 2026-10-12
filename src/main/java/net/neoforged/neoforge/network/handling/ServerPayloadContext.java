package net.neoforged.neoforge.network.handling;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerPayloadContext implements IPayloadContext
{
    private final net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context ctx;

    public ServerPayloadContext(net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context ctx)
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
        ctx.server().execute(work);
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
