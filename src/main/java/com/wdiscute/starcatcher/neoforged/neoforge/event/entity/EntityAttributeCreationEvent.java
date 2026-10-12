package com.wdiscute.starcatcher.neoforged.neoforge.event.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public class EntityAttributeCreationEvent extends Event
{
    @SuppressWarnings("unchecked")
    public void put(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributes)
    {
        FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) entityType, attributes);
    }
}
