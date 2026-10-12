package net.neoforged.neoforge.event;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.Event;

/**
 * Fabric port shim of NeoForge's AddPackFindersEvent. Registers mod-provided
 * datapacks via {@link ResourceManagerHelper#registerBuiltinResourcePack}.
 * The pack is looked up at {@code resourcepacks/<id path>} inside the jar.
 * Position hints are not supported by Fabric and are ignored.
 */
public class AddPackFindersEvent extends Event
{
    public void addPackFinders(ResourceLocation packLocation, PackType packType, Component packDisplayName,
                               PackSource packSource, boolean alwaysActive, Pack.Position position)
    {
        addPackFindersInternal(packLocation, packDisplayName, packSource, alwaysActive);
    }

    public void addPackFinders(ResourceLocation packLocation, PackType packType, Component packDisplayName,
                               PackSource packSource, boolean alwaysActive)
    {
        addPackFindersInternal(packLocation, packDisplayName, packSource, alwaysActive);
    }

    private static void addPackFindersInternal(ResourceLocation packLocation,
                                               Component packDisplayName, PackSource packSource, boolean alwaysActive)
    {
        // compat packs reference content from other mods; NeoForge tolerates the
        // resulting datapack errors, vanilla/Fabric registry loading does not,
        // so a compat/<modid> pack is only offered when <modid> is installed.
        if (packLocation.getPath().startsWith("compat/")
                && !requiredModLoaded(packLocation.getPath().substring("compat/".length())))
            return;

        // vendored code may register packs under a different namespace
        // (e.g. sellingbin) than the actual mod container; fall back to the
        // starcatcher container which owns all bundled resources.
        var container = FabricLoader.getInstance().getModContainer(packLocation.getNamespace())
                .or(() -> FabricLoader.getInstance().getModContainer("starcatcher"))
                .orElse(null);
        if (container == null) return;

        ResourcePackActivationType activation = alwaysActive
                ? ResourcePackActivationType.ALWAYS_ENABLED
                : (packSource.shouldAddAutomatically()
                        ? ResourcePackActivationType.DEFAULT_ENABLED
                        : ResourcePackActivationType.NORMAL);
        ResourceManagerHelper.registerBuiltinResourcePack(packLocation, container, packDisplayName, activation);
    }

    /**
     * Resolves the mod a compat pack belongs to by progressively shortening the
     * name at underscores (e.g. selling_bin_emeralds -> selling_bin).
     */
    private static boolean requiredModLoaded(String name)
    {
        String candidate = name;
        while (!candidate.isEmpty())
        {
            if (FabricLoader.getInstance().isModLoaded(candidate)) return true;
            int idx = candidate.lastIndexOf('_');
            if (idx < 0) return false;
            candidate = candidate.substring(0, idx);
        }
        return false;
    }
}
