package com.wdiscute.starcatcher.io;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class StreamNetworkingUtils {

    public static <MSG2> void registerMessage(
            SimpleChannel channel, int index, Class<MSG2> messageType,
            StreamCodec<MSG2> codec,
            BiConsumer<MSG2, Supplier<NetworkEvent.Context>> messageConsumer
    ) {
        channel.registerMessage(index, messageType, codec::encode, codec::decode, messageConsumer);
    }

    public static <MSG3> void registerMessageNoSup(
            SimpleChannel channel, int index, Class<MSG3> messageType,
            StreamCodec<MSG3> codec,
            BiConsumer<MSG3, NetworkEvent.Context> messageConsumer
    ) {
        channel.registerMessage(index, messageType, codec::encode, codec::decode, (msg, ctx) -> messageConsumer.accept(msg, ctx.get()));
    }


}
