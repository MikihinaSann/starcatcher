package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public class AwardAllSecrets extends Item
{
    public AwardAllSecrets()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //awards all secrets
        Map<ResourceLocation, Integer> trophies = FishingGuideAttachment.getTrophiesCaught(player);

        level.registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY).forEach(
                tp ->
                {
                    if(tp.trophyType() == TrophyProperties.TrophyType.SECRET)
                        trophies.putIfAbsent(U.getRlFromTp(level, tp), 99);
                });

        FishingGuideAttachment.setTrophiesCaught(player, trophies);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
