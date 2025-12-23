package com.wdiscute.starcatcher.event;


import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.commands.ModCommands;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.attachments.NeoCapability;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class ForgeEvents {

    @SubscribeEvent
    public static void addCommand(RegisterCommandsEvent event)
    {
        ModCommands.register(event.getDispatcher(), event.getBuildContext());
    }


    @SubscribeEvent
    public static void levelTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            TournamentHandler.tick(event.getServer());
    }


    @SubscribeEvent
    public static void dropWormsWhenBonemealing(PlayerInteractEvent.RightClickBlock event)
    {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (event.getItemStack().is(Items.BONE_MEAL) && level.getBlockState(event.getPos()).getBlock() instanceof FarmBlock)
        {
            if (!level.isClientSide && Config.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.get())
            {
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

                if (event.getEntity() instanceof ServerPlayer player)
                {
                    player.swing(event.getHand(), true);
                    if (!player.isCreative())
                        event.getItemStack().shrink(1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(event.getEntity() instanceof ServerPlayer serverPlayer)
        {
            FishingGuideAttachment fishingGuideAttachment = ModDataAttachments.get(serverPlayer, ModDataAttachments.FISHING_GUIDE);

            if(Config.GIVE_GUIDE.get() && !fishingGuideAttachment.receivedGuide)
            {
                serverPlayer.addItem(new ItemStack(ModItems.GUIDE.get()));
                fishingGuideAttachment.receivedGuide = true;
            }

            DataAttachmentType.DATA_ATTACHMENTS.values().forEach(attachment -> ModDataAttachments.sync(serverPlayer, attachment));
        }
    }


    @SubscribeEvent
    public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((key, attachment) -> {
            if (attachment.codec() == null) return;

            CompoundTag tag = event.getEntity().getPersistentData().getCompound(key.getPath());
            ModDataAttachments.get(event.getEntity(), attachment).deserializeNBT(tag);
        });
    }

    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {
        DataAttachmentType.DATA_ATTACHMENTS.forEach((key, attachment) -> {
            if (attachment.codec() == null) return;

            CompoundTag tag = ModDataAttachments.get(event.getEntity(), attachment).serializeNBT();
            event.getEntity().getPersistentData().put(key.getPath(), tag);
        });
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        DataAttachmentType.DATA_ATTACHMENTS.forEach((key, attachment) -> {
            if (!attachment.copyOnDeath()) return;

            //This is hacky but avoids messing with the generics
            var tag = ModDataAttachments.get(oldPlayer, attachment).serializeNBT();
            ModDataAttachments.get(newPlayer, attachment).deserializeNBT(tag);

            ModDataAttachments.sync(newPlayer, attachment);
        });

        oldPlayer.invalidateCaps();
    }

}
