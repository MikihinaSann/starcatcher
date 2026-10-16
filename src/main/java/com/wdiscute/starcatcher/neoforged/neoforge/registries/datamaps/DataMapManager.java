package com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fabric port shim of NeoForge's data map system. Values are loaded from
 * {@code data/<ns>/data_maps/<registry>/<map>.json}, merged across packs
 * (higher-priority packs win, {@code "replace": true} clears lower data), and
 * synced types are pushed to clients on join and after datapack reload.
 */
public class DataMapManager
{
    /**
     * Replaces NeoForge's patched {@code Holder#getData(DataMapType)}: looks up
     * the data-map value attached to a registry holder.
     */
    public static <R, T> T getData(Holder<R> holder, DataMapType<R, T> type)
    {
        return getValue(holder, type);
    }

    /**
     * Equivalent of NeoForge's {@code Registry#getDataMap(type)}: returns the
     * full direct-entry map (excluding tag entries).
     */
    @SuppressWarnings("unchecked")
    public static <R, T> Map<ResourceKey<R>, T> getDataMap(Registry<R> registry, DataMapType<R, T> type)
    {
        Map<ResourceKey<?>, Object> direct = currentData().get(type);
        return direct == null ? Map.of() : (Map<ResourceKey<R>, T>) (Map<?, ?>) direct;
    }

    private static final Gson GSON = new GsonBuilder().create();
    // keyed by "<registryDir>/<mapId>" — multiple types may share an id
    // (e.g. starcatcher:modifiers on item/enchantment/mob_effect registries)
    private static final Map<String, DataMapType<?, ?>> TYPES = new HashMap<>();
    private static final Map<DataMapType<?, ?>, Map<ResourceKey<?>, Object>> SERVER_DATA = new IdentityHashMap<>();
    private static final Map<DataMapType<?, ?>, List<Map.Entry<TagKey<?>, Object>>> SERVER_TAGS = new IdentityHashMap<>();
    private static final Map<DataMapType<?, ?>, Map<ResourceKey<?>, Object>> CLIENT_DATA = new IdentityHashMap<>();
    private static final Map<DataMapType<?, ?>, List<Map.Entry<TagKey<?>, Object>>> CLIENT_TAGS = new IdentityHashMap<>();
    private static MinecraftServer server;

    private static String typeKey(DataMapType<?, ?> type)
    {
        return type.directory() + "/" + type.id();
    }

    public static void registerType(DataMapType<?, ?> type)
    {
        TYPES.put(typeKey(type), type);
    }

    @Nullable
    public static DataMapType<?, ?> typeOf(String key)
    {
        return TYPES.get(key);
    }

    public static <R, T> T get(Holder<R> holder, DataMapType<R, T> type)
    {
        return getValue(holder, type);
    }

