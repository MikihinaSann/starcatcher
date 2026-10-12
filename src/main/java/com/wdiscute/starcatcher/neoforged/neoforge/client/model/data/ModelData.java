package com.wdiscute.starcatcher.neoforged.neoforge.client.model.data;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Fabric port shim of NeoForge's ModelData. Only the read/build surface used
 * by this codebase is implemented.
 */
public final class ModelData
{
    public static final ModelData EMPTY = new ModelData(Map.of());

    private final Map<Property<?>, Object> properties;

    private ModelData(Map<Property<?>, Object> properties)
    {
        this.properties = properties;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T get(Property<T> property)
    {
        return (T) properties.get(property);
    }

    public boolean has(Property<?> property)
    {
        return properties.containsKey(property);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Property<T>
    {
        private final java.util.function.Predicate<Object> validator;

        public Property()
        {
            this(v -> true);
        }

        public Property(java.util.function.Predicate<Object> validator)
        {
            this.validator = validator;
        }
    }

    public static class Builder
    {
        private final ImmutableMap.Builder<Property<?>, Object> properties = ImmutableMap.builder();

        public <T> Builder with(Property<T> property, T value)
        {
            properties.put(property, value);
            return this;
        }

        public ModelData build()
        {
            return new ModelData(properties.build());
        }
    }
}
