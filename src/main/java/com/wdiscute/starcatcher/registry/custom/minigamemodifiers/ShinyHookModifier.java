package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.storage.FishProperties;

public class ShinyHookModifier extends AbstractMinigameModifier
{
    private int hits = 0;

    @Override
    public void onMiss()
    {
        super.onMiss();
        hits = 0;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        hits++;

        if(hits == 3 && !instance.treasureActive && instance.treasureProgress == 0)
        {
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.TREASURE));
            removed = true;
        }

        return super.onHit(ass);
    }
}
