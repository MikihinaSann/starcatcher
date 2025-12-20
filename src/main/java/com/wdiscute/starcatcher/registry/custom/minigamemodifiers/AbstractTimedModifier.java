package com.wdiscute.starcatcher.registry.custom.minigamemodifiers;

public abstract class AbstractTimedModifier extends AbstractMinigameModifier
{
    int length;

    public AbstractTimedModifier(int length){
        this.length = length;
    }

    @Override
    public void tick() {
        if (tickCount >= length) {
            removed = true;
        }
        super.tick();
    }
}
