package com.wdiscute.starcatcher.neoforged.neoforge.common.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.LootPool;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.NeoForgeRegistries;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fabric port of NeoForge's global loot modifier runtime. Parses
 * {@code data/<ns>/loot_modifiers/*.json} and injects each decoded modifier as
 * a pool entry into every builtin loot table via {@link LootTableEvents#MODIFY}.
 */
public class GlobalLootModifierManager
{
    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<ResourceLocation, IGlobalLootModifier> MODIFIERS = new LinkedHashMap<>();

    public static final LootPoolEntryType GLM_ENTRY_TYPE =
            new LootPoolEntryType(GlobalLootModifierEntry.CODEC);

    public static void init()
    {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE,
                ResourceLocation.fromNamespaceAndPath("neoforge", "loot_table_id"),
                LootTableIdCondition.TYPE);
        Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE,
                ResourceLocation.fromNamespaceAndPath("neoforge", "global_loot_modifier"),
                GLM_ENTRY_TYPE);

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new Loader());

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) ->
        {
            if (MODIFIERS.isEmpty() || !source.isBuiltin()) return;
            for (IGlobalLootModifier modifier : MODIFIERS.values())
            {
                if (!(modifier instanceof LootModifier lm)) continue;

                //fabric port: vanilla LootContext lacks getQueriedLootTableId, so
                //loot_table_id conditions are checked here at injection time
                boolean tableMatches = java.util.Arrays.stream(lm.conditions())
                        .filter(c -> c instanceof LootTableIdCondition)
                        .allMatch(c -> ((LootTableIdCondition) c).getTargetLootTableId().equals(key.location()));
                if (!tableMatches) continue;

                tableBuilder.pool(LootPool.lootPool()
                        .add(GlobalLootModifierEntry.builder(lm))
                        .build());
            }
        });
    }

    private static class Loader implements SimpleSynchronousResourceReloadListener
    {
        private static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath("neoforge", "global_loot_modifiers");

        @Override
        public ResourceLocation getFabricId()
        {
            return ID;
        }

        @Override
        public void onResourceManagerReload(ResourceManager manager)
        {
            MODIFIERS.clear();

            List<ResourceLocation> ordered = new ArrayList<>();
            manager.getResource(ResourceLocation.fromNamespaceAndPath(
                    "neoforge", "loot_modifiers/global_loot_modifiers.json"))
                    .ifPresent(res ->
                    {
                        try (InputStreamReader reader = new InputStreamReader(res.open()))
                        {
                            JsonElement json = GSON.fromJson(reader, JsonElement.class);
                            json.getAsJsonObject().getAsJsonArray("entries")
                                    .forEach(e -> ordered.add(ResourceLocation.parse(e.getAsString())));
                        }
                        catch (Exception ignored)
                        {
                        }
                    });

            Map<ResourceLocation, List<Resource>> stacks =
                    manager.listResourceStacks("loot_modifiers", rl -> rl.getPath().endsWith(".json"));

            Map<ResourceLocation, IGlobalLootModifier> decoded = new LinkedHashMap<>();
            stacks.forEach((rl, resources) ->
            {
                if (rl.getPath().endsWith("global_loot_modifiers.json") && "neoforge".equals(rl.getNamespace()))
                    return;
                ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(),
                        rl.getPath().substring("loot_modifiers/".length(), rl.getPath().length() - ".json".length()));
                Resource top = resources.get(resources.size() - 1);
                try (InputStreamReader reader = new InputStreamReader(top.open()))
                {
                    JsonElement json = GSON.fromJson(reader, JsonElement.class);
                    IGlobalLootModifier modifier = decode(json);
                    if (modifier != null)
                        decoded.put(modifierId, modifier);
                }
                catch (Exception ignored)
                {
                }
            });

            // index ordering first, remaining afterwards (preserves NeoForge's
            // explicit ordering where present)
            for (ResourceLocation id : ordered)
                if (decoded.containsKey(id))
                    MODIFIERS.put(id, decoded.remove(id));
            MODIFIERS.putAll(decoded);
        }

        private IGlobalLootModifier decode(JsonElement json)
        {
            try
            {
                ResourceLocation typeId = ResourceLocation.parse(json.getAsJsonObject().get("type").getAsString());
                @SuppressWarnings("unchecked")
                MapCodec<? extends IGlobalLootModifier> codec =
                        (MapCodec<? extends IGlobalLootModifier>) NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get(typeId);
                if (codec == null) return null;
                return codec.codec().parse(JsonOps.INSTANCE, json).result().orElse(null);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
}
