package com.wdiscute.starcatcher.neoforged.neoforge.client.event;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

import java.util.Map;
import java.util.function.Supplier;

public class EntityRenderersEvent extends Event
{
    public static class RegisterRenderers extends EntityRenderersEvent
    {
        public <T extends net.minecraft.world.entity.Entity> void registerEntityRenderer(
                EntityType<? extends T> type, EntityRendererProvider<T> provider)
        {
            net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(type, provider);
        }

        public <T extends net.minecraft.world.level.block.entity.BlockEntity> void registerBlockEntityRenderer(
                BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> provider)
        {
            net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(type, provider);
        }
    }

    public static class RegisterLayerDefinitions extends EntityRenderersEvent
    {
        public void registerLayerDefinition(ModelLayerLocation location, Supplier<LayerDefinition> supplier)
        {
            net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.registerModelLayer(location, supplier::get);
        }
    }

    public static class AddLayers extends EntityRenderersEvent
    {
        private final Map<PlayerSkin.Model, ? extends net.minecraft.client.renderer.entity.EntityRenderer<? extends Player>> skins;
        private final EntityModelSet entityModels;

        public AddLayers(Map<PlayerSkin.Model, ? extends net.minecraft.client.renderer.entity.EntityRenderer<? extends Player>> skins,
                         EntityModelSet entityModels)
        {
            this.skins = skins;
            this.entityModels = entityModels;
        }

        public Map<PlayerSkin.Model, ? extends net.minecraft.client.renderer.entity.EntityRenderer<? extends Player>> getSkins()
        {
            return skins;
        }

        public net.minecraft.client.renderer.entity.EntityRenderer<? extends Player> getSkin(PlayerSkin.Model model)
        {
            return skins.get(model);
        }

        public EntityModelSet getEntityModels()
        {
            return entityModels;
        }
    }
}
