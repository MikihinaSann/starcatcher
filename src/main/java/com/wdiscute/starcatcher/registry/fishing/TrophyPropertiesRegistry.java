package com.wdiscute.starcatcher.registry.fishing;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishing.compat.DGTrophies;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TrophyPropertiesRegistry
{
    public static void register()
    {
        DGTrophies.bootstrap();
    }


    private static final List<Pair<ResourceKey<TrophyProperties>, TrophyProperties>> PROPERTIES = new ArrayList<>();
    private static final List<ResourceKey<TrophyProperties>> COMPAT_KEYS = new ArrayList<>();

    public static void registerConditions(BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        for (ResourceKey<TrophyProperties> compatKey : COMPAT_KEYS)
        {
            consumer.accept(compatKey, new ModLoadedCondition(compatKey.location().getNamespace()));
        }
    }

    private static ResourceKey<TrophyProperties> createKey(TrophyProperties tp)
    {
        return ResourceKey.create(Starcatcher.TROPHY_REGISTRY, tp.fish().getKey().location());
    }

    protected static void registerTrophy(TrophyProperties.Builder builder)
    {
        TrophyProperties entry = builder.build();
        ResourceKey<TrophyProperties> key = createKey(entry);
        PROPERTIES.add(Pair.of(key, entry));
        String namespace = key.location().getNamespace();
        if (!namespace.equals("minecraft") && !namespace.equals("starcatcher"))
            COMPAT_KEYS.add(key);
    }

    @SuppressWarnings("deprecation")
    public static void bootstrap(BootstrapContext<TrophyProperties> context)
    {
        PROPERTIES.forEach(p -> context.register(p.getFirst(), p.getSecond()));
    }
}
