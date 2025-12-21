package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.commands.ModCommands;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.FPsSeenPayload;
import com.wdiscute.starcatcher.io.network.FishCaughtPayload;
import com.wdiscute.starcatcher.io.network.FishingCompletedPayload;
import com.wdiscute.starcatcher.io.network.FishingStartedPayload;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.stand.CBStandTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.stand.SBStandTournamentNameChangePayload;
import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.custom.sweetspotbehaviour.AbstractSweetSpotBehaviour;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.util.Supplier;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents
{

    @SubscribeEvent
    public static void levelTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            TournamentHandler.tick(event.getServer());
    }

    @SubscribeEvent
    public static void addCommand(RegisterCommandsEvent event)
    {
        ModCommands.register(event.getDispatcher(), event.getBuildContext());
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
        }
    }


    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        event.create(RegistryBuilder.of(Starcatcher.SWEET_SPOT_BEHAVIOUR.location())
                .setDefaultKey(Starcatcher.rl("normal")));

        event.create(RegistryBuilder.of(Starcatcher.MINIGAME_MODIFIERS.location())
                .setDefaultKey(Starcatcher.rl("no_flip")));

        event.create(RegistryBuilder.of(Starcatcher.CATCH_MODIFIERS.location())
                .setDefaultKey(Starcatcher.rl("no_flip")));
    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {

        event.dataPackRegistry(Starcatcher.FISH_REGISTRY, FishProperties.CODEC, FishProperties.CODEC);
        event.dataPackRegistry(Starcatcher.TROPHY_REGISTRY, TrophyProperties.CODEC, TrophyProperties.CODEC);

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
    public static void registerAttributed(EntityAttributeCreationEvent event)
    {
        event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
    }
}
