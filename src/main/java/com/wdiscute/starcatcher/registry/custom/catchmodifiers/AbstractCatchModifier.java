package com.wdiscute.starcatcher.registry.custom.catchmodifiers;

import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class AbstractCatchModifier
{
    FishingBobEntity instance;

    //server and client
    public void onAdd(FishingBobEntity fishingBobEntity)
    {
        this.instance = fishingBobEntity;
    }

    //server only
    public int adjustMinTicksToFish(int minTicksToFish)
    {
        return minTicksToFish;
    }

    //server only
    public int adjustMaxTicksToFish(int maxTicksToFish)
    {
        return maxTicksToFish;
    }

    //server only
    public int adjustChanceToFishEachTick(int chanceToFishEachTick)
    {
        return chanceToFishEachTick;
    }

    //server only
    public void onReel()
    {

    }

    //server only
    public void onReelStart()
    {

    }

    //server only
    public void onReelAfterTreasureCheck()
    {

    }

    //server only
    public List<FishProperties> modifyAvailablePool(List<FishProperties> available)
    {
        return available;
    }

    //server only
    public void afterChoosingTheCatch(List<FishProperties> immutableAvailable)
    {
    }

    //server only
    public boolean forceSkipMinigame(Boolean enableMinigameConfig)
    {
        return false;
    }

    //server only
    public boolean shouldStopFishing()
    {
        return false;
    }

    //server only
    public boolean forceSpawnEntity()
    {
        return false;
    }

    //server only
    public void onFailedMinigame()
    {
    }

    //server only
    public void onSuccessfulMinigameCompletion(ServerPlayer player, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
    }

    //server only
    public boolean shouldCancelAfterSuccessfulMinigameCompletion(ServerPlayer player, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        return false;
    }

    //server only
    public ItemStack modifyItemStack(ItemStack is)
    {
        return is;
    }

    public boolean shouldCancelBeforeSkipsMinigameCheck()
    {
        return false;
    }

    public boolean forceAwardTreasure(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        return false;
    }
}
