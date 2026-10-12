package com.wdiscute.starcatcher.neoforged.neoforge.attachment;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Fabric port shim of NeoForge's attachment holder. Implemented by Entity via
 * mixin; call sites should go through {@link AttachmentHandler} helpers or cast
 * the entity to this interface.
 */
public interface AttachmentHolder
{
    Map<ResourceLocation, Object> starcatcher$attachments();

    @SuppressWarnings("unchecked")
    default <T> T getData(AttachmentType<T> type)
    {
        ResourceLocation id = AttachmentHandler.idOf(type);
        return (T) starcatcher$attachments().computeIfAbsent(id, k -> type.defaultValueSupplier().get());
    }

    default <T> T getData(Supplier<AttachmentType<T>> type)
    {
        return getData(type.get());
    }

    @SuppressWarnings("unchecked")
    default <T> T setData(AttachmentType<T> type, T data)
    {
        starcatcher$attachments().put(AttachmentHandler.idOf(type), data);
        AttachmentHandler.onSet(this, type, data);
        return data;
    }

    default <T> T setData(Supplier<AttachmentType<T>> type, T data)
    {
        return setData(type.get(), data);
    }

    default <T> boolean hasData(AttachmentType<T> type)
    {
        return starcatcher$attachments().containsKey(AttachmentHandler.idOf(type));
    }

    default <T> boolean hasData(Supplier<AttachmentType<T>> type)
    {
        return hasData(type.get());
    }

    @Nullable
    default <T> T removeData(AttachmentType<T> type)
    {
        @SuppressWarnings("unchecked")
        T removed = (T) starcatcher$attachments().remove(AttachmentHandler.idOf(type));
        AttachmentHandler.onRemove(this, type);
        return removed;
    }

    @Nullable
    default <T> T removeData(Supplier<AttachmentType<T>> type)
    {
        return removeData(type.get());
    }

    default void syncData(AttachmentType<?> type)
    {
        AttachmentHandler.sync(this, type);
    }

    default void syncData(Supplier<? extends AttachmentType<?>> type)
    {
        syncData(type.get());
    }
}
