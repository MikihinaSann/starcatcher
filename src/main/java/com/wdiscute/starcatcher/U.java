package com.wdiscute.starcatcher;

import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.registry.ModCriterionTriggers;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.custom.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.custom.tackleskin.ModTackleSkins;
import com.wdiscute.starcatcher.storage.FishProperties;
import com.wdiscute.starcatcher.storage.TrophyProperties;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class U
{
    public static final Random r = new Random();

    public static void spawnFishFromPlayerFishing(ServerPlayer player, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        ServerLevel level = ((ServerLevel) player.level());

        if (ModDataAttachments.get(player, ModDataAttachments.FISHING_BOB).isEmpty()) return;

        Entity levelEntity = level.getEntity(ModDataAttachments.get(player, ModDataAttachments.FISHING_BOB).getUuid());
        if (levelEntity instanceof FishingBobEntity fbe)
        {
            if (time != -1)
            {
                FishProperties fp = fbe.fpToFish;

                ModCriterionTriggers.MINIGAME_COMPLETED.trigger(player, hits, perfectCatch, completedTreasure, time, fp.catchInfo().fish().get().getDefaultInstance()); //we don't create items here?

                //trigger modifiers
                fbe.modifiers.forEach(m -> m.onSuccessfulMinigameCompletion(player, time, completedTreasure, perfectCatch, hits));

                //play sound
                ModTackleSkins.get(level, fbe.rod).onSuccessfulMinigame(player);

                //if should cancel because of modifier, return
                if(fbe.modifiers.stream().anyMatch(m -> m.shouldCancelAfterSuccessfulMinigameCompletion(
                        player, time, completedTreasure, perfectCatch, hits))) return;

                //pick size and weight
                float percentile = 50;
                int size = getRandomSize(fp, percentile);
                int weight = getRandomWeight(fp, percentile);

                //award fish counter
                FishCaughtCounter.awardFishCaughtCounter(fp, player, time, size, weight, percentile, perfectCatch, true);

                //add score to tournaments
                TournamentHandler.addScore(player, fp, perfectCatch, size, weight, percentile);

                //play sound
                Vec3 p = player.position();
                level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_CELEBRATE, SoundSource.AMBIENT, 1, 1);

                //award exp
                int exp = fp.rarity().getXp();

                player.giveExperiencePoints(exp);

                //SPAWN ENTITY if ⏬⏬⏬
                if (fp.catchInfo().alwaysSpawnEntity() ||
                        ModList.get().isLoaded("fishingreal") ||
                        fbe.modifiers.stream().anyMatch(AbstractCatchModifier::forceSpawnEntity))
                {

                    Vec3 objPos = player.position().subtract(fbe.position());

                    double x = objPos.x / 25;
                    double y = objPos.y / 20;
                    double z = objPos.z / 25;

                    x = Mth.clamp(x, -1, 1);
                    y = Mth.clamp(y, -1, 1);
                    z = Mth.clamp(z, -1, 1);

                    x *= 2.5;
                    y *= 2;
                    z *= 2.5;

                    Entity entity = fp.catchInfo().entityToSpawn().get().create(level);

                    if (entity == null)
                    {
                        LogUtils.getLogger().warn("starcatcher doesnt like when the flag or whatever is not enabled");
                        return;
                    }

                    //set fish item if it's a starcatcher fish entity
                    if (entity instanceof FishEntity fe) fe.setFish(getFishedItemstackFromFP(fp, size, weight, percentile));

                    entity.setPos(fbe.position().add(0, 1.2f, 0));

                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    entity.setDeltaMovement(vec3);
                    level.addFreshEntity(entity);
                }
                else
                {
                    //SPAWN ITEMSTACK
                    ItemStack bait = ModDataComponents.get(fbe.rod, ModDataComponents.BAIT).stack().copy();
                    boolean isStarcaught = fp.catchInfo().bucketedFish().get().equals(ModItems.STARCAUGHT_BUCKET.get()) && bait.is(Items.BUCKET);
                    boolean isBucketed = !fp.catchInfo().bucketedFish().get().equals(ModItems.MISSINGNO.get()) && !isStarcaught && bait.is(Items.BUCKET);

                    ItemStack is;
                    //create itemStack
                    if (isBucketed)
                    {
                        is = new ItemStack(fp.catchInfo().bucketedFish().get());
                    }
                    else
                    {
                        //make fish itemstack
                        is = new ItemStack(fp.catchInfo().fish().get());

                        //store size and weight data component
                        ModDataComponents.set(is, ModDataComponents.SIZE_AND_WEIGHT, new SizeAndWeightInstance(size, weight, percentile));

                        //store fp in itemstack for name color change
                        ModDataComponents.set(is, ModDataComponents.FISH_PROPERTIES, fp);

                        //call modify stack on modifiers (split hook behaviour)
                        for (AbstractCatchModifier acm : fbe.modifiers) is = acm.modifyItemStack(is);

                        //set starcaught bucket data stuff
                        if (isStarcaught)
                        {
                            ItemStack starcaughtBucket = new ItemStack(fp.catchInfo().bucketedFish().get());
                            ModDataComponents.set(starcaughtBucket,ModDataComponents.BUCKETED_FISH, new SingleStackContainer(is.copy()));
                            is = starcaughtBucket;
                        }
                    }


                    //make ItemEntities for fish item stack
                    ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, is);

                    //assign delta movement so fish flies towards player
                    double x = Mth.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                    double y = Mth.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                    double z = Mth.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    itemFished.setDeltaMovement(vec3);

                    //add item entity to level
                    level.addFreshEntity(itemFished);
                }

                //spawn treasure item
                if (completedTreasure || fbe.modifiers.stream().anyMatch(m -> m.forceAwardTreasure(fbe, time, completedTreasure, perfectCatch, hits)))
                {
                    ItemStack treasure = new ItemStack(fp.catchInfo().treasure().get());
                    ItemEntity treasureFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, treasure);
                    double x = Mth.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                    double y = Mth.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                    double z = Mth.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    treasureFished.setDeltaMovement(vec3);
                    level.addFreshEntity(treasureFished);
                }

            }
            else
            {
                //if fish minigame failed/canceled
                fbe.modifiers.forEach(AbstractCatchModifier::onFailedMinigame);

                //play sound from tackle skin
                ModTackleSkins.get(level, fbe.rod).onFailedMinigame(player);
            }

            fbe.kill();
        }

        ModDataAttachments.remove(player, ModDataAttachments.FISHING_BOB);
    }

    public static int getRandomSize(FishProperties fp, float percentile)
    {
        percentile = Mth.clamp(percentile, 0.01f, 99.999f);
        percentile = 100 - percentile;
        percentile = percentile / 100;
        float dev = fp.sizeWeight().sizeDeviation() * 2;
        float average = fp.sizeWeight().sizeAverage();

        return (int) (average + percentile * dev - dev / 2);
    }

    public static int getRandomWeight(FishProperties fp, float percentile)
    {
        percentile = Mth.clamp(percentile, 0.01f, 99.999f);
        percentile = 100 - percentile;
        percentile = percentile / 100;
        float dev = fp.sizeWeight().weightDeviation() * 2;
        float average = fp.sizeWeight().weightAverage();

        return (int) (average + percentile * dev - dev / 2);
    }

    public static ItemStack getFishedItemstackFromFP(FishProperties fp, int size, int weight, float percentile)
    {
        ItemStack is = new ItemStack(fp.catchInfo().fish().get());
        ModDataComponents.set(is, ModDataComponents.FISH_PROPERTIES, fp);
        ModDataComponents.set(is, ModDataComponents.SIZE_AND_WEIGHT, new SizeAndWeightInstance(size, weight, percentile));
        return is;
    }

    //List<TrophyProperties> -> List<ResourceLocation>
    public static List<TrophyProperties> getTpsFromRls(Registry<TrophyProperties> registry, List<ResourceLocation> resourceLocations)
    {
        List<TrophyProperties> tps = new ArrayList<>();

        for (ResourceLocation rl : resourceLocations)
        {
            TrophyProperties trophyProperties = registry.get(rl);
            if (trophyProperties != null) tps.add(trophyProperties);
        }
        return tps;
    }

    public static List<TrophyProperties> getTpsFromRls(RegistryAccess registryAccess, List<ResourceLocation> rls)
    {
        return getTpsFromRls(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), rls);
    }

    public static List<TrophyProperties> getTpsFromRls(Level level, List<ResourceLocation> rls)
    {
        return getTpsFromRls(level.registryAccess(), rls);
    }


    //List<FishProperties> -> List<ResourceLocation>
    public static List<ResourceLocation> getRlsFromFps(Registry<FishProperties> registry, List<FishProperties> fishProperties)
    {
        List<ResourceLocation> rls = new ArrayList<>();

        for (FishProperties fp : fishProperties)
        {
            ResourceLocation resourceLocation = registry.getKey(fp);
            if (resourceLocation != null && fp.isPresent()) rls.add(resourceLocation);
        }
        return rls;
    }

    public static List<ResourceLocation> getRlsFromFps(RegistryAccess registryAccess, List<FishProperties> fps)
    {
        return getRlsFromFps(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), fps);
    }

    public static List<ResourceLocation> getRlsFromFps(Level level, List<FishProperties> fps)
    {
        return getRlsFromFps(level.registryAccess(), fps);
    }


    //List<TrophyProperties> -> List<ResourceLocation>
    public static List<ResourceLocation> getRlsFromTps(Registry<TrophyProperties> registry, List<TrophyProperties> trophyProperties)
    {
        List<ResourceLocation> rls = new ArrayList<>();

        for (TrophyProperties tp : trophyProperties)
        {
            ResourceLocation resourceLocation = registry.getKey(tp);
            if (resourceLocation != null) rls.add(resourceLocation);
        }
        return rls;
    }

    public static List<ResourceLocation> getRlsFromTps(RegistryAccess registryAccess, List<TrophyProperties> tps)
    {
        return getRlsFromTps(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), tps);
    }

    public static List<ResourceLocation> getRlsFromTps(Level level, List<TrophyProperties> tps)
    {
        return getRlsFromTps(level.registryAccess(), tps);
    }


    //ResourceLocation -> TrophyProperties
    public static TrophyProperties getTpFromRl(Registry<TrophyProperties> registry, ResourceLocation resourceLocation)
    {
        TrophyProperties tp = registry.get(resourceLocation);
        return tp == null ? TrophyProperties.builder().build() : tp;
    }

    public static TrophyProperties getTpFromRl(RegistryAccess registryAccess, ResourceLocation rl)
    {
        return getTpFromRl(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), rl);
    }

    public static TrophyProperties getTpFromRl(Level level, ResourceLocation rl)
    {
        return getTpFromRl(level.registryAccess(), rl);
    }


    //TrophyProperties -> ResourceLocation
    public static ResourceLocation getRlFromTp(Registry<TrophyProperties> registry, TrophyProperties tp)
    {
        ResourceLocation rl = registry.getKey(tp);
        return rl == null ? Starcatcher.rl("missingno_rl") : rl;
    }

    public static ResourceLocation getRlFromTp(RegistryAccess registryAccess, TrophyProperties tp)
    {
        return getRlFromTp(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), tp);
    }

    public static ResourceLocation getRlFromTp(Level level, TrophyProperties tp)
    {
        return getRlFromTp(level.registryAccess(), tp);
    }


    //List<ResourceLocation> -> List<TrophyProperties>
    public static List<FishProperties> getFpsFromRls(Registry<FishProperties> registry, List<ResourceLocation> resourceLocations)
    {
        List<FishProperties> fps = new ArrayList<>();

        for (ResourceLocation rl : resourceLocations)
        {
            FishProperties fishProperties = registry.get(rl);
            if (fishProperties != null) fps.add(fishProperties);
        }
        return fps;
    }

    public static List<FishProperties> getFpsFromRls(RegistryAccess registryAccess, List<ResourceLocation> rls)
    {
        return getFpsFromRls(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), rls);
    }

    public static List<FishProperties> getFpsFromRls(Level level, List<ResourceLocation> rls)
    {
        return getFpsFromRls(level.registryAccess(), rls);
    }


    //ResourceLocation -> FishProperties
    public static FishProperties getFpFromRl(Registry<FishProperties> registry, ResourceLocation resourceLocation)
    {
        FishProperties fp = registry.get(resourceLocation);
        return fp == null ? FishProperties.builder().build() : fp;
    }

    public static FishProperties getFpFromRl(RegistryAccess registryAccess, ResourceLocation rl)
    {
        return getFpFromRl(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), rl);
    }

    public static FishProperties getFpFromRl(Level level, ResourceLocation rl)
    {
        return getFpFromRl(level.registryAccess(), rl);
    }


    //resource location from fish properties
    public static ResourceLocation getRlFromFp(Registry<FishProperties> registry, FishProperties fp)
    {
        ResourceLocation rl = registry.getKey(fp);
        return rl == null ? Starcatcher.rl("missingno_rl") : rl;
    }

    public static ResourceLocation getRlFromFp(RegistryAccess registryAccess, FishProperties tp)
    {
        return getRlFromFp(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), tp);
    }

    public static ResourceLocation getRlFromFp(Level level, FishProperties tp)
    {
        return getRlFromFp(level.registryAccess(), tp);
    }

    public static String calculateRealLifeTimeFromTicks(long ticks)
    {
        long ticksRemainingToCalculate = ticks / 20;
        String finalString = "";

        //days
        if(ticksRemainingToCalculate > 86400)
        {
            finalString += ticksRemainingToCalculate / 86400 + "d ";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 86400;
        }

        //hours
        if(ticksRemainingToCalculate > 3600)
        {
            finalString += ticksRemainingToCalculate / 3600 + "h ";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 3600;
        }

        //minutes
        if(ticksRemainingToCalculate > 60)
        {
            finalString += ticksRemainingToCalculate / 60 + "m ";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 60;
        }

        //seconds
        if(ticksRemainingToCalculate > 0)
        {
            finalString += ticksRemainingToCalculate + "s";
        }
        return finalString;
    }

    @SafeVarargs
    public static <T> boolean containsAny(List<T> list, T... contains)
    {
        for (T s : contains)
            if (list.contains(s)) return true;

        return false;
    }

    @SafeVarargs
    public static <T> boolean containsAll(List<T> list, T... contains)
    {
        for (T s : contains)
            if (!list.contains(s)) return false;
        return true;
    }

    @SafeVarargs
    public static <T> boolean containsNone(List<T> list, T... contains)
    {
        return !containsAny(list, contains);
    }

    public static ResourceLocation rl(String ns, String path)
    {
        return new ResourceLocation(ns, path);
    }

    public static ResourceLocation locItem(String ns, String path)
    {
        return rl(ns, path);
    }

    public static Supplier<Item> holderItem(RegistryObject<Item> item)
    {
        return Holder.direct(item.get());
    }

    public static Supplier<Item> holderItem(Item item)
    {
        return Holder.direct(item);
    }

    public static Supplier<EntityType<?>> holderEntity(EntityType<?> entityType)
    {
        return Holder.direct(entityType);
    }

    public static ResourceLocation holderEntity(String ns, String path)
    {
        return rl(ns, path);
    }

    public static Holder<EntityType<?>> holderEntity(Supplier<EntityType<FishEntity>> entity)
    {
        return Holder.direct(entity.get());
    }

    public static int intToRed(int packedColor)
    {
        return packedColor >> 16 & 255;
    }

    public static int intToGreen(int packedColor)
    {
        return packedColor >> 8 & 255;
    }

    public static int intToBlue(int packedColor)
    {
        return packedColor & 255;
    }

    public static int sign(float x)
    {
        return x >= 0 ? 1 : -1;
    }
}
