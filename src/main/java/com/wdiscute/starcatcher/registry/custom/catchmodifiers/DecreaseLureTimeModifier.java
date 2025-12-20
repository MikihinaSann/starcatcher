package com.wdiscute.starcatcher.registry.custom.catchmodifiers;

public class DecreaseLureTimeModifier extends AbstractCatchModifier
{

    final int decreaseMinTicks;
    final int decreaseMaxTicks;
    final int decreaseRandomness;

    public DecreaseLureTimeModifier(int decreaseMinTicks, int decreaseMaxTicks, int decreaseRandomness)
    {
        this.decreaseMinTicks = decreaseMinTicks;
        this.decreaseMaxTicks = decreaseMaxTicks;
        this.decreaseRandomness = decreaseRandomness;
    }

    @Override
    public int adjustMinTicksToFish(int minTicksToFish)
    {
        return minTicksToFish - decreaseMinTicks;
    }

    @Override
    public int adjustMaxTicksToFish(int maxTicksToFish)
    {
        return maxTicksToFish - decreaseMaxTicks;
    }

    @Override
    public int adjustChanceToFishEachTick(int chanceToFishEachTick)
    {
        return chanceToFishEachTick - decreaseRandomness;
    }
}
