package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.StreamNetworkingUtils;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.stand.CBStandTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.stand.SBStandTournamentNameChangePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            Starcatcher.rl("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    static int id = 0;



    public static void init() {
        id = 0;

        register(
                FishingStartedPayload.class,
                FishingStartedPayload.STREAM_CODEC,
                FishingStartedPayload::handle
        );

        register(
                FishingCompletedPayload.class,
                FishingCompletedPayload.STREAM_CODEC,
                FishingCompletedPayload::handle
        );

        register(
                FishingCompletedPayload.class,
                FishingCompletedPayload.STREAM_CODEC,
                FishingCompletedPayload::handle
        );

        register(
                FishCaughtPayload.class,
                FishCaughtPayload.STREAM_CODEC,
                FishCaughtPayload::handle
        );

        register(
                FPsSeenPayload.class,
                FPsSeenPayload.STREAM_CODEC,
                FPsSeenPayload::handle
        );

        register(
                CBStandTournamentUpdatePayload.class,
                CBStandTournamentUpdatePayload.STREAM_CODEC,
                CBStandTournamentUpdatePayload::handle
        );

        register(
                SBStandTournamentNameChangePayload.class,
                SBStandTournamentNameChangePayload.STREAM_CODEC,
                SBStandTournamentNameChangePayload::handle
        );

        register(
                CBActiveTournamentUpdatePayload.class,
                CBActiveTournamentUpdatePayload.STREAM_CODEC,
                CBActiveTournamentUpdatePayload::handle
        );
    }

    private static <T> void register(Class<T> msg, StreamCodec<T> streamCodec, BiConsumer<T, Supplier<NetworkEvent.Context>> consumer){
        StreamNetworkingUtils.registerMessage(CHANNEL, id++,
                msg,
                streamCodec,
                consumer
        );

    }

    public static void sendToAllPlayers(Object message){
        CHANNEL.send(PacketDistributor.PLAYER.noArg(), message);
    }

    public static void sendToServer(Object message){
        CHANNEL.sendToServer(message);
    }
    public static void sendToPlayer(Player player, Object message){
     if (!(player instanceof ServerPlayer serverPlayer))  throw new IllegalArgumentException("player is not a server player");

     CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }
}
