package com.wdiscute.starcatcher.data.network.tournament;


import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.Tournament;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record CBFinishedTournamentsListPayload(List<Tournament> list) implements CustomPacketPayload
{
    public CBFinishedTournamentsListPayload
    {
        list = List.copyOf(list);
    }

    public static final Type<CBFinishedTournamentsListPayload> TYPE = new Type<>(Starcatcher.rl("cb_finished_tournaments_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBFinishedTournamentsListPayload> STREAM_CODEC = StreamCodec.composite(
            Tournament.STREAM_CODEC.apply(ByteBufCodecs.list()), CBFinishedTournamentsListPayload::list,
            CBFinishedTournamentsListPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> Client.setFinishedTournaments(list));
    }

    //fabric port: NeoForge strips @OnlyIn methods on dedicated servers; keeping
    //the client refs in a nested class gives the same lazy-loading guarantee
    private static class Client
    {
        static void setFinishedTournaments(List<Tournament> list)
        {
            StandScreen.finishedTournaments = list;
        }
    }

}
