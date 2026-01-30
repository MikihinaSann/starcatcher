package com.wdiscute.starcatcher.bob;

import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.FishingStartedPayload;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.blocks.ModBlocks;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.ModCatchModifiers;
import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.registry.ModParticles;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.ModCatchModifiers;
import com.wdiscute.starcatcher.registry.custom.tackleskin.ModTackleSkins;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FishingBobEntity extends Projectile
{
    private static final Logger log = LoggerFactory.getLogger(FishingBobEntity.class);
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(FishingBobEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> VOID = SynchedEntityData.defineId(FishingBobEntity.class, EntityDataSerializers.BOOLEAN);

    public final Player player;
    private FishHookState currentState;
    public FishProperties fpToFish;
    public ItemStack rod = ItemStack.EMPTY;
    public final List<AbstractCatchModifier> modifiers;

    public boolean survivesLava = false;

    public int minTicksToFish;
    public int maxTicksToFish;
    public int chanceToFishEachTick;

    public int timeBiting;

    public int ticksInFluid;

    boolean voidHook = false;

    enum FishHookState
    {
        FLYING,
        BOBBING,
        BITING,
        FISHING
    }

    //client
    public FishingBobEntity(EntityType<? extends FishingBobEntity> entityType, Level level)
    {
        super(entityType, level);
        this.player = null;
        this.modifiers = ModCatchModifiers.getAllCatchModifiers(level, rod);
        modifiers.forEach(acm -> acm.onAdd(this));
    }

    //server
    public FishingBobEntity(Level level, Player player, ItemStack rod)
    {
        super(ModEntities.FISHING_BOB.get(), level);

        this.setOwner(player);
        this.player = player;
        this.rod = rod;
        this.modifiers = ModCatchModifiers.getAllCatchModifiers(level, rod);
        SingleStackContainer ssc = ModDataComponents.get(rod, ModDataComponents.HOOK);
        voidHook = BuiltInRegistries.ITEM.getKey(ssc.stack().getItem()).equals(U.rl("tide", "void_fishing_hook"));

        entityData.set(VOID, voidHook);

        survivesLava = ModDataComponents.getOrDefault(rod, ModDataComponents.NETHERITE_UPGRADE, false) || ModDataComponents.get(rod, ModDataComponents.HOOK).stack().is(StarcatcherTags.HOOK_SURVIVES_LAVA);

        minTicksToFish = 100;
        maxTicksToFish = 300;
        chanceToFishEachTick = 100;

        //modify base chances
        for (AbstractCatchModifier acm : modifiers)
        {
            minTicksToFish = acm.adjustMinTicksToFish(minTicksToFish);
            maxTicksToFish = acm.adjustMaxTicksToFish(maxTicksToFish);
            chanceToFishEachTick = acm.adjustChanceToFishEachTick(chanceToFishEachTick);
        }

        //trigger onBobSummon
        modifiers.forEach(acm -> acm.onAdd(this));

        float playerXRot = player.getXRot();
        float playerYRot = player.getYRot();
        float f2 = Mth.cos(-playerYRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-playerYRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-playerXRot * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-playerXRot * ((float) Math.PI / 180F));
        double d0 = player.getX() - (double) f3 * 0.3;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double) f2 * 0.3;
        this.moveTo(d0, d1, d2, playerYRot, playerXRot);
        Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0F, 5.0F), -f2);
        double d3 = vec3.length();
        vec3 = vec3.multiply(0.6 / d3 + this.random.triangle(0.5F, 0.0103365), 0.6 / d3 + this.random.triangle(0.5F, 0.0103365), 0.6 / d3 + this.random.triangle(0.5F, 0.0103365));
        this.setDeltaMovement(vec3);
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) 180.0F / (double) (float) Math.PI));
        this.setXRot((float) (Mth.atan2(vec3.y, vec3.horizontalDistance()) * (double) 180.0F / (double) (float) Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        if (!level.isClientSide)
            ModDataAttachments.get(player, ModDataAttachments.FISHING_BOB).setUuid(player, this.getUUID());

        currentState = FishHookState.FLYING;
    }

    public void reel()
    {
        modifiers.forEach(AbstractCatchModifier::onReelStart);

        //server only
        List<FishProperties> available = new ArrayList<>(List.of());

        Map<ResourceLocation, Integer> data = FishingGuideAttachment.getTrophiesCaught(player);

        List<TrophyProperties> trophiesCaught = new ArrayList<>(U.getTpsFromRls(level(), data.keySet().stream().toList()));

        //-1 on the common to account for the default "fish" unfortunately, there's probably a way to fix this
        AtomicReference<TrophyProperties.RarityProgress> all = new AtomicReference<>(TrophyProperties.RarityProgress.fromAttachment(player));
        Map<FishProperties.Rarity, TrophyProperties.RarityProgress> progressMap = new EnumMap<>(Map.of(
                FishProperties.Rarity.COMMON, new TrophyProperties.RarityProgress(0, -1),
                FishProperties.Rarity.UNCOMMON, TrophyProperties.RarityProgress.DEFAULT,
                FishProperties.Rarity.RARE, TrophyProperties.RarityProgress.DEFAULT,
                FishProperties.Rarity.EPIC, TrophyProperties.RarityProgress.DEFAULT,
                FishProperties.Rarity.LEGENDARY, TrophyProperties.RarityProgress.DEFAULT
        ));

        FishingGuideAttachment.getFishesCaught(player).forEach((loc, counter) ->
        {
            all.set(new TrophyProperties.RarityProgress(all.get().total() + counter.count(), all.get().unique()));

            progressMap.computeIfPresent(U.getFpFromRl(level(), loc).rarity(), (r, p) -> new TrophyProperties.RarityProgress(p.total() + counter.count(), p.unique() + 1));
        });

        //check if any trophy can be caught
        e:
        for (TrophyProperties tp : level().registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY))
        {
            //if tp can be caught
            for (FishProperties.Rarity value : FishProperties.Rarity.values())
            {
                if (!check(progressMap.get(value), tp.getProgress(value))) continue e;
            }

            if (check(all.get(), tp.all())
                    && !trophiesCaught.contains(tp)
                    && FishProperties.getChance(tp.fp(), this, rod) > 0
                    && random.nextIntBetweenInclusive(0, 99) < tp.chanceToCatch()
            )
            {

                ItemStack is = new ItemStack(tp.fish().get());

                ModDataComponents.set(is, ModDataComponents.TROPHY, tp);

                Entity itemFished = new ItemEntity(
                        level(), position().x, position().y + 1.2f, position().z, is);

                Vec3 vec3 = new Vec3(
                        Mth.clamp((player.position().x - position().x) / 25, -1, 1),
                        0.7 + Mth.clamp((player.position().y - position().y) / 20, -1, 1),
                        Mth.clamp((player.position().z - position().z) / 25, -1, 1));

                itemFished.setDeltaMovement(vec3);
                level().addFreshEntity(itemFished);

                trophiesCaught.add(tp);

                U.getRlsFromTps(level(), trophiesCaught).forEach(loc -> data.putIfAbsent(loc, 0));

                kill();
                return;
            }
        }

        //trigger modifiers
        modifiers.forEach(AbstractCatchModifier::onReelAfterTreasureCheck);

        //if no trophy is available, get chances of getting each fish
        for (FishProperties fp : Starcatcher.getAllRegistryValues(level(), Starcatcher.FISH_REGISTRY))
        {
            int chance = FishProperties.getChance(fp, this, rod);

            for (int i = 0; i < chance; i++)
            {
                available.add(fp);
            }
        }

        //if no fish is available, reset player fishing data and award nothing
        if (available.isEmpty()) this.kill();

        //trigger modifiers for which fish to get based on available
        for (AbstractCatchModifier acm : modifiers)
        {
            available = acm.modifyAvailablePool(available);
        }

        //get random fish from available pool
        fpToFish = available.get(random.nextInt(available.size()));

        //trigger modifiers for which fish to get based on available
        List<FishProperties> immutableAvailable = available;
        modifiers.forEach(acm -> acm.afterChoosingTheCatch(immutableAvailable));

        //should cancel to prevent normal minigame/item fished (only used for vanilla bobber)
        if (modifiers.stream().anyMatch(AbstractCatchModifier::shouldCancelBeforeSkipsMinigameCheck))
        {
            this.kill();
            return;
        }

        //skips minigame if (skipsminigame() or server config of minigame enabled = false) OR any modifier wants to
        if ((fpToFish.skipMinigame() || !Config.ENABLE_MINIGAME.get())
                || modifiers.stream().anyMatch(m -> m.forceSkipMinigame(Config.ENABLE_MINIGAME.get())))
        {
            U.spawnFishFromPlayerFishing(((ServerPlayer) player), 0, false, false, 0);
        } else
        {
            //otherwise send fishing minigame payload to client
            ModNetworking.sendToPlayer(player, new FishingStartedPayload(fpToFish, rod)
            );
        }
    }

    private boolean shouldStopFishing(Player player)
    {
        if (level().isClientSide) return false;

        //if any modifier wants to stop fishing
        if (modifiers.stream().anyMatch(acm -> acm.shouldStopFishing())) return true;

        boolean holdingRod = player.getMainHandItem().is(StarcatcherTags.RODS)
                || player.getOffhandItem().is(StarcatcherTags.RODS);

        if (!player.isRemoved() && player.isAlive() && holdingRod && !(this.distanceToSqr(player) > 1024))
        {
            return false;
        } else
        {
            this.kill();
            return true;
        }
    }

    @Override
    public boolean fireImmune()
    {
        return survivesLava;
    }

    @Override
    public void lavaHurt()
    {
        super.lavaHurt();
        if (!survivesLava && !level().isClientSide)
        {
            kill();
        }
    }

    @Override
    public void kill()
    {
        ModDataAttachments.remove(player, ModDataAttachments.FISHING_BOB);
        super.kill();
    }

    @Override
    public void tick()
    {
        super.tick();

        voidHook = entityData.get(VOID);

        if (!level().isClientSide)
        {
            if (currentState == FishHookState.FLYING) entityData.set(STATE, 1);
            if (currentState == FishHookState.BOBBING) entityData.set(STATE, 2);
            if (currentState == FishHookState.BITING) entityData.set(STATE, 3);
            if (currentState == FishHookState.FISHING) entityData.set(STATE, 4);
        } else
        {
            if (entityData.get(STATE) == 1) currentState = FishHookState.FLYING;
            if (entityData.get(STATE) == 2) currentState = FishHookState.BOBBING;
            if (entityData.get(STATE) == 3) currentState = FishHookState.BITING;
            if (entityData.get(STATE) == 4) currentState = FishHookState.FISHING;
        }

        Player player = ((Player) this.getOwner());
        if (player == null || this.shouldStopFishing(player))
        {
            this.discard();
            if (player != null) ModDataAttachments.remove(player, ModDataAttachments.FISHING_BOB);
        }

        BlockPos blockpos = this.blockPosition();
        FluidState fluid = this.level().getFluidState(blockpos);
        FluidState fluidBellow = this.level().getFluidState(blockpos.below());

        if (this.currentState == FishHookState.FLYING)
        {
            //set voidhook fishing for overworld/nether/end negative offset (based on tide) and always for any other dimension
            ResourceLocation dim = level().dimension().location();
            if (voidHook && position().y < -71 && dim.equals(Level.OVERWORLD.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;

            if (voidHook && position().y < -5 && dim.equals(Level.NETHER.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;

            if (voidHook && position().y < 50 && dim.equals(Level.END.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;

            if (!dim.equals(Level.OVERWORLD.location()) && !dim.equals(Level.NETHER.location()) && !dim.equals(Level.END.location()))
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;


            if (getDeltaMovement().y < 1.2f)
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.02, 0));

            if (!fluid.isEmpty())
            {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.3, 0.3));
                if (!level().isClientSide) this.currentState = FishHookState.BOBBING;
                return;
            }
        }

        if (this.currentState == FishHookState.BITING)
        {
            timeBiting++;
            for (int i = 0; i < 5; i++)
            {
                if (level().getFluidState(blockpos).is(Fluids.LAVA))
                    level().addParticle(
                            ModParticles.FISHING_BITING_LAVA.get(),
                            position().x + random.nextFloat() - 0.5,
                            position().y + random.nextFloat() * 0.5 - 0.25,
                            position().z + random.nextFloat() - 0.5,
                            0, 0, 0);
                else
                    level().addParticle(
                            ModParticles.FISHING_BITING.get(),
                            position().x + random.nextFloat() - 0.5,
                            position().y + random.nextFloat() * 0.5 - 0.25,
                            position().z + random.nextFloat() - 0.5,
                            0, 0, 0);
            }

            if (timeBiting > 80)
            {
                ModDataAttachments.remove(player, ModDataAttachments.FISHING_BOB);
                ModTackleSkins.get(level(), rod).onMissed(player);

                ItemStack bait = ModDataComponents.getOrDefault(rod, ModDataComponents.BAIT, new SingleStackContainer(ItemStack.EMPTY)).stack();
                if (!bait.is(Items.BUCKET))
                {
                    bait.shrink(1);
                    ModDataComponents.set(rod, ModDataComponents.BAIT, new SingleStackContainer(bait));
                }

                kill();
            }
        } else
        {
            timeBiting = 0;
        }

        //if theres no fluid on block or under, changes to FLYING
        if (fluid.isEmpty() && fluidBellow.isEmpty() && !voidHook)
        {
            if (!level().isClientSide) currentState = FishHookState.FLYING;
        }

        //TODO check for water level instead of just blockstate to make the entity sit better in water
        if (this.currentState == FishHookState.BOBBING || this.currentState == FishHookState.FISHING)
        {
            checkForFish();

            if (!voidHook)
            {
                if (!fluid.isEmpty())
                {
                    setDeltaMovement(this.getDeltaMovement().add(0.0F, 0.01, 0.0F));
                } else
                {
                    if (random.nextFloat() > 0.02)
                    {
                        setDeltaMovement(this.getDeltaMovement().add(0.0F, -0.03, 0.0F));
                    } else
                    {
                        setDeltaMovement(this.getDeltaMovement().add(0.0F, -0.01, 0.0F));
                    }
                }
            } else
            {
                setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y * 0.9, getDeltaMovement().z);
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        //this.updateRotation();

        if (this.onGround() || this.horizontalCollision)
        {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
        this.reapplyPosition();
    }

    public boolean checkBiting()
    {

        if (currentState == FishHookState.BITING)
        {
            currentState = FishHookState.FISHING;
            reel();
            return true;
        } else
        {
            return false;
        }
    }

    private void checkForFish()
    {
        if (!level().isClientSide && currentState == FishHookState.BOBBING)
        {
            ticksInFluid++;
            int i = random.nextInt(chanceToFishEachTick);
            if ((i == 1 || ticksInFluid > maxTicksToFish) && ticksInFluid > minTicksToFish)
            {
                if (Config.SHOW_EXCLAMATION_MARK_PARTICLE.get())
                    ((ServerLevel) level()).sendParticles(
                            ModParticles.FISHING_NOTIFICATION.get(),
                            position().x, position().y + 1, position().z,
                            1, 0, 0, 0, 0);

                this.setPos(position().x, position().y - 0.5f, position().z);
                if (!level().isClientSide) currentState = FishHookState.BITING;
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
        }


    }

    @Override
    public AABB getBoundingBoxForCulling()
    {
        AABB box = new AABB(-10, -10, -10, 10, 10, 10);
        return box.move(position());
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(STATE, 0);
        this.entityData.define(VOID, false);
    }

    public static boolean check(TrophyProperties.RarityProgress current, TrophyProperties.RarityProgress restriction)
    {
        return current.total() >= restriction.total() && current.unique() >= restriction.unique();
    }

}
