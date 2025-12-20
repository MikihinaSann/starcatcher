package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.storage.FishProperties;

public class LowChanceTreasureSpawnModifier extends AbstractMinigameModifier
{
    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        if (U.r.nextFloat() > 0.975 && instance.progress == 0)
        {
            removed = true;
            ActiveSweetSpot newTreasureSweetSpot = new ActiveSweetSpot(instance, FishProperties.SweetSpot.TREASURE);
            instance.addSweetSpot(newTreasureSweetSpot);
        }

        return super.onHit(ass);
    }
}
