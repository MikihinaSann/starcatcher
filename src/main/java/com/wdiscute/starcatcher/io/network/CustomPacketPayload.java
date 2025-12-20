package com.wdiscute.starcatcher.io.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface CustomPacketPayload {
    void handle(Supplier<NetworkEvent.Context> ctx);
}
