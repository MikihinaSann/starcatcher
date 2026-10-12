package net.neoforged.neoforge.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Fabric port shim of NeoForge's AttachmentType. Behaviour is implemented by
 * {@link net.neoforged.neoforge.attachment.AttachmentHandler} and the Entity
 * mixin in this package.
 */
public class AttachmentType<T>
{
    final Supplier<T> defaultValueSupplier;
    @Nullable final Codec<T> serializer;
    @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec;
    final boolean copyOnDeath;

    private AttachmentType(Builder<T> builder)
    {
        this.defaultValueSupplier = builder.defaultValueSupplier;
        this.serializer = builder.serializer;
        this.syncCodec = builder.syncCodec;
        this.copyOnDeath = builder.copyOnDeath;
    }

    public static <T> Builder<T> builder(Supplier<T> defaultValueSupplier)
    {
        return new Builder<>(defaultValueSupplier);
    }

    public Supplier<T> defaultValueSupplier()
    {
        return defaultValueSupplier;
    }

    @Nullable
    public Codec<T> serializer()
    {
        return serializer;
    }

    @Nullable
    public StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec()
    {
        return syncCodec;
    }

    public boolean copyOnDeath()
    {
        return copyOnDeath;
    }

    public static class Builder<T>
    {
        private final Supplier<T> defaultValueSupplier;
        @Nullable private Codec<T> serializer;
        @Nullable private StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec;
        private boolean copyOnDeath;

        private Builder(Supplier<T> defaultValueSupplier)
        {
            this.defaultValueSupplier = defaultValueSupplier;
        }

        public Builder<T> serialize(Codec<T> codec)
        {
            this.serializer = codec;
            return this;
        }

        public Builder<T> sync(StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
        {
            this.syncCodec = codec;
            return this;
        }

        public Builder<T> copyOnDeath()
        {
            this.copyOnDeath = true;
            return this;
        }

        public AttachmentType<T> build()
        {
            return new AttachmentType<>(this);
        }
    }
}
