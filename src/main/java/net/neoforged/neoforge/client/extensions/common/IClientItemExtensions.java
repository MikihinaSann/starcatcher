package net.neoforged.neoforge.client.extensions.common;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Fabric port shim of NeoForge's IClientItemExtensions. Implementations are
 * associated with items through {@link RegisterClientExtensionsEvent}.
 */
public interface IClientItemExtensions
{
    IClientItemExtensions DEFAULT = new IClientItemExtensions() {};

    Map<Item, IClientItemExtensions> REGISTERED = new IdentityHashMap<>();

    static void register(Item item, IClientItemExtensions extensions)
    {
        REGISTERED.put(item, extensions);
    }

    static IClientItemExtensions of(ItemStack stack)
    {
        return of(stack.getItem());
    }

    static IClientItemExtensions of(Item item)
    {
        return REGISTERED.getOrDefault(item, DEFAULT);
    }

    /**
     * Fabric port marker: when true the entity renders the "hold above head"
     * pose (replaces NeoForge's ArmPose enum extension).
     */
    default boolean isHoldAboveHead()
    {
        return false;
    }

    @Nullable
    default HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack)
    {
        return null;
    }
}
