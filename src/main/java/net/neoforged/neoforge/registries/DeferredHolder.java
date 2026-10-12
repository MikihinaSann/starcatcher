package net.neoforged.neoforge.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Fabric port shim of NeoForge's DeferredHolder. Vanilla registries cannot
 * create references ahead of registration the way NeoForge's patched holder
 * factory does, so every {@link Holder} method resolves the bound reference
 * through the backing registry at call time; until the entry is registered the
 * lookup throws the same "unbound" error a vanilla {@code Holder.Reference}
 * would.
 */
public class DeferredHolder<R, T extends R> implements Supplier<T>, Holder<R>
{
    private final ResourceKey<R> key;
    private final Registry<R> registry;
    private final Holder<R> fixed;

    public DeferredHolder(ResourceKey<R> key, Registry<R> registry)
    {
        this.key = key;
        this.registry = registry;
        this.fixed = null;
    }

    public DeferredHolder(ResourceKey<R> key, Holder<R> holder)
    {
        this.key = key;
        this.registry = null;
        this.fixed = holder;
    }

    public static <R> DeferredHolder<R, R> create(ResourceKey<? extends Registry<R>> registryKey, ResourceLocation id)
    {
        @SuppressWarnings("unchecked")
        Registry<R> registry = (Registry<R>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null)
            return new DeferredHolder<>(ResourceKey.create(registryKey, id), (Holder<R>) null);
        return new DeferredHolder<>(ResourceKey.create(registryKey, id), registry);
    }

    private Holder<R> resolve()
    {
        if (fixed != null) return fixed;
        return registry.getHolderOrThrow(key);
    }

    @Override
    public T get()
    {
        //noinspection unchecked
        return (T) resolve().value();
    }

    @Override
    public R value()
    {
        return resolve().value();
    }

    public ResourceKey<R> getKey()
    {
        return key;
    }

    public ResourceLocation getId()
    {
        return key.location();
    }

    public Holder<R> getDelegate()
    {
        return resolve();
    }

    public Optional<Holder<R>> holder()
    {
        return Optional.of(resolve());
    }

    public String getRegisteredName()
    {
        return getId().toString();
    }

    @Override
    public boolean isBound()
    {
        return resolve().isBound();
    }

    @Override
    public boolean is(ResourceLocation location)
    {
        return key.location().equals(location);
    }

    @Override
    public boolean is(ResourceKey<R> resourceKey)
    {
        return key.equals(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate)
    {
        return predicate.test(key);
    }

    @Override
    public boolean is(TagKey<R> tagKey)
    {
        return resolve().is(tagKey);
    }

    @Override
    public boolean is(Holder<R> holder)
    {
        return resolve().is(holder);
    }

    @Override
    public Stream<TagKey<R>> tags()
    {
        return resolve().tags();
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap()
    {
        return Either.left(key);
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey()
    {
        return Optional.of(key);
    }

    @Override
    public Kind kind()
    {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner)
    {
        return resolve().canSerializeIn(owner);
    }
}
