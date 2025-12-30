package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents
{
    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        event.create(RegistryBuilder.of(Starcatcher.SWEET_SPOT_BEHAVIOUR.location())
                .setDefaultKey(Starcatcher.rl("normal")));

        event.create(RegistryBuilder.of(Starcatcher.MINIGAME_MODIFIERS.location())
                .setDefaultKey(Starcatcher.rl("no_flip")));

        event.create(RegistryBuilder.of(Starcatcher.CATCH_MODIFIERS.location())
                .setDefaultKey(Starcatcher.rl("no_flip")));

        event.create(RegistryBuilder.of(Starcatcher.TACKLE_SKIN.location())
                .setDefaultKey(Starcatcher.rl("pearl")));

    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {

        event.dataPackRegistry(Starcatcher.FISH_REGISTRY, FishProperties.CODEC, FishProperties.CODEC);
        event.dataPackRegistry(Starcatcher.TROPHY_REGISTRY, TrophyProperties.CODEC, TrophyProperties.CODEC);

    }

    @SubscribeEvent
    public static void registerAttributed(EntityAttributeCreationEvent event)
    {
        event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
    }

}
