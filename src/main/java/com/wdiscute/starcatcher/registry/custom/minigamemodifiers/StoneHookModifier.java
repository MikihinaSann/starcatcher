package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;

public class StoneHookModifier extends AbstractMinigameModifier
{
    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        instance.gracePeriod = instance.fishProperties.rarity().getStoneHookGraceTicks();;

        return super.onHit(ass);
    }
}
