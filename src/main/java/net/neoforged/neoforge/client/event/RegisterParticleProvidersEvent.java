package net.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

public class RegisterParticleProvidersEvent extends Event
{
    public <T extends ParticleOptions> void registerSpriteSet(
            ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> factory)
    {
        ParticleFactoryRegistry.getInstance().register(type,
                (ParticleFactoryRegistry.PendingParticleFactory<T>) factory::apply);
    }

    public <T extends ParticleOptions> void registerSpriteSet(
            DeferredHolder<ParticleType, ParticleType<T>> type,
            Function<SpriteSet, ParticleProvider<T>> factory)
    {
        registerSpriteSet(type.get(), factory);
    }
}
