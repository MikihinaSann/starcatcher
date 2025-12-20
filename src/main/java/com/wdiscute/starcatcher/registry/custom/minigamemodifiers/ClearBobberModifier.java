package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;

public class ClearBobberModifier extends AbstractMinigameModifier
{
    @Override
    public ActiveSweetSpot onSpotAdded(ActiveSweetSpot ass)
    {
        super.onSpotAdded(ass);
        ass.vanishingRate /= 3;
        return ass;
    }
}
