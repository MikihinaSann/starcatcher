package com.wdiscute.starcatcher.fishentity;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FishEntity extends AbstractFish
{
    private static final EntityDataAccessor<ItemStack> FISH_ITEM = SynchedEntityData.defineId(FishEntity.class, EntityDataSerializers.ITEM_STACK);

    public FishEntity(EntityType<? extends FishEntity> entityType, Level level)
    {
        super(entityType, level);
    }

    private boolean shouldDropItem = true;

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return SoundEvents.TROPICAL_FISH_HURT;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FISH_ITEM, ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getPickResult()
    {
        return getFishItem();
    }

    public @NotNull ItemStack getFishItem() {
        return entityData.get(FISH_ITEM);
    }

    @Override
    protected SoundEvent getFlopSound()
    {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0F);
    }

    //1.20 specific fish because of Bucketable#saveDefaultDataToBucketTag :)
    @Override
    public void saveToBucketTag(ItemStack stack)
    {
    }

    @Override
    public void tick()
    {
        super.tick();
        if(getFishItem().isEmpty() && !level().isClientSide)
        {
            shouldDropItem = false;
            List<FishProperties> available = new ArrayList<>();

            for (FishProperties fp : level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
            {
                if (FishProperties.getChance(fp, this, ModItems.ROD.get().getDefaultInstance()) > 0 && fp.catchInfo().fish().is(StarcatcherTags.BUCKETABLE_FISHES)) available.add(fp);
            }

            if(available.isEmpty())
                kill();
            else
            {
                FishProperties fp = available.get(U.r.nextInt(available.size()));
                ItemStack is = new ItemStack(fp.catchInfo().fish());
                setFish(is);
            }
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource)
    {
        super.dropAllDeathLoot(damageSource);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit)
    {
        ItemEntity itementity = this.spawnAtLocation(entityData.get(FISH_ITEM));
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }
    }

    @Override
    protected boolean shouldDropLoot() {
        return super.shouldDropLoot() && shouldDropItem;
    }


    public void setFish(ItemStack is)
    {
        shouldDropItem = true;
        entityData.set(FISH_ITEM, is);
        setCustomName(is.getDisplayName());
    }


    @Override
    public ItemStack getBucketItemStack()
    {
        ItemStack is = new ItemStack(ModItems.STARCAUGHT_BUCKET.get());
        ModDataComponents.set(is, ModDataComponents.BUCKETED_FISH, new SingleStackContainer(getFishItem().copy()));
        return is;
    }
}
