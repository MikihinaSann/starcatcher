package com.wdiscute.starcatcher.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

import java.util.function.Consumer;

/**
 * Fabric port shim backed by {@link CoreShaderRegistrationCallback}.
 */
public class RegisterShadersEvent extends Event
{
    private final CoreShaderRegistrationCallback.RegistrationContext context;

    public RegisterShadersEvent(CoreShaderRegistrationCallback.RegistrationContext context)
    {
        this.context = context;
    }

    public ResourceProvider getResourceProvider()
    {
        return net.minecraft.client.Minecraft.getInstance().getResourceManager();
    }

    public void registerShader(ShaderInstance shader, Consumer<ShaderInstance> onLoaded) throws java.io.IOException
    {
        context.register(ResourceLocation.parse(shader.getName()), shader.getVertexFormat(), onLoaded);
    }

    /**
     * Fabric port: constructing a plain {@link ShaderInstance} with a namespaced
     * name throws before Fabric's FabricShaderProgram id-rewrite can run, so
     * modded shaders must register through this overload.
     */
    public void registerShader(ResourceLocation id, com.mojang.blaze3d.vertex.VertexFormat format,
                               Consumer<ShaderInstance> onLoaded) throws java.io.IOException
    {
        context.register(id, format, onLoaded);
    }
}
