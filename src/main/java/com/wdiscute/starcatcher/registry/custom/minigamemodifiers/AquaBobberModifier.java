package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.storage.FishProperties;

public class AquaBobberModifier extends AbstractMinigameModifier
{
    int numberOfSweetSpotsToAdd;
    public AquaBobberModifier(int numberOfSweetSpotsToAdd)
    {
        this.numberOfSweetSpotsToAdd = numberOfSweetSpotsToAdd;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);

        for (int i = 0; i < numberOfSweetSpotsToAdd; i++)
        {
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.AQUA_10));
        }
    }
}
