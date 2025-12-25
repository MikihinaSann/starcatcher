package com.wdiscute.starcatcher.fishspotter;

import com.wdiscute.starcatcher.minigame.PartialTickHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class LayeredDraw {
    public static final float Z_SEPARATION = 200.0F;
    private static final List<Layer> LAYERS = new ArrayList<>();

    public static void add(LayeredDraw.Layer layer) {
        LAYERS.add(layer);
    }

    public static void renderAll(GuiGraphics guiGraphics, float partialTicks) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        guiGraphics.pose().pushPose();
        renderInner(guiGraphics, PartialTickHelper.INSTANCE.getPartialTicks(level));
        guiGraphics.pose().popPose();
    }

    private static void renderInner(GuiGraphics guiGraphics, float deltaTracker) {
        for (LayeredDraw.Layer layereddraw$layer : LAYERS) {
            layereddraw$layer.render(guiGraphics, deltaTracker);
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface Layer {
        void render(GuiGraphics guiGraphics, float deltaTracker);
    }
}