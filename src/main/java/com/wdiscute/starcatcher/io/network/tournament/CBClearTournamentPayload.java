package com.wdiscute.starcatcher.io.network.tournament;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.StreamCodec;
import com.wdiscute.starcatcher.io.network.CustomPacketPayload;
import com.wdiscute.starcatcher.io.network.ToClientPacket;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.network.NetworkEvent;


public record CBClearTournamentPayload(String text) implements ToClientPacket
{
    public static final StreamCodec<CBClearTournamentPayload> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.STRING, CBClearTournamentPayload::text,
            CBClearTournamentPayload::new
    );

    @Override
    public void handleClient(NetworkEvent.Context context, ClientLevel level, LocalPlayer player) {
        TournamentOverlay.clear();
    }
}
