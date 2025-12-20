package com.wdiscute.starcatcher.event;


import com.wdiscute.starcatcher.Starcatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class ForgeEvents {
}
