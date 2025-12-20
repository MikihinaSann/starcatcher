package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;

public class BaseModifier extends AbstractMinigameModifier
{
    @Override
    public void onMiss()
    {
        super.onMiss();
        //kimbe marker
        instance.kimbeMarkerAlpha = 1;
        //You have to make the actual texture white before trying to recolor like this, dummy
        instance.kimbeMarkerColor = 0xff6767;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();

        //refresh all vanishes
        instance.refreshSweetSpotsAlphas();

        instance.perfectCatch = false;

        instance.consecutiveHits = 0;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        instance.kimbeMarkerAlpha = 1;
        instance.kimbeMarkerColor = 0x2ce17d;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();

        instance.consecutiveHits++;

        return super.onHit(ass);
    }
}
