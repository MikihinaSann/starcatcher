package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.advancement.MinigameCompletedTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public interface ModCriterionTriggers {
    //TODO: fix all of this

    MinigameCompletedTrigger MINIGAME_COMPLETED = CriteriaTriggers.register(new MinigameCompletedTrigger());

    static void init() {}
}
