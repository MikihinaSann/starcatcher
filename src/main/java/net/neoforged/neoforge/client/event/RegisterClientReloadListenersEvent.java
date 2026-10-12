package net.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.Event;

public class RegisterClientReloadListenersEvent extends Event
{
    private int counter;

    public void registerReloadListener(ResourceManagerReloadListener listener)
    {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                new net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener()
                {
                    private final ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                            "starcatcher", "listener_" + counter);

                    @Override
                    public ResourceLocation getFabricId()
                    {
                        return id;
                    }

                    @Override
                    public void onResourceManagerReload(net.minecraft.server.packs.resources.ResourceManager manager)
                    {
                        listener.onResourceManagerReload(manager);
                    }
                });
    }
}
