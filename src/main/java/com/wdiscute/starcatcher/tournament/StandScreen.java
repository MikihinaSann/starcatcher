package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.network.tournament.stand.SBStandTournamentNameChangePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class StandScreen extends AbstractContainerScreen<StandMenu>
{
    public Tournament tournamentCache;
    public static Map<UUID, String> gameProfilesCache;

    private EditBox nameEditBox;
    private boolean nameWasFocused;

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/tournament/background.png");

    int uiX;
    int uiY;

    @Override
    protected void init()
    {
        super.init();
        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;
        subInit();
    }

    protected void subInit()
    {
        nameEditBox = new EditBox(this.font, uiX + 53, uiY + 36, 210, 12, Component.translatable("container.repair"));
        nameEditBox.setCanLoseFocus(true);
        nameEditBox.setTextColor(0x635040);
        nameEditBox.setBordered(false);
        nameEditBox.setMaxLength(20);
        nameEditBox.setValue("");
        nameEditBox.setTextShadow(false);
        nameEditBox.setEditable(false);
        addWidget(this.nameEditBox);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1)
    {
        this.renderBlurredBackground(i);
    }

    private void onFocusNameEditBox()
    {
        nameEditBox.setValue(tournamentCache.name);
        tournamentCache.name = "";
    }

    private void onUnfocusNameEditBox()
    {
        //send packet
        PacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(tournamentCache.tournamentUUID, nameEditBox.getValue()));
        tournamentCache.name = nameEditBox.getValue();
        nameEditBox.setValue("");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        if (tournamentCache == null) return;
        if (gameProfilesCache == null) return;

        //handle Name editbox focusing
        if (nameWasFocused != nameEditBox.isFocused())
        {
            if (nameEditBox.isFocused())
                onFocusNameEditBox();
            else
                onUnfocusNameEditBox();
        }
        nameWasFocused = nameEditBox.isFocused();

        //render background
        renderImage(guiGraphics, BACKGROUND);

        //render tournament name
        guiGraphics.drawString(this.font, tournamentCache.name, uiX + 53, uiY + 36, 0x635040, false);

        //organizer
        guiGraphics.drawString(this.font, getPlayerFromUUID(tournamentCache.owner), uiX + 55, uiY + 56, 0x635040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.organizer"), uiX + 55, uiY + 68, 0x9c897c, false);

        //status
        guiGraphics.drawString(this.font, Component.translatable(tournamentCache.status.getSerializedName()), uiX + 130, uiY + 56, 0x635040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.status"), uiX + 130, uiY + 68, 0x9c897c, false);

        //duration
        guiGraphics.drawString(this.font, U.calculateRealLifeTimeFromTicks(tournamentCache.settings.duration), uiX + 55, uiY + 88, 0x635040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.duration"), uiX + 60, uiY + 100, 0x9c897c, false);
        //duration hover
        if (x > 52 && x < 116 && y > 85 && y < 98)
        {
            List<Component> durationTooltip = new ArrayList<>();

            durationTooltip.add(Component.literal(tournamentCache.settings.duration + " ticks"));

            MutableComponent durationComp = Component.literal(String.format("%.2f ", (float) tournamentCache.settings.duration / 24000));
            if (tournamentCache.settings.duration % 24000 == 0)
                durationComp = Component.literal(tournamentCache.settings.duration / 24000 + " ");
            durationTooltip.add(durationComp.append(Component.translatable("gui.starcatcher.tournament.duration.days")));

            guiGraphics.renderTooltip(this.font, durationTooltip, Optional.empty(), mouseX, mouseY);
        }


        //scoring
        int xOwnerOffset = 0;
        if (Minecraft.getInstance().player.getUUID().equals(tournamentCache.owner))
            xOwnerOffset += 4;
        guiGraphics.drawString(this.font, Component.translatable(tournamentCache.settings.scoring.getSerializedName()), uiX + 130, uiY + 88, 0x635040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.scoring"), uiX + 130 + xOwnerOffset, uiY + 100, 0x9c897c, false);


        //signup button
        if (tournamentCache.playerScores.containsKey(Minecraft.getInstance().player.getUUID()))
        {
            guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.signed_up"), uiX + 51, uiY + 120, 0x40752c, false);
        }
        else
        {
            int color = tournamentCache.settings.canSignUp(minecraft.player) ? 0x40752c : 0xa34536;
            guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.sign_up"), uiX + 51, uiY + 120, color, false);
        }

        //cost tooltip
        if (x > 48 && x < 98 && y > 117 && y < 127 && !tournamentCache.settings.entryCost.isEmpty())
        {
            List<Component> signUpCostList = new ArrayList<>();

            signUpCostList.add(Component.literal("Sign Up Fee:"));


            for (SingleStackContainer ssc : tournamentCache.settings.entryCost)
            {
                if (!ssc.stack().isEmpty())
                    signUpCostList.add(Component.literal(ssc.stack().getCount() + "x ").append(Component.translatable(ssc.stack().getItem().getDescriptionId())));
            }

            guiGraphics.renderTooltip(this.font, signUpCostList, Optional.empty(), mouseX, mouseY);
        }

        //list of players
        int count = 0;
        int xOffset = 53;
        int yOffset = 132;
        boolean drawOthers = false;
        List<Component> others = new ArrayList<>();
        others.add(Component.translatable("gui.starcatcher.tournament.other"));
        for (var entry : tournamentCache.getPlayerScores().entrySet())
        {
            if (count == 11)
            {
                drawOthers = true;
                others.add(Component.literal(getPlayerFromUUID(entry.getKey())));
                continue;
            }
            guiGraphics.drawString(this.font, getPlayerFromUUID(entry.getKey()), uiX + xOffset, uiY + yOffset, 0x635040, false);
            count++;
            yOffset += 12;
            if (count == 6)
            {
                xOffset += 77;
                yOffset = 132;
            }
        }

        //[others]
        if (drawOthers)
        {
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.hover"), uiX + xOffset, uiY + yOffset, 0x635040, false);
            if (x > 125 && x < 190 && y > 188 && y < 202)
                guiGraphics.renderTooltip(
                        this.font,
                        others,
                        Optional.empty(), mouseX, mouseY);
        }


        if (tournamentCache.owner.equals(Minecraft.getInstance().player.getUUID()))
        {
            //[Start Tournament]
            if (tournamentCache.status.equals(Tournament.Status.SETUP))
            {
                guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.start"), uiX + 236, uiY + 188, 0x635040, false);
                if (x > 226 && x < 340 && y > 183 && y < 200)
                {
                    guiGraphics.renderTooltip(this.font, Component.literal("this can not be undone!"), mouseX, mouseY);
                }
            }

            //[Cancel Tournament]
            if (tournamentCache.status.equals(Tournament.Status.ACTIVE))
            {
                if (x > 226 && x < 340 && y > 183 && y < 200)
                {
                    guiGraphics.renderTooltip(this.font, Component.literal("this can not be undone!"), mouseX, mouseY);
                }
                guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.cancel"), uiX + 236, uiY + 188, 0x635040, false);
            }
        }
        else
        {
            guiGraphics.drawString(
                    this.font, Component.translatable("gui.starcatcher.tournament.waiting")
                            .append(Component.literal(" " + gameProfilesCache.get(tournamentCache.owner) + "...")),
                    uiX + 236, uiY + 188, 0x635040, false);
            if (x > 226 && x < 340 && y > 183 && y < 200)
            {
                guiGraphics.renderTooltip(this.font, Component.literal("this can not be undone!"), mouseX, mouseY);
            }
        }


    }

    public void onTournamentReceived(Tournament tournament)
    {
        tournamentCache = tournament;
        nameEditBox.setEditable(tournamentCache.owner.equals(Minecraft.getInstance().player.getUUID()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        System.out.println("clicked relative x: " + x);
        System.out.println("clicked relative y: " + y);

        //sign up
        if (x > 48 && x < 98 && y > 117 && y < 127)
        {
            minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 67);
        }

        //start
        if (x > 226 && x < 340 && y > 183 && y < 200)
        {
            if (tournamentCache.status.equals(Tournament.Status.SETUP))
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 68);

            if (tournamentCache.status.equals(Tournament.Status.ACTIVE))
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 69);
        }

        //duration decrease, shift does x10
        if (x > 50 && x < 60 && y > 98 && y < 107)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 101);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 102);
        }

        //duration increase, shift does x10
        if (x > 109 && x < 119 && y > 99 && y < 109)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 103);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 104);
        }

        //scoring previous
        if (x > 124 && x < 134 && y > 99 && y < 109)
        {
        }

        //scoring next
        if (x > 182 && x < 192 && y > 99 && y < 109)
        {
        }

        nameEditBox.setFocused(false);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public static String getPlayerFromUUID(UUID uuid)
    {
        if (gameProfilesCache != null)
        {
            if (gameProfilesCache.containsKey(uuid))
            {
                return gameProfilesCache.get(uuid);
            }
        }
        return "Unknown";
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 420, 260, 420, 260);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int xOffset, int yOffset)
    {
        guiGraphics.blit(rl, uiX + xOffset, uiY + yOffset, 0, 0, 420, 260, 420, 260);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == 256)
        {
            this.minecraft.player.closeContainer();
        }

        boolean editbox = this.nameEditBox.keyPressed(keyCode, scanCode, modifiers) || this.nameEditBox.canConsumeInput();
        return editbox || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public StandScreen(StandMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        //standMenu = menu;
        imageWidth = 420;
        imageHeight = 260;
    }
}
