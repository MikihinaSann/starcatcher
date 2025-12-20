package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RevokeAllExtras extends Item
{
    public RevokeAllExtras()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {


        FishingGuideAttachment.getTrophiesCaught(player).keySet().removeIf(loc -> U.getTpFromRl(level, loc).trophyType() == TrophyProperties.TrophyType.EXTRA);

        FishingGuideAttachment.sync(player);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
