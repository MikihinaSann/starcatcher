package com.wdiscute.starcatcher.neoforged.neoforge.client.event;

import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public abstract class InputEvent extends Event
{
    public static class Key extends InputEvent
    {
        private final int key;
        private final int scanCode;
        private final int action;
        private final int modifiers;

        public Key(int key, int scanCode, int action, int modifiers)
        {
            this.key = key;
            this.scanCode = scanCode;
            this.action = action;
            this.modifiers = modifiers;
        }

        public int getKey()
        {
            return key;
        }

        public int getScanCode()
        {
            return scanCode;
        }

        public int getAction()
        {
            return action;
        }

        public int getModifiers()
        {
            return modifiers;
        }
    }

    public static class MouseButton extends InputEvent
    {
        private final int button;
        private final int action;
        private final int modifiers;

        public MouseButton(int button, int action, int modifiers)
        {
            this.button = button;
            this.action = action;
            this.modifiers = modifiers;
        }

        public int getButton()
        {
            return button;
        }

        public int getAction()
        {
            return action;
        }

        public int getModifiers()
        {
            return modifiers;
        }
    }
}
