package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.FishCaughtPayload;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AwardOneFish extends Item
{
    public AwardOneFish()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!player.isCreative()) return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        if (level.isClientSide()) return InteractionResultHolder.success(player.getItemInHand(usedHand));

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = new HashMap<>(FishingGuideAttachment.getFishesCaught(player));

        List<FishProperties> fishies = FishProperties.getFPs(level);
        FishProperties fish = fishies.get(level.random.nextInt(fishies.size() - 1));

        if(fish.isPresent())
        {
            if(fish.catchInfo().fish().equals(Items.NETHER_STAR)) return InteractionResultHolder.pass(player.getItemInHand(usedHand));

            //todo fix this awarding repeated entries. It should check which entries the player doesnt have to award a new one instead
            fishesCaught.putIfAbsent(U.getRlFromFp(level, fish), FishCaughtCounter.createHacked());

            if(player instanceof ServerPlayer sp)
            {
                ModNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new FishCaughtPayload(fish, false, 0,0, 0));
            }
        }

        FishingGuideAttachment.setFishesCaught(player, fishesCaught);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


}
