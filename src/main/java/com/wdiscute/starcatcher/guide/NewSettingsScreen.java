package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NewSettingsScreen extends FishingMinigameScreen {
    public static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame/minigame.png");
    public static final ResourceLocation TANK = Starcatcher.rl("textures/gui/minigame/surface.png");
    public static final ResourceLocation SETTINGS = Starcatcher.rl("textures/gui/minigame/settings.png");
    public static final ResourceLocation GUI_SCALE = Starcatcher.rl("textures/gui/minigame/gui_scale.png");

    boolean changeRotation;
    boolean moveMarkers = false;
    boolean isHoveringWidgets = false;

    SettingsScreen.Units unitSelected;

    public NewSettingsScreen(FishProperties fp, ItemStack rod) {
        super(fp, rod);
    }

    @Override
    protected void init() {
        super.init();

        hitDelay = (Config.HIT_DELAY.get().floatValue());
        unitSelected = Config.UNIT.get();

        //Use widgets instead of doing hovering/clicking logic manually
        addRenderableWidget(new GuiScaleWidget(width / 2 - 50, 0, 100, 50));

        //hit delay
        addRenderableWidget(new LeftRightButtonWidget(
                () -> hitDelay, // the value to render
                () -> hitDelay -= 0.2f, // left button action
                () -> hitDelay += 0.2f, // right button action
                Component.literal("Hit Delay"),
                width / 2 + 100, height / 2 - 40, 91, 19, 160, 69, 256, 256, SETTINGS, 13));

        //Speed
        addRenderableWidget(new LeftRightButtonWidget(
                () -> pointerSpeed, // the value to render
                () -> pointerSpeed -= 0.1f, // left button action
                () -> pointerSpeed += 0.1f, // right button action
                Component.literal("Speed"),
                width / 2 + 100, height / 2 + 10, 91, 19, 160, 69, 256, 256, SETTINGS, 13));


        //Units
        addRenderableWidget(new LeftRightButtonWidget(
                () -> unitSelected, // the value to render
                () -> unitSelected = unitSelected.previous(), // left button action
                () -> unitSelected = unitSelected.next(), // right button action
                Component.literal("Units"),
                width / 2, height / 2 + 80, 136, 25, 34, 222, 256, 256, SETTINGS, 16));


        //TODO: make a boolean / buttonList widget for the little buttons
    }

    public Options getOptions() {
        return getMinecraft().options;
    }

    private @NotNull OptionInstance<Integer> guiScale() {
        return getOptions().guiScale();
    }

    @Override
    public boolean isSettingsScreen() {
        return true;
    }

    @Override
    public void inputPressed() {
        if (!(isHoldingMouse && children().stream().anyMatch(GuiEventListener::isFocused)))
            super.inputPressed();

        if (progress > 100) progress = 100;
        if (progress < 0 ) progress = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (progress > 100) progress = 100;
        if (progress < 0 ) progress = 0;
    }

    @Override
    public void onClose() {
        //round it to 2 decimal points
        Config.HIT_DELAY.set(Math.round(hitDelay * 10) / 10d);
        Config.HIT_DELAY.save();

        Config.MINIGAME_GUI_SCALE.set(Minecraft.getInstance().options.guiScale().get());
        Config.MINIGAME_GUI_SCALE.save();

        Config.UNIT.set(unitSelected);
        Config.UNIT.save();

        modifiers.forEach(AbstractMinigameModifier::onRemove);

        if (!hasDistantHorizons())
            Minecraft.getInstance().options.guiScale().set(previousGuiScale);

        this.minecraft.popGuiLayer();
    }

    public class LeftRightButtonWidget extends AbstractWidget {
        int uOffset, vOffset, textureWidth, textureHeight, buttonWidth;
        ResourceLocation texture;
        Supplier<?> value;
        Runnable rightAction, leftAction;
        Component name;

        // This is automatically centered
        public LeftRightButtonWidget(Supplier<?> value, Runnable leftAction, Runnable rightAction, MutableComponent name,
                                     int x, int y, int width, int height, int uOffset, int vOffset, int textureWidth, int textureHeight, ResourceLocation texture, int buttonWidth) {

            super(x - (width >> 1), y - (height >> 1), width, height, Component.empty());

            this.uOffset = uOffset;
            this.vOffset = vOffset;
            this.texture = texture;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.buttonWidth = buttonWidth;

            this.rightAction = rightAction;
            this.leftAction = leftAction;

            this.value = value;
            this.name = name;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            Object o = value.get();
            if (o instanceof Float number){
               number = Math.round(number * 10) / 10f;
               o = number;
            }

            MutableComponent component = Component.empty().append(name).append(": ").append(String.valueOf(o));
            guiGraphics.drawCenteredString(getMinecraft().font, component, getX() + (getWidth() / 2), getY() + (getHeight() / 4), 0x000000);

            guiGraphics.blit(
                    texture, getX(), getY(),
                    getWidth(), getHeight(), uOffset, vOffset, getWidth(), getHeight(), textureWidth, textureHeight);

        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            //confirm the mouse is on the element
            if (!(mouseX > getX() && mouseX < getRight() && mouseY > getY() && mouseY < getBottom()))
                return super.mouseClicked(mouseX, mouseY, button);

            //left button
            if (mouseX < getX() + buttonWidth){
                leftAction.run();
            }


            //right button
            if (mouseX > getRight() - buttonWidth){
                rightAction.run();
            }

            return true;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }


    public class GuiScaleWidget extends AbstractWidget {
        public GuiScaleWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty());

            if (hasDistantHorizons()) {
                setTooltip(Tooltip.create(Component.literal("GUI Scale is not supported while Distant Horizons is installed. It causes a massive frame drop upon starting and ending the minigame.")));
            } else {
                setTooltip(Tooltip.create(Component.literal("Change the GUI Scale of the minigame.")));
            }
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            //GUI SCALE
            guiGraphics.blit(
                    GUI_SCALE, getX(), getY(),
                    getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());

        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            //confirm the mouse is on the element
            if (!(mouseX > getX() && mouseX < getRight() && mouseY > getY() && mouseY < getBottom()))
                return super.mouseClicked(mouseX, mouseY, button);

            int current = guiScale().get();

            // if it's on the right half
            if (mouseX < getX() + getWidth() / 2f) {
                if (current > 1)
                    guiScale().set(current - 1);

            } else {
                guiScale().set(current + 1);
            }
            return true;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }

}
