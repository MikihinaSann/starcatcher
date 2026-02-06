package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;

public class FlipEveryHitModifier extends AbstractMinigameModifier
{
    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        instance.currentRotation *= -1;
        instance.getActiveSweetSpots().forEach(o -> o.currentRotation *= -1);
        return super.onHit(ass);
    }
}
