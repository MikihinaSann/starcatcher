package net.neoforged.fml;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Fabric port shim of NeoForge's ModContainer. Only the config/extension-point
 * surface used by this codebase is implemented.
 */
public class ModContainer
{
    private final String modId;
    private final IEventBus eventBus = IEventBus.NOOP;

    public ModContainer(String modId)
    {
        this.modId = modId;
    }

    public String getModId()
    {
        return modId;
    }

    public IEventBus getEventBus()
    {
        return eventBus;
    }

    public void registerConfig(ModConfig.Type type, ModConfigSpec spec)
    {
        fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry.INSTANCE
                .register(modId, type, spec);
    }

    /**
     * NeoForge extension points (e.g. IConfigScreenFactory) have no Fabric
     * equivalent; registration is a no-op here.
     */
    public <T> void registerExtensionPoint(Class<T> point, T factory)
    {
    }
}
