package com.wdiscute.starcatcher.neoforged.bus.api;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Fabric port shim of NeoForge's IEventBus. Registration methods are accepted
 * for source compatibility; dispatch happens through explicit calls in this
 * port (see StarcatcherFabric entrypoints), so listeners registered here are
 * stored but not invoked.
 */
public interface IEventBus
{
    IEventBus NOOP = new IEventBus() {};

    default <T extends Event> void addListener(Consumer<T> listener)
    {
    }

    default <T extends Event> void addListener(EventPriority priority, Consumer<T> listener)
    {
    }

    default <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Consumer<T> listener)
    {
    }

    default void register(Object target)
    {
    }

    /**
     * Posts an event to listeners registered via
     * {@link #addListener(Class, Consumer)} and returns it (NeoForge semantics).
     */
    default <T extends Event> T post(T event)
    {
        java.util.List<java.util.function.Consumer<? super Event>> list = LISTENERS.get(event.getClass());
        if (list != null) list.forEach(c -> c.accept(event));
        return event;
    }

    default <T extends Event> void addListener(Class<T> type, Consumer<T> listener)
    {
        LISTENERS.computeIfAbsent(type, k -> new java.util.concurrent.CopyOnWriteArrayList<>())
                .add(new java.util.function.Consumer<Event>()
                {
                    @SuppressWarnings("unchecked")
                    public void accept(Event e) { listener.accept((T) e); }
                });
    }

    java.util.Map<Class<?>, java.util.List<java.util.function.Consumer<? super Event>>> LISTENERS =
            new java.util.concurrent.ConcurrentHashMap<>();

    default void unregister(Object target)
    {
    }
}
