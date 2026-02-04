package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.backport.IDatagenConditionsExtension;
import com.wdiscute.starcatcher.registry.fishing.TrophyPropertiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DGTrophyPropertiesProvider extends DatapackBuiltinEntriesProvider implements IDatagenConditionsExtension
{
    static
    {
        TrophyPropertiesRegistry.register(); //register all entries before anything else
    }

    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder().add(Starcatcher.TROPHY_REGISTRY, TrophyPropertiesRegistry::bootstrap);

    public DGTrophyPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, Set.of(
                Starcatcher.MOD_ID,
                "minecraft",
                "aquaculture"
        ));
    }

    @Override
    public void registerConditions(BiConsumer<ResourceKey<?>, ICondition> consumer) {
        addConditions(consumer);
    }

    private static void addConditions(final BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        TrophyPropertiesRegistry.registerConditions(consumer);
    }
}
