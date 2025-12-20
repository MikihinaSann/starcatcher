package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class AwardAllFishes extends Item
{
    public AwardAllFishes()
    {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!player.isCreative())
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        //sets all fps on fishes caught to 1
        Map<ResourceLocation, FishCaughtCounter> fishesCaught = new HashMap<>();

        for (FishProperties fish : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            if(fish.hasGuideEntry())
                fishesCaught.put(U.getRlFromFp(level, fish), FishCaughtCounter.createHacked());
        }

        FishingGuideAttachment.setFishesCaught(player, fishesCaught);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


}
