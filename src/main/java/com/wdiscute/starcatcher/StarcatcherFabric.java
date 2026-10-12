package com.wdiscute.starcatcher;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.event.SBevents;
import com.wdiscute.starcatcher.event.SCEvents;
import com.wdiscute.utils.Utils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.util.TriState;
import net.mcexpanded.fancytabsections.FTSInternal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.InteractionResult;
import com.wdiscute.starcatcher.neoforged.bus.api.IEventBus;
import com.wdiscute.starcatcher.neoforged.fml.ModContainer;
import com.wdiscute.starcatcher.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.attachment.AttachmentHandler;
import com.wdiscute.starcatcher.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.common.loot.GlobalLootModifierManager;
import com.wdiscute.starcatcher.neoforged.neoforge.event.*;
import com.wdiscute.starcatcher.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.entity.player.PlayerEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.server.ServerStartedEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.server.ServerStoppingEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.event.tick.ServerTickEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.DataPackRegistryEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredRegister;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.NewRegistryEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps.DataMapManager;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import com.wdiscute.starcatcher.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fabric entrypoint for the ported Starcatcher mod. This class replaces the
 * NeoForge mod-construction + event-bus wiring, calling the existing event
 * methods directly so the ported code paths stay identical.
 */
public class StarcatcherFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        // ---- shim infrastructure (payload types + listeners must exist before
        //      any sync path can run) ----
        AttachmentHandler.init();
        DataMapManager.init();
        GlobalLootModifierManager.init();

        // ---- run the @Mod constructors: creates custom registries (<clinit>)
        //      and flushes every DeferredRegister ----
        new Starcatcher(IEventBus.NOOP, new ModContainer(Starcatcher.MOD_ID));
        new Tooltips(IEventBus.NOOP, new ModContainer(Tooltips.MOD_ID));
        new Utils();
        new SellingBin(IEventBus.NOOP, new ModContainer(SellingBin.MOD_ID));
        new FTSInternal(IEventBus.NOOP, new ModContainer("fancytabsections"));

        // NeoForge fires RegisterEvents per-registry in name order after all mod
        // constructors — every DeferredRegister above queued its suppliers
        DeferredRegister.flushAll();

        // ---- direct calls replacing NeoForge mod-bus events ----
        SCEvents.addRegistry(new NewRegistryEvent());
        SBevents.addRegistry(new NewRegistryEvent());
        SCEvents.addDatapackRegistry(new DataPackRegistryEvent.NewRegistry());
        SCEvents.registerDataMaps(new RegisterDataMapTypesEvent());
        SBevents.registerAttributed(new RegisterDataMapTypesEvent());

        SCEvents.addCapabilities(new RegisterCapabilitiesEvent());
        SBevents.addCapabilities(new RegisterCapabilitiesEvent());

        SCEvents.addPackFinders(new AddPackFindersEvent());
        SBevents.addPackFinders(new AddPackFindersEvent());

        SCEvents.entityAttributes(new EntityAttributeCreationEvent());
        SCEvents.modifyDefaultAttributes(new EntityAttributeModificationEvent());
        EntityAttributeModificationEvent.apply();
        SCEvents.commonSetup(new FMLCommonSetupEvent());

        SCEvents.registerPayloads(new RegisterPayloadHandlersEvent());
        Utils.Events.registerPayloads(new RegisterPayloadHandlersEvent());

        // ---- server reload listeners ----
        AddReloadListenerEvent reloadEvent = new AddReloadListenerEvent();
        Utils.Events.registerReloadListeners(reloadEvent);
        AtomicInteger reloadCounter = new AtomicInteger();
        for (PreparableReloadListener listener : reloadEvent.getListeners())
        {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    "starcatcher", "reload_" + reloadCounter.getAndIncrement());
            ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
                    wrap(id, listener));
        }

        // ---- commands ----
        registerEnumArgument();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                SCEvents.addCommand(new RegisterCommandsEvent(dispatcher, registryAccess, environment)));

        // ---- server lifecycle ----
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            ServerLifecycleHooks.setCurrentServer(server);
            SCEvents.serverStarted(new ServerStartedEvent(server));
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server ->
                SCEvents.serverStopping(new ServerStoppingEvent(server)));
        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
                ServerLifecycleHooks.setCurrentServer(null));

        // ---- server tick ----
        ServerTickEvents.END_SERVER_TICK.register(server ->
                SCEvents.levelTick(new ServerTickEvent.Post(server)));

        // ---- player login ----
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            SCEvents.onPlayerLoggedIn(new PlayerEvent.PlayerLoggedInEvent(handler.getPlayer()));
            Utils.Events.playerLoggedInEvent(new PlayerEvent.PlayerLoggedInEvent(handler.getPlayer()));
        });

        // ---- tags loaded (replaces TagsUpdatedEvent) ----
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) ->
        {
            TagsUpdatedEvent event = new TagsUpdatedEvent(registries);
            Utils.Events.tagsUpdatedEvent(event);
            FTSInternal.tagsUpdatedEvent(event);
        });

        // ---- right click block (worm bonemeal interaction) ----
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) ->
        {
            if (level.isClientSide) return InteractionResult.PASS;
            PlayerInteractEvent.RightClickBlock event =
                    new PlayerInteractEvent.RightClickBlock(player, hand, hitResult.getBlockPos(), hitResult);
            SCEvents.dropWormsWhenBonemealing(event);
            return event.isCanceled() ? InteractionResult.FAIL : InteractionResult.PASS;
        });

        // ---- creative tabs ----
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) ->
                SBevents.addCreative(new BuildCreativeModeTabContentsEvent(tab, entries)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerEnumArgument()
    {
        net.minecraft.commands.synchronization.ArgumentTypeInfos.register(
                net.minecraft.core.registries.BuiltInRegistries.COMMAND_ARGUMENT_TYPE,
                "starcatcher:enum",
                (Class) com.wdiscute.starcatcher.neoforged.neoforge.server.command.EnumArgument.class,
                new com.wdiscute.starcatcher.neoforged.neoforge.server.command.EnumArgument.EnumArgumentInfo());
    }

    private static IdentifiableResourceReloadListener wrap(ResourceLocation id, PreparableReloadListener listener)
    {
        return new IdentifiableResourceReloadListener()
        {
            @Override
            public ResourceLocation getFabricId()
            {
                return id;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier barrier,
                                                  net.minecraft.server.packs.resources.ResourceManager manager,
                                                  net.minecraft.util.profiling.ProfilerFiller prepProfiler,
                                                  net.minecraft.util.profiling.ProfilerFiller reloadProfiler,
                                                  Executor backgroundExecutor,
                                                  Executor gameExecutor)
            {
                return listener.reload(barrier, manager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        };
    }
}
