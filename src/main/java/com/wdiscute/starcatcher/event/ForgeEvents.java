package com.wdiscute.starcatcher.event;


import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.commands.ModCommands;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.TournamentSavedData;
import com.wdiscute.starcatcher.io.attachments.CapabilityType;
import com.wdiscute.starcatcher.io.attachments.DataAttachment;
import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.ModKeymappings;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event) {
        if (event.getAction() == 0 && event.getKey() == ModKeymappings.EXPAND_TOURNAMENT.getKey().getValue()) {
            TournamentOverlay.isExpanded = !TournamentOverlay.isExpanded;
        }
    }

    @SubscribeEvent
    public static void addCommand(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher(), event.getBuildContext());
    }


    @SubscribeEvent
    public static void levelTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            TournamentHandler.tick(event.getServer());
    }


    @SubscribeEvent
    public static void dropWormsWhenBonemealing(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (event.getItemStack().is(Items.BONE_MEAL) && level.getBlockState(event.getPos()).getBlock() instanceof FarmBlock) {
            if (!level.isClientSide && Config.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.get()) {
                ItemStack is;
                float i = level.getRandom().nextFloat();
                if (i < 0.8f)
                    is = new ItemStack(ModItems.WORM.get());
                else if (i < 0.9f)
                    is = new ItemStack(ModItems.ALMIGHTY_WORM.get());
                else
                    is = new ItemStack(ModItems.SEEKING_WORM.get());

                Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5F, 1.01, 0.5F).offsetRandom(level.random, 0.7F);
                ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), is);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);

                level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (event.getEntity() instanceof ServerPlayer player) {
                    player.swing(event.getHand(), true);
                    if (!player.isCreative())
                        event.getItemStack().shrink(1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (Config.GIVE_GUIDE.get() && !FishingGuideAttachment.getReceivedGuide(serverPlayer)) {
                serverPlayer.addItem(new ItemStack(ModItems.GUIDE.get()));
                FishingGuideAttachment.setReceivedGuide(serverPlayer, true);
            }

            DataAttachmentType.DATA_ATTACHMENTS.values().forEach(attachment -> ModDataAttachments.sync(serverPlayer, attachment));
        }
    }


    @SubscribeEvent
    public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((key, type) -> {
            if (type.attachment().getCodec() == null) return;

            LazyOptional<? extends DataAttachment<?>> capability = event.getEntity().getCapability(type.attachment().getCapabilityKey());
            if (!capability.isPresent()) return;


            CompoundTag tag = event.getEntity().getPersistentData().getCompound(key.getPath());
            capability.orElseThrow(IllegalStateException::new).deserializeNBT(tag);
        });
    }

    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((key, type) -> {
            if (type.attachment().getCodec() == null) return;

            LazyOptional<? extends DataAttachment<?>> capability = event.getEntity().getCapability(type.attachment().getCapabilityKey());
            if (!capability.isPresent()) return;

            CompoundTag tag = capability.orElseThrow(IllegalStateException::new).serializeNBT();
            event.getEntity().getPersistentData().put(key.getPath(), tag);
        });
    }


    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            var tournament = TournamentHandler.getTournamentForPlayer(sp);
            if (tournament != null)
                TournamentHandler.sendActiveTournamentUpdateToClient(sp, tournament);
            else
                TournamentHandler.clearTournamentToClient(sp);
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        TournamentHandler.setAll(TournamentSavedData.get(event.getServer().overworld()).getTournaments());
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event) {
        TournamentSavedData.get(event.getServer().overworld()).setTournaments(TournamentHandler.getAll());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        DataAttachmentType.DATA_ATTACHMENTS.forEach((key, type) -> {
            if (!type.attachment().isCopyOnDeath()) return;

            LazyOptional<? extends DataAttachment<?>> capNew = newPlayer.getCapability(type.attachment().getCapabilityKey());
            LazyOptional<? extends DataAttachment<?>> capOld = oldPlayer.getCapability(type.attachment().getCapabilityKey());
            if (!capNew.isPresent() || !capOld.isPresent()) return;

            //avoids messing with the generics
            CompoundTag tag = capOld.orElseThrow(IllegalStateException::new).serializeNBT();
            capNew.orElseThrow(IllegalStateException::new).deserializeNBT(tag);

            ModDataAttachments.sync(newPlayer, type);
        });

        oldPlayer.invalidateCaps();
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        //  System.out.println("attaching capabilities for entity:" + event.getObject().getClass().getName());

        if (event.getObject() instanceof Player) {
            DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                    .filter(type -> type.attachment().getPotentialHolders().contains(CapabilityType.PLAYER))
                    .forEach(type -> event.addCapability(type.name(), type.attachment()));

        }
        if (event.getObject() instanceof LivingEntity) {
            DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                    .filter(type -> type.attachment().getPotentialHolders().contains(CapabilityType.LIVING_ENTITY))
                    .forEach(type -> event.addCapability(type.name(), type.attachment()));
        } else {
            DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                    .filter(type -> type.attachment().getPotentialHolders().contains(CapabilityType.NON_LIVING_ENTITY))
                    .forEach(type -> event.addCapability(type.name(), type.attachment()));
        }


        DataAttachmentType.DATA_ATTACHMENTS.values().stream()
                .filter(type -> type.attachment().getPotentialHolders().stream().anyMatch(type1 -> type1.getWithSubtypes().contains(CapabilityType.ENTITY)))
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((loc, attachments) -> event.register(attachments.attachment().getClass()));
    }


}
