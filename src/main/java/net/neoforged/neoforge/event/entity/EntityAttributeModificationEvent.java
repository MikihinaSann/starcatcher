package net.neoforged.neoforge.event.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Queued attribute defaults applied to entities on spawn (see the ported
 * entity-modify wiring in SCEvents).
 */
public class EntityAttributeModificationEvent extends Event
{
    public record Modification(EntityType<? extends LivingEntity> type, Holder<Attribute> attribute, double value)
    {
    }

    /**
     * Global list of registered modifications; applied by the Player/attribute
     * mixins in this port.
     */
    public static final List<Modification> REGISTRATIONS = new ArrayList<>();

    private final List<Modification> modifications = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public void add(EntityType<? extends LivingEntity> entityType, Holder<Attribute> attribute)
    {
        add(entityType, attribute, attribute.value().getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    public void add(EntityType<? extends LivingEntity> entityType, Holder<Attribute> attribute, double defaultValue)
    {
        Modification mod = new Modification((EntityType<? extends LivingEntity>) entityType, attribute, defaultValue);
        modifications.add(mod);
        REGISTRATIONS.add(mod);
    }

    public List<Modification> getModifications()
    {
        return modifications;
    }

    /**
     * Fabric port: NeoForge patches each entity type's default supplier; here we
     * rebuild the supplier from {@link DefaultAttributes} with the queued
     * additions and overwrite it through {@link net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry}.
     */
    public static void apply()
    {
        java.util.Map<EntityType<? extends LivingEntity>, List<Modification>> byType = new java.util.LinkedHashMap<>();
        for (Modification mod : REGISTRATIONS)
            byType.computeIfAbsent(mod.type(), t -> new ArrayList<>()).add(mod);

        byType.forEach((type, mods) ->
        {
            net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder =
                    net.minecraft.world.entity.ai.attributes.AttributeSupplier.builder();
            net.minecraft.world.entity.ai.attributes.AttributeSupplier base =
                    net.minecraft.world.entity.ai.attributes.DefaultAttributes.getSupplier(type);
            base.instances.forEach((attribute, instance) -> builder.add(attribute, instance.getBaseValue()));
            for (Modification mod : mods)
                builder.add(mod.attribute(), mod.value());
            net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry.register(
                    (EntityType) type, builder.build());
        });
    }
}
