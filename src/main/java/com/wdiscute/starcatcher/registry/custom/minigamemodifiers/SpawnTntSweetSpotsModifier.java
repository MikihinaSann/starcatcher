package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.storage.FishProperties;

import java.util.Random;

public class SpawnTntSweetSpotsModifier extends AbstractMinigameModifier
{
    private final Random r = new Random();
    float chance;
    int cooldown;

    public SpawnTntSweetSpotsModifier(){
        chance = 0.25f;
        cooldown = 5;
    }

    public SpawnTntSweetSpotsModifier(float chance, int cooldown){
        this.chance = chance;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (tickCount % 4 == 0  &&  r.nextFloat() < chance)
        {
            ActiveSweetSpot activeSweetSpot = new ActiveSweetSpot(instance, FishProperties.SweetSpot.TNT);
            instance.addSweetSpot(activeSweetSpot);
        }
    }


}
