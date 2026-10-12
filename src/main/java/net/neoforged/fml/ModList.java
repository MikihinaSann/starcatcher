package net.neoforged.fml;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;

/**
 * Fabric port shim backed by {@link FabricLoader}.
 */
public class ModList
{
    private static final ModList INSTANCE = new ModList();

    public static ModList get()
    {
        return INSTANCE;
    }

    public boolean isLoaded(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public Optional<ModContainer> getModContainerById(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId)
                ? Optional.of(new ModContainer(modId))
                : Optional.empty();
    }
}
