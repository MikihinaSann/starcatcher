package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.ModKeymappings;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event) {
        if (event.getAction() == 0 && event.getKey() == ModKeymappings.EXPAND_TOURNAMENT.getKey().getValue()) {
            TournamentOverlay.isExpanded = !TournamentOverlay.isExpanded;
        }
    }

}
