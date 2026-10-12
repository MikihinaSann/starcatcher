package com.wdiscute.starcatcher.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

// NeoForge port: original wrapped NeoForge's RenderTypeHelper#getFallbackItemRenderType;
// vanilla's equivalent fallback is ItemBlockRenderTypes#getRenderType(ItemStack, boolean).
@Mixin(ItemBlockRenderTypes.class)
public class RenderTypeHelperMixin
{
    @WrapMethod(method = "getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;")
    private static RenderType getFallbackItemRenderType(ItemStack stack, boolean cull, Operation<RenderType> original)
    {
        if (Rarity.isGolden(stack))
        {
            try
            {
                return GoldRenderer.INSTANCE.getOrCreateItem(stack, cull).renderType;
            } catch (Exception e)
            {
                Starcatcher.LOGGER.error(String.valueOf(e));
                e.printStackTrace(); // more robust logging is for pussies
            }
        }
        return original.call(stack, cull);
    }

}
