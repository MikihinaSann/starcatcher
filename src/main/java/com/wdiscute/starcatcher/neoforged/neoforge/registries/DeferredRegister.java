package com.wdiscute.starcatcher.neoforged.neoforge.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.wdiscute.starcatcher.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Fabric port shim of NeoForge's DeferredRegister. Entries are registered
 * eagerly when {@link #register(IEventBus)} is called (during ModInitializer),
 * which is safe on Fabric because vanilla registries are unfrozen at that point.
 */
public class DeferredRegister<T>
{
    protected final Registry<T> registry;
    protected final String modid;
    protected final List<Runnable> pending = new ArrayList<>();
    private final List<DeferredHolder<T, ?>> entries = new ArrayList<>();

    public DeferredRegister(Registry<T> registry, String modid)
    {
        this.registry = registry;
        this.modid = modid;
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String modid)
    {
        return new DeferredRegister<>(registry, modid);
    }

    @SuppressWarnings("unchecked")
    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modid)
    {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null)
            throw new IllegalArgumentException("No registry found for key " + registryKey);
        return new DeferredRegister<>(registry, modid);
    }

    public static <T> DeferredRegister<T> createDataComponents(ResourceKey<? extends Registry<T>> registryKey, String modid)
    {
        return create(registryKey, modid);
    }

    public static DeferredRegister.Items createItems(String modid)
    {
        return new DeferredRegister.Items(modid);
    }

    public static DeferredRegister.Blocks createBlocks(String modid)
    {
        return new DeferredRegister.Blocks(modid);
    }

    @SuppressWarnings("unchecked")
    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> sup)
    {
        ResourceKey<T> key = ResourceKey.create(registry.key(), ResourceLocation.fromNamespaceAndPath(modid, name));
        DeferredHolder<T, I> deferred = makeHolder(key);
        pending.add(() -> Registry.register(registry, key, sup.get()));
        entries.add(deferred);
        return deferred;
    }

    @SuppressWarnings("unchecked")
    protected <I extends T> DeferredHolder<T, I> makeHolder(ResourceKey<T> key)
    {
        //fabric port: DeferredHolder resolves through the registry lazily; the
        //entry binds when the pending Registry.register runs
        return new DeferredHolder<>(key, registry);
    }

    private static final List<DeferredRegister<?>> FLUSH_QUEUE = new ArrayList<>();
    private static boolean flushed = false;

    /**
     * Fabric port: NeoForge fires RegisterEvents per-registry in name order, and
     * mod constructors routinely dereference holders from other registries
     * (e.g. an Item capturing a DataComponentType). All registers therefore queue
     * here and are flushed sorted by registry name in {@link #flushAll()}, which
     * the common entrypoint calls once every mod constructor has run.
     */
    public void register(IEventBus bus)
    {
        if (flushed)
        {
            pending.forEach(Runnable::run);
            pending.clear();
            return;
        }
        FLUSH_QUEUE.add(this);
    }

    /**
     * Runs all queued registrations in registry-name order (NeoForge semantics).
     */
    public static void flushAll()
    {
        flushed = true;
        List<DeferredRegister<?>> sorted = new ArrayList<>(FLUSH_QUEUE);
        FLUSH_QUEUE.clear();
        sorted.sort(java.util.Comparator.comparing(d -> d.registry.key().location().toString()));
        for (DeferredRegister<?> d : sorted)
        {
            d.pending.forEach(Runnable::run);
            d.pending.clear();
        }
    }

    public Collection<DeferredHolder<T, ?>> getEntries()
    {
        return Collections.unmodifiableList(entries);
    }

    public static class Items extends DeferredRegister<Item>
    {
        public Items(String modid)
        {
            super(BuiltInRegistries.ITEM, modid);
        }

        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> factory)
        {
            return registerItem(name, factory, new Item.Properties());
        }

        @SuppressWarnings("unchecked")
        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> factory, Item.Properties properties)
        {
            return (DeferredItem<I>) register(name, () -> factory.apply(properties));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> sup)
        {
            return (DeferredItem<I>) super.register(name, sup);
        }

        public DeferredItem<Item> registerSimpleItem(String name)
        {
            return registerItem(name, Item::new);
        }

        public DeferredItem<Item> registerSimpleItem(String name, Item.Properties properties)
        {
            return registerItem(name, Item::new, properties);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <I extends Item> DeferredHolder<Item, I> makeHolder(ResourceKey<Item> key)
        {
            return new DeferredItem<>(key, registry);
        }
    }

    public static class Blocks extends DeferredRegister<Block>
    {
        public Blocks(String modid)
        {
            super(BuiltInRegistries.BLOCK, modid);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <I extends Block> DeferredBlock<I> register(String name, Supplier<? extends I> sup)
        {
            return (DeferredBlock<I>) super.register(name, sup);
        }

        public DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties properties)
        {
            return registerBlock(name, Block::new, properties);
        }

        public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory)
        {
            return registerBlock(name, factory, BlockBehaviour.Properties.of());
        }

        @SuppressWarnings("unchecked")
        public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory, BlockBehaviour.Properties properties)
        {
            return (DeferredBlock<B>) register(name, () -> factory.apply(properties));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <I extends Block> DeferredHolder<Block, I> makeHolder(ResourceKey<Block> key)
        {
            return new DeferredBlock<>(key, registry);
        }
    }
}