    public static <R, T> T get(Holder<R> holder, com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredHolder<? super DataMapType<?, ?>, DataMapType<R, T>> type)
    {
        return getValue(holder, type.get());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <R, T> T getValue(Holder<R> holder, DataMapType<R, T> type)
    {
        Map<ResourceKey<?>, Object> direct = currentData().get(type);
        if (direct != null)
        {
            ResourceKey<R> key = holder.unwrapKey().orElse(null);
            if (key != null && direct.containsKey(key))
                return (T) direct.get(key);
        }
        List<Map.Entry<TagKey<?>, Object>> tagEntries = currentTags().get(type);
        if (tagEntries != null)
        {
            for (int i = tagEntries.size() - 1; i >= 0; i--)
            {
                Map.Entry<TagKey<?>, Object> e = tagEntries.get(i);
                if (((TagKey<R>) e.getKey()).registry().equals(type.registryKey()) && holder.is((TagKey<R>) e.getKey()))
                    return (T) e.getValue();
            }
        }
        return null;
    }

    private static Map<DataMapType<?, ?>, Map<ResourceKey<?>, Object>> currentData()
    {
        return isRemoteClient() ? CLIENT_DATA : SERVER_DATA;
    }

    private static Map<DataMapType<?, ?>, List<Map.Entry<TagKey<?>, Object>>> currentTags()
    {
        return isRemoteClient() ? CLIENT_TAGS : SERVER_TAGS;
    }

    /**
     * True when running on the client of a remote server: the integrated
     * singleplayer server shares the SERVER maps.
     */
    private static boolean isRemoteClient()
    {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return false;
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        return mc.level != null && !mc.hasSingleplayerServer();
    }

    public static void init()
    {
        PayloadTypeRegistry.playS2C().register(DataMapSyncPayload.TYPE, DataMapSyncPayload.STREAM_CODEC);

        ServerLifecycleEvents.SERVER_STARTED.register(srv -> server = srv);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, srv) -> sendToClient(handler.getPlayer()));

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new Loader());

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            initClientReceiver();
    }

    private static void initClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(DataMapSyncPayload.TYPE, (payload, context) ->
            context.client().execute(() -> applySync(payload)));
    }

    @SuppressWarnings("unchecked")
    private static <R, T> void applySync(DataMapSyncPayload payload)
    {
        CLIENT_DATA.clear();
        CLIENT_TAGS.clear();
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        RegistryAccess access = mc.level != null ? mc.level.registryAccess()
                : RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        payload.values().forEach((typeKey, entries) ->
        {
            DataMapType<?, ?> raw = TYPES.get(typeKey);
            if (raw == null) return;
            DataMapType<R, T> type = (DataMapType<R, T>) raw;
            Map<ResourceKey<?>, Object> direct = new HashMap<>();
            List<Map.Entry<TagKey<?>, Object>> tags = new ArrayList<>();
            entries.forEach((keyStr, bytes) -> decodeValue(type, bytes, access).ifPresent(v ->
            {
                if (keyStr.startsWith("#"))
                {
                    ResourceLocation tagId = ResourceLocation.tryParse(keyStr.substring(1));
                    if (tagId != null)
                        tags.add(Map.entry(TagKey.create(type.registryKey(), tagId), v));
                }
                else
                {
                    ResourceLocation keyId = ResourceLocation.tryParse(keyStr);
                    if (keyId != null)
                        direct.put(ResourceKey.create(type.registryKey(), keyId), v);
                }
            }));
            CLIENT_DATA.put(type, direct);
            CLIENT_TAGS.put(type, tags);
        });
    }

    private static <R, T> java.util.Optional<T> decodeValue(DataMapType<R, T> type, byte[] data, RegistryAccess access)
    {
        try
        {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(data), access);
            T value = type.networkCodec().decode(buf);
            buf.release();
            return java.util.Optional.ofNullable(value);
        }
        catch (Exception e)
        {
            return java.util.Optional.empty();
        }
    }

    private static <R, T> byte[] encodeValue(DataMapType<R, T> type, T value, RegistryAccess access)
    {
        try
        {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), access);
            type.networkCodec().encode(buf, value);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            buf.release();
            return data;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static void sendToClient(ServerPlayer player)
    {
        Map<String, Map<String, byte[]>> values = new HashMap<>();
        RegistryAccess access = player.registryAccess();
        SERVER_DATA.forEach((type, direct) ->
        {
            if (type.networkCodec() == null) return;
            values.put(typeKey(type), encodeEntries(type, direct, SERVER_TAGS.get(type), access));
        });
        if (!values.isEmpty())
            ServerPlayNetworking.send(player, new DataMapSyncPayload(values));
    }

    @SuppressWarnings("unchecked")
    private static <R, T> Map<String, byte[]> encodeEntries(DataMapType<R, T> type,
                                                          Map<ResourceKey<?>, Object> direct,
                                                          List<Map.Entry<TagKey<?>, Object>> tags,
                                                          RegistryAccess access)
    {
        Map<String, byte[]> entries = new HashMap<>();
        if (direct != null)
            direct.forEach((k, v) ->
            {
                byte[] b = encodeValue(type, (T) v, access);
                if (b != null) entries.put(k.location().toString(), b);
            });
        if (tags != null)
            for (Map.Entry<TagKey<?>, Object> e : tags)
            {
                byte[] b = encodeValue(type, (T) e.getValue(), access);
                if (b != null) entries.put("#" + e.getKey().location(), b);
            }
        return entries;
    }

    private static void onServerReloadComplete()
    {
        if (server == null) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers())
            sendToClient(player);
    }

    @SuppressWarnings("unchecked")
    private static <R, T> void merge(DataMapType<R, T> type, boolean replace, Map<String, T> values)
    {
        Map<ResourceKey<?>, Object> direct = SERVER_DATA.computeIfAbsent(type, k -> new HashMap<>());
        List<Map.Entry<TagKey<?>, Object>> tags = SERVER_TAGS.computeIfAbsent(type, k -> new ArrayList<>());
        if (replace)
        {
            direct.clear();
            tags.clear();
        }
        values.forEach((keyStr, value) ->
        {
            if (keyStr.startsWith("#"))
            {
                ResourceLocation tagId = ResourceLocation.tryParse(keyStr.substring(1));
                if (tagId != null)
                {
                    tags.removeIf(e -> e.getKey().location().equals(tagId));
                    tags.add(Map.entry(TagKey.create(type.registryKey(), tagId), value));
                }
            }
            else
            {
                ResourceLocation keyId = ResourceLocation.tryParse(keyStr);
                if (keyId != null)
                    direct.put(ResourceKey.create(type.registryKey(), keyId), value);
            }
        });
    }

    private static class Loader implements SimpleSynchronousResourceReloadListener
    {
        private static final ResourceLocation FABRIC_ID =
                ResourceLocation.fromNamespaceAndPath("neoforge", "data_map_loader");

        @Override
        public ResourceLocation getFabricId()
        {
            return FABRIC_ID;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onResourceManagerReload(ResourceManager manager)
        {
            SERVER_DATA.clear();
            SERVER_TAGS.clear();

            Map<ResourceLocation, List<Resource>> stacks =
                    manager.listResourceStacks("data_maps", rl -> rl.getPath().endsWith(".json"));

            stacks.forEach((rl, resources) ->
            {
                // rl.path = data_maps/<registryDir...>/<mapFile>.json
                String path = rl.getPath();
                String stripped = path.substring("data_maps/".length(), path.length() - ".json".length());
                int slash = stripped.lastIndexOf('/');
                if (slash < 0) return;
                String registryDir = stripped.substring(0, slash);
                String mapName = stripped.substring(slash + 1);
                ResourceLocation mapId = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), mapName);

                DataMapType<Object, Object> type = (DataMapType<Object, Object>) TYPES.get(registryDir + "/" + mapId);
                if (type == null) return;

                // resource stacks are ordered lowest -> highest priority
                for (Resource resource : resources)
                {
                    try (InputStreamReader reader = new InputStreamReader(resource.open()))
                    {
                        JsonElement json = GSON.fromJson(reader, JsonElement.class);
                        var parsed = DataMapFile.codec(type.registryKey(), type.codec()).parse(JsonOps.INSTANCE, json);
                        if (parsed.result().isEmpty())
                            com.mojang.logging.LogUtils.getLogger().error("Failed to parse data map " + rl + ": " + parsed.error().map(Object::toString).orElse("unknown"));
                        parsed.result()
                                .ifPresent(file -> merge(type, file.replace(), file.values()));
                    }
                    catch (Exception e)
                    {
                        com.mojang.logging.LogUtils.getLogger().error("Failed to read data map " + rl, e);
                    }
                }
            });

            onServerReloadComplete();
        }
    }
}
