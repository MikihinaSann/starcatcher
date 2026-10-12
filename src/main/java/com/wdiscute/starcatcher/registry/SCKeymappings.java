package com.wdiscute.starcatcher.registry;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public interface SCKeymappings
{
    KeyMapping MINIGAME_HIT = new KeyMapping("key.starcatcher.minigame_hit", GLFW.GLFW_KEY_SPACE, "key.category.starcatcher.starcatcher");
    KeyMapping EXPAND_TOURNAMENT = new KeyMapping("key.starcatcher.expand_tournament",
            com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_TAB, "key.category.starcatcher.starcatcher");

    KeyMapping OPEN_GUIDE = new KeyMapping("key.starcatcher.open_guide",
            com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
            com.mojang.blaze3d.platform.InputConstants.UNKNOWN.getValue(), "key.category.starcatcher.starcatcher");
}
