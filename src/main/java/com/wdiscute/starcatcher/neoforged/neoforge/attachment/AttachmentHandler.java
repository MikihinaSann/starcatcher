package com.wdiscute.starcatcher.neoforged.neoforge.attachment;

import com.mojang.serialization.Codec;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Internal wiring for the attachment shim: sync, persistence and copy-on-death.
 */
public class AttachmentHandler
{
    private static final String NBT_KEY = "neoforge:attachments";

    // ---------- static accessors ----------
    // NeoForge patches Entity with getData/setData/hasData/removeData/syncData.
    // On Fabric these live on the mixin-injected AttachmentHolder interface, so
    // call sites route through these statics.

    @SuppressWarnings("unchecked")
    public static <T> T getData(Entity entity, Supplier<AttachmentType<T>> type)
    {
        return ((AttachmentHolder) entity).getData(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getData(Entity entity, AttachmentType<T> type)
    {
        return ((AttachmentHolder) entity).getData(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> void setData(Entity entity, Supplier<AttachmentType<T>> type, T data)
    {
        ((AttachmentHolder) entity).setData(type, data);
    }

    @SuppressWarnings("unchecked")
    public static <T> void setData(Entity entity, AttachmentType<T> type, T data)
    {
        ((AttachmentHolder) entity).setData(type, data);
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean hasData(Entity entity, Supplier<AttachmentType<T>> type)
    {
        return ((AttachmentHolder) entity).hasData(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> void removeData(Entity entity, Supplier<AttachmentType<T>> type)
    {
        ((AttachmentHolder) entity).removeData(type);
    }

    public static <T> void removeData(Entity entity, AttachmentType<T> type)
    {
        ((AttachmentHolder) entity).removeData(type);
    }

    public static <T> void removeData(AttachmentHolder holder, AttachmentType<T> type)
    {
        holder.removeData(type);
    }

    public static <T> void setData(AttachmentHolder holder, AttachmentType<T> type, T data)
    {
        holder.setData(type, data);
    }

    public static <T> T getData(AttachmentHolder holder, AttachmentType<T> type)
    {
        return holder.getData(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> void syncData(AttachmentHolder holder, Supplier<AttachmentType<T>> type)
    {
        AttachmentType<T> t = type.get();
        sync(holder, t);
    }

    public static <T> void syncData(AttachmentHolder holder, AttachmentType<T> type)
    {
        sync(holder, type);
    }

    public static <T> void syncData(Entity entity, Supplier<AttachmentType<T>> type)
    {
        AttachmentType<T> t = type.get();
        sync((AttachmentHolder) entity, t);
    }

    public static void init()
    {
        PayloadTypeRegistry.playS2C().register(AttachmentSyncPayload.TYPE, AttachmentSyncPayload.STREAM_CODEC);

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
        {
            AttachmentHolder oldHolder = (AttachmentHolder) oldPlayer;
            AttachmentHolder newHolder = (AttachmentHolder) newPlayer;
            oldHolder.starcatcher$attachments().forEach((id, value) ->
            {
                AttachmentType<?> type = typeOf(id);
                if (type != null && type.copyOnDeath())
                    newHolder.starcatcher$attachments().put(id, value);
            });
        });

        // send a tracked entity's synced attachments when a player starts tracking it
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) ->
            sendAllToPlayer(trackedEntity, player));

        // players also need their own attachments on login
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
            sendAllToPlayer(handler.getPlayer(), handler.getPlayer()));
    }

    public static void initClient()
    {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        ClientPlayNetworking.registerGlobalReceiver(AttachmentSyncPayload.TYPE, (payload, context) ->
        {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.level == null) return;
            Entity entity = mc.level.getEntity(payload.entityId());
            if (entity instanceof AttachmentHolder holder)
            {
                AttachmentType<?> type = typeOf(payload.attachmentId());
                if (type != null)
                    applySync(holder, type, payload.data(), entity.registryAccess());
            }
        });
    }

    private static void applySync(AttachmentHolder holder, AttachmentType<?> type, byte[] data, net.minecraft.core.RegistryAccess access)
    {
        decode(type, data, access).ifPresent(v -> setRaw(holder, type, v));
    }

    @SuppressWarnings("unchecked")
    private static <T> void setRaw(AttachmentHolder holder, AttachmentType<T> type, Object value)
    {
        holder.starcatcher$attachments().put(idOf(type), (T) value);
    }

    static ResourceLocation idOf(AttachmentType<?> type)
    {
        ResourceLocation id = NeoForgeRegistries.ATTACHMENT_TYPES.getKey(type);
        if (id == null)
            throw new IllegalArgumentException("Unregistered attachment type " + type);
        return id;
    }

    static AttachmentType<?> typeOf(ResourceLocation id)
    {
        return NeoForgeRegistries.ATTACHMENT_TYPES.get(id);
    }

    static void onSet(AttachmentHolder holder, AttachmentType<?> type, Object data)
    {
        sync(holder, type);
    }

    static void onRemove(AttachmentHolder holder, AttachmentType<?> type)
    {
        sync(holder, type);
    }

    public static void sync(AttachmentHolder holder, AttachmentType<?> type)
    {
        if (!(holder instanceof Entity entity)) return;
        if (entity.level().isClientSide) return;
        if (type.syncCodec() == null) return;

        Object value = holder.starcatcher$attachments().get(idOf(type));
        //removed attachments decode as the default value (same observable state as absent)
        if (value == null) value = type.defaultValueSupplier().get();
        AttachmentSyncPayload payload = encode(entity, type, value);
        if (payload == null) return;

        for (ServerPlayer player : PlayerLookup.tracking(entity))
            ServerPlayNetworking.send(player, payload);
        if (entity instanceof ServerPlayer sp)
            ServerPlayNetworking.send(sp, payload);
    }

    private static void sendAllToPlayer(Entity entity, ServerPlayer player)
    {
        if (!(entity instanceof AttachmentHolder holder)) return;
        holder.starcatcher$attachments().forEach((id, value) ->
        {
            AttachmentType<?> type = typeOf(id);
            if (type != null && type.syncCodec() != null)
            {
                AttachmentSyncPayload payload = encode(entity, type, value);
                if (payload != null)
                    ServerPlayNetworking.send(player, payload);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> AttachmentSyncPayload encode(Entity entity, AttachmentType<T> type, Object value)
    {
        try
        {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), entity.registryAccess());
            type.syncCodec().encode(buf, (T) value);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            buf.release();
            return new AttachmentSyncPayload(entity.getId(), idOf(type), data);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> java.util.Optional<T> decode(AttachmentType<T> type, byte[] data, net.minecraft.core.RegistryAccess access)
    {
        try
        {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(data), access);
            T value = type.syncCodec().decode(buf);
            buf.release();
            return java.util.Optional.ofNullable(value);
        }
        catch (Exception e)
        {
            return java.util.Optional.empty();
        }
    }

    // ---------- persistence ----------

    @SuppressWarnings("unchecked")
    public static CompoundTag writeAttachments(AttachmentHolder holder, net.minecraft.core.RegistryAccess access)
    {
        CompoundTag tag = new CompoundTag();
        holder.starcatcher$attachments().forEach((id, value) ->
        {
            AttachmentType<?> type = typeOf(id);
            if (type == null || type.serializer() == null) return;
            try
            {
                Tag encoded = ((Codec<Object>) type.serializer()).encodeStart(access.createSerializationContext(NbtOps.INSTANCE), value)
                        .result().orElse(null);
                if (encoded != null)
                    tag.put(id.toString(), encoded);
            }
            catch (Exception ignored)
            {
            }
        });
        return tag;
    }

    @SuppressWarnings("unchecked")
    public static void readAttachments(AttachmentHolder holder, CompoundTag entityTag, net.minecraft.core.RegistryAccess access)
    {
        if (!entityTag.contains(NBT_KEY, Tag.TAG_COMPOUND)) return;
        CompoundTag tag = entityTag.getCompound(NBT_KEY);
        for (String key : tag.getAllKeys())
        {
            ResourceLocation id = ResourceLocation.tryParse(key);
            if (id == null) continue;
            AttachmentType<?> type = typeOf(id);
            if (type == null || type.serializer() == null) continue;
            try
            {
                ((Codec<Object>) type.serializer()).parse(access.createSerializationContext(NbtOps.INSTANCE), tag.get(key))
                        .result().ifPresent(v -> holder.starcatcher$attachments().put(id, v));
            }
            catch (Exception ignored)
            {
            }
        }
    }

    public static String nbtKey()
    {
        return NBT_KEY;
    }
}
