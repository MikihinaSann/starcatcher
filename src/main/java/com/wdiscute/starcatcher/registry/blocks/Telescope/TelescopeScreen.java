package com.wdiscute.starcatcher.registry.blocks.Telescope;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.jna.platform.win32.OaIdl;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelescopeScreen extends Screen
{
    List<StarInfo> stars = new ArrayList<>();

    double offsetX = 0d;
    double offsetY = 0d;

    boolean omouseLocked = true;
    boolean mouseLocked = true;

    float zoomScale = 1;

    public static float raToDegrees(int hours, int minutes, double seconds)
    {
        double decimalHours = hours + minutes / 60.0 + seconds / 3600.0;
        return ((float) (decimalHours * 15.0));
    }

    record StarInfo(
            ResourceLocation rl,
            Vector2d pos,
            List<ResourceLocation> connections
    )
    {
        //helper builder using ra and dec
        public static StarInfo starBullshit(String name, float raDegrees, float dec, String... connections)
        {
            List<ResourceLocation> con = new ArrayList<>();
            Arrays.stream(connections).forEach(o -> con.add(Starcatcher.rl(o)));

            double finaly = (dec + 90) * 2;

            double angleX = Math.cos(Math.toRadians(raDegrees));
            double angleY = Math.sin(Math.toRadians(raDegrees));

            int offset = dec > 0 ? 1800 : 0;

            Vector2d coords;
            if (dec > 0)
                coords = new Vector2d((int) (offset + angleX * Math.abs(90 - dec) * 10), (int) (angleY * Math.abs(90 - dec)) * 10);
            else
                coords = new Vector2d((int) (offset + angleX * Math.abs(dec) * 10), (int) (angleY * Math.abs(dec)) * 10);


            return new StarInfo(Starcatcher.rl(name), coords, con);
        }
    }

    protected TelescopeScreen(Component title)
    {
        super(title);

        // stars.add(StarInfo.starBullshit("center", 0, 90f));

        // Ursa Minor

        for (int i = 0; i < 18; i++)
        {
//            stars.add(StarInfo.starBullshit("polaris", raToDegrees(0, 0, 0), -90f + i * 10));
//            stars.add(StarInfo.starBullshit("polaris", raToDegrees(2, 0, 0), -90f + i * 10));
//            stars.add(StarInfo.starBullshit("polaris", raToDegrees(22, 0, 0), -90f + i * 10));
        }


        for (int i = 0; i < 36; i++)
        {
            for (int j = 0; j < 48; j++)
            {
                //stars.add(StarInfo.starBullshit("polaris", raToDegrees(j / 2, j % 2 == 0? 30 : 0, 0), -90f + i * 5, "dwad"));
            }

        }


        stars.add(StarInfo.starBullshit("polaris", raToDegrees(3, 6, 43), 89f));
        stars.add(StarInfo.starBullshit("kochab", raToDegrees(14, 50, 42), 74.16f));
        stars.add(StarInfo.starBullshit("pherkad", raToDegrees(15, 20, 44), 71.83f));
        stars.add(StarInfo.starBullshit("yildun", raToDegrees(17, 32, 13), 86.59f));
        stars.add(StarInfo.starBullshit("epsilon_umi", raToDegrees(16, 45, 58), 82.04f));
        stars.add(StarInfo.starBullshit("zeta_umi", raToDegrees(15, 44, 4), 77.79f));
        stars.add(StarInfo.starBullshit("eta_umi", raToDegrees(16, 17, 31), 75.75f));
//
//        //Ursa Major
        stars.add(StarInfo.starBullshit("dubhe", raToDegrees(11, 3, 43), 61.75f));
        stars.add(StarInfo.starBullshit("merak", raToDegrees(11, 1, 50), 56.38f));
        stars.add(StarInfo.starBullshit("phecda", raToDegrees(11, 53, 49), 53.70f));
        stars.add(StarInfo.starBullshit("megrez", raToDegrees(12, 15, 25), 57.03f));
        stars.add(StarInfo.starBullshit("alioth", raToDegrees(12, 54, 1), 55.97f));
        stars.add(StarInfo.starBullshit("mizar", raToDegrees(13, 23, 55), 54.92f));
        stars.add(StarInfo.starBullshit("alkaid", raToDegrees(13, 47, 32), 49.31f));


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

        //System.out.println(Minecraft.getInstance().getFps());

        //cursor
        guiGraphics.fill(-2, -2, +2, +2, 0xffff0000);

        int number = 0;
        for (StarInfo star : stars)
        {
            double baseX = star.pos.x - offsetX;
            double baseY = star.pos.y - offsetY;

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
                    if(translateX > screenWidth /2) continue;
                    if(translateX < -screenWidth /2) continue;
                    if(translateY > screenHeight /2) continue;
                    if(translateY < -screenHeight /2) continue;

                    guiGraphics.pose().pushPose();

                    number++;
                    guiGraphics.pose().translate(translateX, translateY, 0);

                    if (star.connections.isEmpty())
                        guiGraphics.fill(-2, -2, 2, 2, 0xffffffff);
                    else
                        guiGraphics.fill(-2, -2, 2, 2, 0x55ffffff);

                    guiGraphics.pose().popPose();
                }
            }
        }

        System.out.println("rendered " + number + " stars");
        guiGraphics.pose().popPose();
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        zoomScale += (float) (scrollY / 20);
        zoomScale = Math.clamp(zoomScale, 0.2f, 4);
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
