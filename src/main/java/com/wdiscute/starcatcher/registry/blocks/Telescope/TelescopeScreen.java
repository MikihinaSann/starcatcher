package com.wdiscute.starcatcher.registry.blocks.Telescope;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelescopeScreen extends Screen
{
    List<FishProperties> stars = new ArrayList<>();

    double offsetX = 0d;
    double offsetY = 0d;

    boolean omouseLocked = true;
    boolean mouseLocked = true;

    float zoomScale = 1;

    protected TelescopeScreen(Component title)
    {
        super(title);

        stars.addAll(Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY)
                .stream().filter(o -> !o.star().equals(FishProperties.Star.DEFAULT)).toList());

        for (int i = 0; i < 36; i++)
        {
            for (int j = 0; j < 48; j++)
            {
                FishProperties.Star star = FishProperties.Star.fromRaAndDec("wda", j/2, j % 2 == 0? 30 : 0, 0, -90 + i * 5, 0xddffffff);
                FishProperties fp = FishProperties.builder().withStar(star).build();

                //stars.add(fp);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        lockCursor();

        int screenHeight = Minecraft.getInstance().getWindow().getScreenHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        //background
        guiGraphics.fill(0, 0, width, height, 0xff000000);

        //scaling
        guiGraphics.pose().pushPose();

        //translate to center for scaling
        guiGraphics.pose().translate((float) width / 2, (float) height / 2, 0);
        //unscale vanilla gui scaling
        float vanillaScale = (float) Minecraft.getInstance().getWindow().getGuiScale();
        guiGraphics.pose().scale(1 / vanillaScale + 1.5f, 1 / vanillaScale + 1.5f, 0);
        //zoom scale
        guiGraphics.pose().scale(zoomScale, zoomScale, zoomScale);

        //cursor
        guiGraphics.fill(-2, -2, +2, +2, 0xffff0000);

        //stars.set(0, new StarInfo(Starcatcher.rl("da"), new Vector2d(0, 0), List.of()));
        //stars.set(1, new StarInfo(Starcatcher.rl("da"), new Vector2d(111, 20), List.of()));

        int number = 0;
        for (FishProperties fpdonotuse : stars)
        {
            FishProperties.Star star = fpdonotuse.star();
            double baseX = star.x() - offsetX;
            double baseY = star.y() - offsetY;

            for (int i = -1; i <= 1; i++)
            {
                for (int j = -2; j <= 2; j++)
                {
                    double translateX = baseX;
                    translateX += i * 3600;
                    if (j % 2 == 0)
                        translateX += 1800;

                    double translateY = baseY;
                    translateY += j * 1800;

                    //culling
                    //if (translateX > screenWidth / 2) continue;
                    //if (translateX < -screenWidth / 2) continue;
                    //if (translateY > screenHeight / 2) continue;
                    //if (translateY < -screenHeight / 2) continue;

                    guiGraphics.pose().pushPose();

                    number++;
                    guiGraphics.pose().translate(translateX, translateY, 0);

                    //render lines to connections
                    for (String connectionStarName : star.connections())
                    {

                        Optional<FishProperties> optional = stars.stream().filter(fplol -> fplol.star().name().equals(connectionStarName)).findFirst();

                        //if rl is not valid
                        if (optional.isPresent())
                        {
                            FishProperties.Star connectionStar = optional.get().star();
                            double distance = Vector2d.distance(star.x(), star.y(), connectionStar.x(), connectionStar.y());

                            var a = star.y() - connectionStar.y();
                            var b = star.x() - connectionStar.x();
                            var angle = Mth.atan2(a, b);

                            guiGraphics.pose().pushPose();
                            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) Math.toDegrees(angle) + 90));
                            guiGraphics.fill(0, 0, 1, (int) distance, star.debugColor());
                            guiGraphics.pose().popPose();

                        }


                    }

                    //render star
                    guiGraphics.fill(-5, -5, 5, 5, 0xff000000);
                    guiGraphics.fill(-2, -2, 2, 2, star.debugColor());

                    guiGraphics.pose().popPose();
                }
            }
        }

        //todo cull better
        //System.out.println("rendered " + number + " stars");
        guiGraphics.pose().popPose();
    }

    private static void plotLine(GuiGraphics graphics, final int x0, final int y0, final int x1, final int y1, final int color)
    {
        //plotLine(guiGraphics, (int) 0, (int) 0, (int) (star.pos.x - starConnected.pos.x), (int) (star.pos.y - starConnected.pos.y), 0xffff0000);
        graphics.drawManaged(() ->
        {
            int newX0 = x0;
            int newY0 = y0;
            int dx = Mth.abs(x1 - newX0);
            int sx = newX0 < x1 ? 1 : -1;
            int dy = -Mth.abs(y1 - newY0);
            int sy = newX0 < y1 ? 1 : -1;
            int error = dx + dy;

            while (true)
            {
                graphics.fill(newX0, newY0, newX0 + 1, newY0 + 1, color);
                int e2 = 2 * error;
                if (e2 >= dy)
                {
                    if (newX0 == x1) break;
                    error = error + dy;
                    newX0 = newX0 + sx;
                }
                if (e2 <= dx)
                {
                    if (newY0 == y1) break;
                    error = error + dx;
                    newY0 = newY0 + sy;
                }
            }
        });
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        zoomScale += (float) (scrollY / 20);
        zoomScale = Math.clamp(zoomScale, 0.01f, 4);
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void lockCursor()
    {
        omouseLocked = mouseLocked;
        if (mouseLocked)
        {
            double xpos = (double) this.minecraft.getWindow().getScreenWidth() / 2;
            double ypos = (double) this.minecraft.getWindow().getScreenHeight() / 2;
            InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_HIDDEN, xpos, ypos);
        } else
        {
            GLFW.glfwSetInputMode(this.minecraft.getWindow().getWindow(), 208897, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_LEFT_ALT)
            mouseLocked = false;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_LEFT_ALT)
            mouseLocked = true;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY)
    {
        super.mouseMoved(mouseX, mouseY);

        if (omouseLocked && mouseLocked)
        {
            //dumb lil formula to make the mouse sensitivity feel better on non default zoom scales
            double scale = zoomScale <= 1f ? 4f - 3f * zoomScale : 1f - (zoomScale - 1f) / 4f;
            scale /= 6;

            offsetX += (mouseX - (double) this.minecraft.getWindow().getGuiScaledWidth() / 2) * scale;
            offsetY += (mouseY - (double) this.minecraft.getWindow().getGuiScaledHeight() / 2) * scale;

            if (offsetX > 3600) offsetX -= 3600;
            if (offsetX < 0) offsetX += 3600;

            if (offsetY > 3600) offsetY -= 3600;
            if (offsetY < 0) offsetY += 3600;
        }

    }

    @Override
    protected void init()
    {
        super.init();
        double xpos = (double) this.minecraft.getWindow().getScreenWidth() / 2;
        double ypos = (double) this.minecraft.getWindow().getScreenHeight() / 2;
        InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), 212995, xpos, ypos);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
