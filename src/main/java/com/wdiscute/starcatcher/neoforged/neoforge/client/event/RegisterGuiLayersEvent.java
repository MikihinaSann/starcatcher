package com.wdiscute.starcatcher.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

/**
 * Fabric port shim of NeoForge's RegisterGuiLayersEvent. Fabric API for 1.21.1
 * only exposes {@link HudRenderCallback} (no named/layered hud elements), so all
 * layers are appended to the global hud callback which renders after vanilla
 * layers. Ordering hints are therefore best-effort.
 */
public class RegisterGuiLayersEvent extends Event
{
    public void registerAboveAll(ResourceLocation name, LayeredDraw.Layer layer)
    {
        HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) ->
                layer.render(guiGraphics, deltaTracker));
    }

    public void registerBelowAll(ResourceLocation name, LayeredDraw.Layer layer)
    {
        registerAboveAll(name, layer);
    }

    public void registerAboveVanilla(ResourceLocation name, LayeredDraw.Layer layer)
    {
        registerAboveAll(name, layer);
    }

    public void registerBelowVanilla(ResourceLocation name, LayeredDraw.Layer layer)
    {
        registerAboveAll(name, layer);
    }

    public void registerAbove(ResourceLocation other, ResourceLocation name, LayeredDraw.Layer layer)
    {
        registerAboveAll(name, layer);
    }

    public void registerBelow(ResourceLocation other, ResourceLocation name, LayeredDraw.Layer layer)
    {
        registerAboveAll(name, layer);
    }

    public void register(ResourceLocation name, LayeredDraw.Layer layer)
    {
        registerAboveAll(name, layer);
    }

    public void wrapLayer(ResourceLocation vanillaLayer,
                          java.util.function.Function<LayeredDraw.Layer, LayeredDraw.Layer> factory)
    {
        // cannot wrap vanilla hud layers without a dedicated mixin; no-op
    }
}
