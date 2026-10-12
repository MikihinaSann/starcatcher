package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.registry.SBMenuTypes;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinScreen;
import com.wdiscute.starcatcher.neoforged.api.distmarker.Dist;
import com.wdiscute.starcatcher.neoforged.bus.api.SubscribeEvent;
import com.wdiscute.starcatcher.neoforged.fml.common.EventBusSubscriber;
import com.wdiscute.starcatcher.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = SellingBin.MOD_ID, value = Dist.CLIENT)
public class SBClientEvents
{
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(SBMenuTypes.SELLING_BIN_MENU.get(), SellingBinScreen::new);
    }
}
