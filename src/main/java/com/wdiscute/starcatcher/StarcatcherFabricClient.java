package com.wdiscute.starcatcher;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.event.SBTooltipEvents;
import com.wdiscute.starcatcher.event.SCClientEvents;
import com.wdiscute.starcatcher.event.SCTooltipEvents;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.registry.SCKeymappings;
import com.wdiscute.starcatcher.registry.SCRenderTypes;
import com.wdiscute.starcatcher.tournament.TournamentLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.mcexpanded.fancytabsections.FTSInternal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import com.wdiscute.starcatcher.neoforged.fml.ModContainer;
import com.wdiscute.starcatcher.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.client.event.*;
import com.wdiscute.starcatcher.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.io.IOException;

/**
 * Fabric client entrypoint for the ported Starcatcher mod.
 */
public class StarcatcherFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModContainer container = new ModContainer(Starcatcher.MOD_ID);

        com.wdiscute.starcatcher.neoforged.neoforge.attachment.AttachmentHandler.initClient();

        // vendored client inits
        new Starcatcher.Client(container);
        new Tooltips.Client(new ModContainer(Tooltips.MOD_ID), com.wdiscute.starcatcher.neoforged.bus.api.IEventBus.NOOP);
        new SellingBin.Client(new ModContainer(SellingBin.MOD_ID));
        new FTSInternal.Client(new ModContainer("fancytabsections"));

        // ---- direct calls replacing NeoForge client-bus events ----
        SCClientEvents.onAssetReload(new RegisterClientReloadListenersEvent());
        SCClientEvents.registerBER(new EntityRenderersEvent.RegisterRenderers());
        SCClientEvents.onClientSetup(new FMLClientSetupEvent());
        SCClientEvents.registerGuiLayers(new RegisterGuiLayersEvent());
        SCClientEvents.registerParticleFactories(new RegisterParticleProvidersEvent());
        SCClientEvents.registerScreens(new RegisterMenuScreensEvent());
        SCClientEvents.registerLayers(new EntityRenderersEvent.RegisterLayerDefinitions());
        SCClientEvents.onClientExtensions(new RegisterClientExtensionsEvent());
        SCClientEvents.onRegisterKeyMappings(new RegisterKeyMappingsEvent());
        Tooltips.Client.registerKeybinds(new RegisterKeyMappingsEvent());
        SCClientEvents.onRegisterTooltips(new RegisterClientTooltipComponentFactoriesEvent());

        // player model layers need the EntityRenderDispatcher to be built:
        // run at first client tick once entity renderers exist
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            if (addLayersDone || client.getEntityRenderDispatcher() == null) return;
            addLayersDone = true;
            EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
            SCClientEvents.addLayers(new EntityRenderersEvent.AddLayers(
                    dispatcher.playerRenderers, dispatcher.entityModels));
        });

        // ---- shaders ----
        CoreShaderRegistrationCallback.EVENT.register(context ->
        {
            try
            {
                SCRenderTypes.registerShaders(new RegisterShadersEvent(context));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        // ---- item tooltips ----
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) ->
        {
            // tooltip callbacks also fire on worker threads (creative search
            // index); the handlers below call GLFW input polling which asserts
            // on the render thread
            if (!com.mojang.blaze3d.systems.RenderSystem.isOnRenderThread()) return;

            Player player = Minecraft.getInstance().player;
            com.wdiscute.starcatcher.neoforged.neoforge.event.entity.player.ItemTooltipEvent event =
                    new com.wdiscute.starcatcher.neoforged.neoforge.event.entity.player.ItemTooltipEvent(
                            stack, player, lines, tooltipType, tooltipContext);
            Tooltips.modifyItemTooltip(event);
            SCTooltipEvents.tooltipEvent(event);
            SBTooltipEvents.tooltipEvent(event);
        });

        // ---- key handling: konami easter egg in screens ----
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenKeyboardEvents.afterKeyPress(screen).register((s, key, scanCode, modifiers) ->
                        SCClientEvents.keyPressed(new InputEvent.Key(key, scanCode, 1, modifiers))));

        // ---- keymapping-driven actions (tournament expand + guide open) ----
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (SCKeymappings.EXPAND_TOURNAMENT.consumeClick())
                TournamentLayer.expandedType = TournamentLayer.expandedType.next();

            while (SCKeymappings.OPEN_GUIDE.consumeClick())
                if (SCConfig.ALLOW_GUIDE_KEYBIND.get())
                    client.setScreen(new FishingGuideScreen(BlockPos.ZERO, null));
        });
    }

    private static boolean addLayersDone;
}
