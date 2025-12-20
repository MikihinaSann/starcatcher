package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.storage.FishProperties;

public class MossyHookModifier extends AbstractMinigameModifier
{
    @Override
    public void tick()
    {
        super.tick();
        if(tickCount == 1 && instance.fishProperties.rarity() != FishProperties.Rarity.LEGENDARY && instance.fishProperties.rarity() != FishProperties.Rarity.EPIC)
        {
            instance.removeAllSweetSpots();
            instance.pointerBaseSpeed = 12;
            instance.pointerSpeed = 12;
            instance.penalty = 25;

            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.THIN_STEADY_MOSSY));
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.AQUA_10));
        }
    }

    //award treasure if perfect catch and mossy hook
    @Override
    public boolean forceAwardTreasure()
    {
        return instance.perfectCatch && instance.fishProperties.rarity() != FishProperties.Rarity.LEGENDARY && instance.fishProperties.rarity() != FishProperties.Rarity.EPIC;
    }
}


