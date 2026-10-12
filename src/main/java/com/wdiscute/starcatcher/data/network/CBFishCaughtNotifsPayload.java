package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.wdiscute.starcatcher.neoforged.neoforge.network.handling.IPayloadContext;

public record CBFishCaughtNotifsPayload(FishProperties fp, boolean displayToast, float percentile, boolean golden) implements CustomPacketPayload
{

    public static final Type<CBFishCaughtNotifsPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught_toast"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBFishCaughtNotifsPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC,
            CBFishCaughtNotifsPayload::fp,
            ByteBufCodecs.BOOL,
            CBFishCaughtNotifsPayload::displayToast,
            ByteBufCodecs.FLOAT,
            CBFishCaughtNotifsPayload::percentile,
            ByteBufCodecs.BOOL,
            CBFishCaughtNotifsPayload::golden,
            CBFishCaughtNotifsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> Client.handle(this));
    }

    //fabric port: NeoForge strips @OnlyIn methods on dedicated servers; keeping
    //the client refs in a nested class gives the same lazy-loading guarantee
    private static class Client
    {
        static void handle(CBFishCaughtNotifsPayload payload)
        {
            if (payload.fp().hasGuideEntry())
                FishCaughtToast.newFish(payload.fp(), payload.displayToast(), payload.percentile(), payload.golden());
        }
    }
}
