package net.neoforged.neoforge.client.event;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.bus.api.Event;

import java.util.function.Function;

public class RegisterClientTooltipComponentFactoriesEvent extends Event
{
    @SuppressWarnings("unchecked")
    public <T extends TooltipComponent> void register(Class<T> type,
            Function<T, ? extends ClientTooltipComponent> factory)
    {
        net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback.EVENT.register(component ->
        {
            if (type.isInstance(component))
                return factory.apply((T) component);
            return null;
        });
    }
}
