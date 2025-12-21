package com.wdiscute.starcatcher.guide;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class BetterAbstractWidget extends AbstractWidget {
    public BetterAbstractWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public int getRight(){
        return getX() + getWidth();
    }

    public int getBottom(){
        return getY() + getHeight();
    }
}
