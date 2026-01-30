package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.ForgeRegistryHelper;
import com.wdiscute.starcatcher.registry.ModCriterionTriggers;
import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event) {
        ForgeRegistryHelper.getInstance(Starcatcher.SWEET_SPOT_BEHAVIOUR)
                .create(event,
                        (reg) -> Starcatcher.SWEET_SPOT_BEHAVIOUR_REGISTRY = reg,
                        (builder) -> builder.setDefaultKey(Starcatcher.rl("normal"))
                );

        ForgeRegistryHelper.getInstance(Starcatcher.MINIGAME_MODIFIERS)
                .create(event,
                        (reg) -> Starcatcher.MINIGAME_MODIFIERS_REGISTRY = reg,
                        (builder) -> builder.setDefaultKey(Starcatcher.rl("no_flip"))
                );


        ForgeRegistryHelper.getInstance(Starcatcher.CATCH_MODIFIERS)
                .create(event,
                        (reg) -> Starcatcher.CATCH_MODIFIERS_REGISTRY = reg,
                        (builder) -> builder.setDefaultKey(Starcatcher.rl("normal"))
                );

        ForgeRegistryHelper.getInstance(Starcatcher.TACKLE_SKIN)
                .create(event,
                        (reg) -> Starcatcher.TACKLE_SKIN_REGISTRY = reg,
                        (builder) -> builder.setDefaultKey(Starcatcher.rl("pearl"))
                );

    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {

        event.dataPackRegistry(Starcatcher.FISH_REGISTRY, FishProperties.CODEC, FishProperties.CODEC);
        event.dataPackRegistry(Starcatcher.TROPHY_REGISTRY, TrophyProperties.CODEC, TrophyProperties.CODEC);

    }

    @SubscribeEvent
    public static void registerAttributed(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
        ModCriterionTriggers.init();
    }
}
