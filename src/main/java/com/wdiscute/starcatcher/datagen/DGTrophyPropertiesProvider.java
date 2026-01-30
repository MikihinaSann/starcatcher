package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishing.TrophyPropertiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DGTrophyPropertiesProvider extends DatapackBuiltinEntriesProvider
{
    static
    {
        TrophyPropertiesRegistry.register(); //register all entries before anything else
    }

    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder().add(Starcatcher.TROPHY_REGISTRY, TrophyPropertiesRegistry::bootstrap);

    public DGTrophyPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, DGTrophyPropertiesProvider::addConditions, Set.of(
                Starcatcher.MOD_ID,
                "minecraft",
                "aquaculture"
        ));
    }

    private static void addConditions(final BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        TrophyPropertiesRegistry.registerConditions(consumer);
    }
}
