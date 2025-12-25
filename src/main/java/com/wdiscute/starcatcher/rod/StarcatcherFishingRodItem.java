package com.wdiscute.starcatcher.rod;

import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.attachments.FishingBobAttachment;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.custom.tackleskin.ModTackleSkins;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarcatcherFishingRodItem extends Item implements MenuProvider
{
    public StarcatcherFishingRodItem()
    {
        super(new Item.Properties()
                .rarity(Rarity.EPIC)
                .fireResistant()
                .stacksTo(1));

                ModDataComponents.registerDefault(this, ModDataComponents.BOBBER, new SingleStackContainer(new ItemStack(ModItems.BOBBER.get())));
                ModDataComponents.registerDefault(this,ModDataComponents.BAIT, SingleStackContainer.EMPTY);
                ModDataComponents.registerDefault(this,ModDataComponents.HOOK, new SingleStackContainer(new ItemStack(ModItems.HOOK.get())));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack is = player.getItemInHand(hand);

        if (!is.is(StarcatcherTags.RODS))
            return InteractionResultHolder.pass(is);

        FishingBobAttachment fishingBobAttachment = ModDataAttachments.get(player, ModDataAttachments.FISHING_BOB);
        if (player.isCrouching() && fishingBobAttachment.isEmpty())
        {
            player.openMenu(this);
            return InteractionResultHolder.success(is);
        }

        if (!(level instanceof ServerLevel serverLevel))
            return InteractionResultHolder.success(player.getItemInHand(hand));


        if (fishingBobAttachment.isEmpty())
        {
            ModTackleSkins.get(player.level(), player.getItemInHand(hand)).onCast(player);

            if (level instanceof ServerLevel)
            {
                //TODO ADD CUSTOM STAT FOR NUMBER OF FISHES CAUGHT TOTAL ON STAT SCREEN

                Entity entity = new FishingBobEntity(level, player, is);
                level.addFreshEntity(entity);
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), entity.getEyeY(), player.getZ()));

                fishingBobAttachment.setUuid(player, entity.getUUID());
                if(ModDataComponents.has(is, ModDataComponents.TACKLE_SKIN))
                    ModDataAttachments.set(entity, ModDataAttachments.TACKLE_SKIN, ModDataComponents.get(is, ModDataComponents.TACKLE_SKIN));
            }
        }
        else
        {
            if (serverLevel.getEntity(fishingBobAttachment.getUuid()) instanceof FishingBobEntity fbe) {
                if (!fbe.checkBiting()) {
                    level.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.FISHING_BOBBER_RETRIEVE,
                            SoundSource.NEUTRAL,
                            1.0F,
                            0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
                    );
                    ModDataAttachments.remove(player, ModDataAttachments.FISHING_BOB);
                    fbe.kill();
                }
            } else {
                ModDataAttachments.remove(player, ModDataAttachments.FISHING_BOB);
            }
        }


        return InteractionResultHolder.success(is);
    }


    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack)
    {
        return itemStack.copy();
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        if (player.getMainHandItem().is(StarcatcherTags.RODS))
            return new FishingRodMenu(i, inventory, player.getMainHandItem());
        else
            return new FishingRodMenu(i, inventory, player.getOffhandItem());
    }
}

