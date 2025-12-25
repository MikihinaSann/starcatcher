package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.network.FishingCompletedPayload;
import com.wdiscute.starcatcher.io.network.ModNetworking;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.registry.ModKeymappings;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.custom.minigamemodifiers.BaseModifier;
import com.wdiscute.starcatcher.registry.custom.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.custom.tackleskin.BaseTackleSkin;
import com.wdiscute.starcatcher.storage.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.joml.Quaternionf;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class FishingMinigameScreen extends Screen implements GuiEventListener
{
    public static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame/minigame.png");
    private static final ResourceLocation NETHER = Starcatcher.rl("textures/gui/minigame/nether.png");
    private static final ResourceLocation CAVE = Starcatcher.rl("textures/gui/minigame/cave.png");
    private static final ResourceLocation SURFACE = Starcatcher.rl("textures/gui/minigame/surface.png");
    public ResourceLocation tankTexture = SURFACE;

    public final FishProperties fishProperties;
    public FishProperties.Difficulty difficulty;

    public final ItemStack itemBeingFished;
    public final ItemStack bobber;
    public final ItemStack bait;
    public final ItemStack hook;
    public final ItemStack treasureIS;

    public final AbstractTackleSkin tackleSkin;

    public final InteractionHand handToSwing;

    public int penalty;
    public float decay;

    public float kimbeMarkerPos = 0;
    public float kimbeMarkerAlpha = 0;
    public int kimbeMarkerColor = 0x00ff00;

    public int gracePeriod = 80;
    //todo do this smiley face :)
    public boolean minigameStarted;

    public float pointerSpeed;
    public float pointerBaseSpeed;

    public int tickCount = 0;
    public int pointerPos = 0;
    public int currentRotation = 1;
    public float partial;
    public float hitDelay;

    public float progress = 20;
    public int progressSmooth = 20;

    public boolean perfectCatch = true;
    public int consecutiveHits = 0;

    public boolean treasureActive;
    public int treasureProgress = 0;
    public int treasureProgressSmooth = 0;

    List<HitFakeParticle> hitParticles = new ArrayList<>();

    public int previousGuiScale;


    // Nikdo53 values, these are mine dont steal them
    public final int holdingDelay = 6;
    public int holdingTicks = 0;
    protected boolean isHoldingKey = false;
    protected boolean isHoldingMouse = false;

    protected final List<ActiveSweetSpot> activeSweetSpots = new ArrayList<>();
    protected final List<AbstractMinigameModifier> modifiers = new ArrayList<>();

    public FishingMinigameScreen(FishProperties fp, ItemStack rod)
    {
        super(Component.empty());

        handToSwing = Minecraft.getInstance().player.getMainHandItem().is(StarcatcherTags.RODS) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        previousGuiScale = Minecraft.getInstance().options.guiScale().get();
        if (!hasDistantHorizons())
            Minecraft.getInstance().options.guiScale().set(Config.MINIGAME_GUI_SCALE.get());

        hitDelay = Config.HIT_DELAY.get().floatValue();

        this.fishProperties = fp;
        this.difficulty = fp.dif();

        //if override is not missingno (default) then use the override item set
        if (!fp.catchInfo().overrideMinigameWith().get().equals(ModItems.MISSINGNO.get()))
            this.itemBeingFished = new ItemStack(fp.catchInfo().overrideMinigameWith().get());
        else
            this.itemBeingFished = new ItemStack(fp.catchInfo().fish().get());

        this.bobber = ModDataComponents.get(rod, ModDataComponents.BOBBER).stack().copy();
        this.bait = ModDataComponents.get(rod, ModDataComponents.BAIT).stack().copy();
        this.hook = ModDataComponents.get(rod, ModDataComponents.HOOK).stack().copy();

        this.treasureIS = new ItemStack(fp.catchInfo().treasure().get());

        if (ModDataComponents.has(rod, ModDataComponents.TACKLE_SKIN))
        {
            ResourceLocation rl = ModDataComponents.get(rod, ModDataComponents.TACKLE_SKIN);

            Optional<Supplier<AbstractTackleSkin>> optional = Optional.ofNullable(Starcatcher.getRegistry(Starcatcher.TACKLE_SKIN).getValue(rl));
            if (optional.isPresent())
                this.tackleSkin = optional.get().get();
            else
                this.tackleSkin = new BaseTackleSkin();
        }
        else
        {
            this.tackleSkin = new BaseTackleSkin();
        }


        //tank texture change
        ClientLevel level = Minecraft.getInstance().level;
        ResourceKey<Level> dim = level.dimension();
        Player player = Minecraft.getInstance().player;
        if (player.getY() < 50 && dim.equals(Level.OVERWORLD))
            tankTexture = CAVE;

        if (dim.equals(Level.NETHER))
            tankTexture = NETHER;

        //base - a lot of these are now hitZone-based
        this.pointerSpeed = difficulty.speed();
        this.pointerBaseSpeed = difficulty.speed();
        this.penalty = difficulty.penalty();
        this.decay = difficulty.decay();

        //add base modifier for kimbe before other modifiers so they can override kimbe if needed
        addModifier(new BaseModifier());

        //add every modifier from fp json which is registered
        for (ResourceLocation rl : fp.dif().modifiers())
        {
            Optional<Supplier<AbstractMinigameModifier>> newModifier = level.registryAccess().registryOrThrow(Starcatcher.MINIGAME_MODIFIERS).getOptional(rl);
            newModifier.ifPresent(mod -> addModifier(mod.get()));
        }

        //cycle through all the items to check for modifiers
        //todo improve this to check things dynamically, look into baubles compat & armor slots (not inventory otherwise people could offhand modifiers)
        List<ItemStack> allItems = List.of(bobber, rod, bait, hook);
        for (ItemStack is : allItems)
            if (ModDataComponents.has(is, ModDataComponents.MINIGAME_MODIFIERS))
                for (ResourceLocation rl : Objects.requireNonNull(ModDataComponents.get(is, ModDataComponents.MINIGAME_MODIFIERS)))
                {
                    Optional<Supplier<AbstractMinigameModifier>> newModifier = level.registryAccess().registryOrThrow(Starcatcher.MINIGAME_MODIFIERS).getOptional(rl);
                    newModifier.ifPresent(mod -> addModifier(mod.get()));
                }


        //add every sweet spot from fp json which is registered
        for (FishProperties.SweetSpot ss : fp.dif().sweetSpots())
        {
            var newSweetSpot = new ActiveSweetSpot(this, ss, bobber, bait, hook);
            if (newSweetSpot.removed) continue;

            addSweetSpot(newSweetSpot);
        }

    }

    public void addModifier(AbstractMinigameModifier mod)
    {
        mod.onAdd(this);
        if (!mod.removed) this.modifiers.add(mod);
    }

    public void addUniqueModifier(AbstractMinigameModifier mod)
    {
        //only adds if theres not a modifier of the same class already present
        if (modifiers.stream().noneMatch(m -> m.getClass() == mod.getClass()))
        {
            mod.onAdd(this);
            if (!mod.removed) this.modifiers.add(mod);
        }
    }

    public void addSweetSpot(ActiveSweetSpot ass)
    {
        ass.behaviour.onAdd(this, ass);

        for (AbstractMinigameModifier modifier : modifiers)
        {
            ass = modifier.onSpotAdded(ass);
        }

        if (!ass.removed) this.activeSweetSpots.add(ass);
    }

    public int getRandomFreePosition(int sizeOfTheSweetspotToPlace)
    {
        int posBeingChecked = U.r.nextInt(360);

        //find the closest available pos
        for (int i = 0; i < 180; i++)
        {

            //check left and right
            for (int j = 1; j > -2; j -= 2)
            {

                int checkPos = clampPos(posBeingChecked + (i * j));

                if (activeSweetSpots.stream().noneMatch(s -> doDegreesOverlapWithLeeway(checkPos, s.pos, (s.thickness + sizeOfTheSweetspotToPlace) / 2)
                ))
                {
                    return checkPos;
                }
            }
        }

        LogUtils.getLogger().warn("Starcatcher's minigame couldn't find a non-overlapping free position! Consider not having so many active sweet spots");
        return U.r.nextInt(360);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float badPartialTick) {
        renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, badPartialTick);
        final float partialTick = PartialTickHelper.INSTANCE.getPartialTicks(minecraft.level);

        PoseStack poseStack = guiGraphics.pose();
        partial = partialTick;

        //render modifiers background
        modifiers.forEach(modifier -> modifier.renderBackground(guiGraphics, partialTick, width, height));

        if (treasureActive) renderTreasure(guiGraphics);

        //render tank background
        guiGraphics.blit(tankTexture, width / 2 - 42 - 100, height / 2 - 48, 85, 97, 0, 0, 85, 97, 85, 97);

        //render wheel background
        guiGraphics.blit(TEXTURE, width / 2 - 32, height / 2 - 32, 64, 64, 0, 192, 64, 64, 256, 256);

        //render spacebar
        guiGraphics.blit(TEXTURE, width / 2 - 16, height / 2 + 40, 32, 16, isHoldingKey ? 48 : 0, 112, 32, 16, 256, 256);

        //render all sweet spots
        activeSweetSpots.forEach(ass -> renderSweetSpot(ass, guiGraphics, partialTick, poseStack));

        //render wheel second layer
        guiGraphics.blit(TEXTURE, width / 2 - 32, height / 2 - 32, 64, 64, 64, 192, 64, 64, 256, 256);

        //render pointer
        renderPointer(guiGraphics, poseStack, partialTick);

        //render kimbe marker
        renderKimbeMarker(guiGraphics);

        //silver thing on top
        guiGraphics.blit(TEXTURE, width / 2 - 16, height / 2 - 16, 32, 32, 208, 208, 32, 32, 256, 256);

        //fishing rod
        guiGraphics.blit(TEXTURE, width / 2 - 32 - 70, height / 2 - 24 - 57, 64, 48, 192, 0, 64, 48, 256, 256);

        //fishing line
        guiGraphics.blit(
                TEXTURE, width / 2 - 6 - 102, height / 2 - 56 - 18,
                16, 112 - progressSmooth,
                176, progressSmooth,
                16, 112 - progressSmooth,
                256, 256);

        //item being fished
        guiGraphics.renderItem(itemBeingFished, width / 2 - 8 - 100, height / 2 - 8 + 35 - progressSmooth);

        //render sweet spots foreground
        activeSweetSpots.forEach(sweetspot -> sweetspot.behaviour.renderForeground(guiGraphics, partialTick, width, height));

        //render modifiers foreground
        modifiers.forEach(modifier -> modifier.renderForeground(guiGraphics, partialTick, width, height));

        //render particles
        hitParticles.forEach(p -> p.render(guiGraphics, width, height));
    }

    public void renderSweetSpot(ActiveSweetSpot ass, GuiGraphics guiGraphics, float partialTick, PoseStack poseStack)
    {
        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.pushPose();

        poseStack.translate(centerX, centerY, 0);

        poseStack.rotateAround(Axis.ZP.rotationDegrees(ass.pos + partialTick * ass.movingRate), 0, 0, 0);

        boolean isDisabled = modifiers.stream().anyMatch(mod -> mod.disableSweetSpotRendering(ass));
        if (!isDisabled)
            ass.behaviour.render(guiGraphics, poseStack, partialTick);

        modifiers.forEach(mod -> mod.renderOnSweetSpot(guiGraphics, poseStack, ass, partialTick));

        poseStack.popPose();
    }

    public void renderTreasure(GuiGraphics guiGraphics)
    {
        //treasure bar
        guiGraphics.blit(
                TEXTURE, width / 2 - 158, height / 2 - 42 + (int) (64 - (64f * treasureProgressSmooth) / 100),
                5, 64 * treasureProgressSmooth / 100,
                141, 6 + 64 - (float) (64 * treasureProgressSmooth) / 100,
                5, 64 * treasureProgressSmooth / 100,
                256, 256);

        //treasure chest
        guiGraphics.blit(TEXTURE, width / 2 - 16 - 155, height / 2 - 48, 32, 96, 96, 0, 32, 96, 256, 256);

        //render treasure on top of bar
        guiGraphics.renderItem(treasureIS, width / 2 - 163, ((int) ((float) height / 2 - (64f * treasureProgressSmooth) / 100) + 15));

        //todo consider if i want tackle skins to change particle color like this
//        int color = Tooltips.hueToRGBInt(Tooltips.hue);
//        if (tackleSkin.is(ModItems.PEARL_BOBBER_SMITHING_TEMPLATE))
//            RenderSystem.setShaderColor(
//                    (float) FastColor.ARGB32.red(color) / 255,
//                    (float) FastColor.ARGB32.green(color) / 255,
//                    (float) FastColor.ARGB32.blue(color) / 255,
//                    1);
//
//        if (tackleSkin.is(ModItems.COLORFUL_BOBBER_SMITHING_TEMPLATE))
//            color = ModDataComponents.get(tackleSkin, ModDataComponents.BOBBER_COLOR).getColorAsInt();
//
//        if (tackleSkin.is(ModItems.COLORFUL_BOBBER_SMITHING_TEMPLATE))
//            RenderSystem.setShaderColor(
//                    (float) FastColor.ARGB32.red(color) / 255,
//                    (float) FastColor.ARGB32.green(color) / 255,
//                    (float) FastColor.ARGB32.blue(color) / 255,
//                    1);

        //outline when treasure complete
        if (treasureProgress > 99)
            guiGraphics.blit(
                    TEXTURE, width / 2 - 16 - 155, height / 2 - 48,
                    32, 96, 64, 0, 32, 96, 256, 256);

        //todo related to above
//        if (tackleSkin.is(ModItems.COLORFUL_BOBBER_SMITHING_TEMPLATE) || tackleSkin.is(ModItems.PEARL_BOBBER_SMITHING_TEMPLATE))
//            RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public void renderKimbeMarker(GuiGraphics guiGraphics)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(kimbeMarkerPos)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(
                (float) U.intToRed(kimbeMarkerColor) / 255,
                (float) U.intToGreen(kimbeMarkerColor) / 255,
                (float) U.intToBlue(kimbeMarkerColor) / 255,
                kimbeMarkerAlpha);
        RenderSystem.enableBlend();

        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                64, 64, 128, 128, 64, 64, 256, 256);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    public void renderPointer(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick)
    {
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);

        poseStack.mulPose(Axis.ZP.rotationDegrees(pointerPos + ((pointerSpeed * partialTick) * currentRotation)));

        poseStack.translate(0, -16, 0);

        boolean isDisabled = modifiers.stream().anyMatch(AbstractMinigameModifier::disablePointerRendering);
        if (!isDisabled)
            renderPoseCentered(guiGraphics, TEXTURE, 64, 64, 128, 192, 256);

        poseStack.translate(0, 16, 0);

        modifiers.forEach(mod -> mod.renderOnPointer(guiGraphics, poseStack, partialTick));

        poseStack.popPose();
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int keyModifiers)
    {
        if (keyCode == ModKeymappings.MINIGAME_HIT.getKey().getValue())
        {
            isHoldingKey = false;
            holdingTicks = 0;
        }

        modifiers.forEach(mod -> mod.onKeyReleased(keyCode, scanCode, keyModifiers));

        return super.keyReleased(keyCode, scanCode, keyModifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        isHoldingMouse = false;
        holdingTicks = 0;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        inputPressed();
        isHoldingMouse = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int keyModifiers)
    {
        //closes when pressing E
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey))
        {
            this.onClose();
            return true;
        }

        //hit input
        if (ModKeymappings.MINIGAME_HIT.isActiveAndMatches(mouseKey))
        {
            if (!isHoldingKey) inputPressed();

            isHoldingKey = true;
        }

        this.modifiers.forEach(mod -> mod.onKeyPress(keyCode, scanCode, keyModifiers));

        return super.keyPressed(keyCode, scanCode, keyModifiers);
    }

    public void inputPressed()
    {
        if (gracePeriod > 0) gracePeriod = 0;

        Minecraft.getInstance().player.swing(handToSwing, true);

        boolean hitSomething = false;

        Vec3 pos = Minecraft.getInstance().player.position();
        ClientLevel level = Minecraft.getInstance().level;

        kimbeMarkerPos = getPointerPosPrecise();


        for (ActiveSweetSpot ass : activeSweetSpots)
        {
            if (doDegreesOverlapWithLeeway(getPointerPosPrecise(), ass.pos, ass.thickness / 2))
            {

                //check if each modifier allows the hit to register
                boolean isCanceled = false;
                for (AbstractMinigameModifier modifier : modifiers)
                {

                    if (modifier.onHit(ass))
                        isCanceled = true;
                }

                if (isCanceled) continue;

                hitSomething = true;
                ass.behaviour.onHit();
                break;
            }
        }


        if (!hitSomething)
        {
            this.modifiers.forEach(AbstractMinigameModifier::onMiss);

            consecutiveHits = 0;
            level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1, 1, false);
            progress -= penalty;
        }
    }

    public float getPointerPosPrecise()
    {
        float pointerPosPrecise = (pointerPos + ((pointerSpeed * partial) * currentRotation));

        pointerPosPrecise += hitDelay * pointerSpeed * currentRotation;
        return pointerPosPrecise;
    }

    public static boolean doDegreesOverlapWithLeeway(float degrees1, float degrees2, int leeway)
    {
        boolean b = Math.abs(degrees2 - degrees1) < (float) leeway;
        boolean b2 = Math.abs(degrees2 - degrees1) > 360 - ((float) leeway);
        return b || b2;
    }

    @Override
    public void tick()
    {
        if (isHoldingInput())
        { //mimics the keyboard behavior
            holdingTicks++;
            if (holdingTicks > holdingDelay) inputPressed();
        }

        //tick modifiers
        modifiers.forEach(AbstractMinigameModifier::tick);
        //tick behaviour
        activeSweetSpots.forEach(s -> s.behaviour.tick());

        //remove activeSweetSpots marked for removal
        activeSweetSpots.removeIf(s ->
        {
            if (s.removed) s.behaviour.onRemove();
            return s.removed;
        });

        //remove modifiers marked for removal
        modifiers.removeIf(m ->
        {
            if (m.removed) m.onRemove();
            return m.removed;
        });

        //move pointer
        pointerPos += (int) (pointerSpeed * currentRotation);

        //decrease kimbe markers alpha
        kimbeMarkerAlpha -= 0.1f;

        if (pointerPos > 360) pointerPos -= 360;
        if (pointerPos < 0) pointerPos += 360;

        gracePeriod--;

        tickCount++;

        progressSmooth += (int) Math.signum(progress - progressSmooth);
        progressSmooth += (int) Math.signum(progress - progressSmooth);

        treasureProgressSmooth += (int) Math.signum(treasureProgress - treasureProgressSmooth);

        if (tickCount % 5 == 0 && gracePeriod < 0)
        {
            progress -= decay;
        }

        if (!isSettingsScreen())
        {

            if (progressSmooth < 0)
            {
                this.onClose();
            }

            if (progressSmooth > 75)
            {
                //if completed treasure minigame, or is a perfect catch with the mossy hook
                boolean awardTreasure = treasureProgress > 100 || modifiers.stream().anyMatch(AbstractMinigameModifier::forceAwardTreasure);

                ModNetworking.sendToServer(new FishingCompletedPayload(tickCount, awardTreasure, perfectCatch, consecutiveHits));
                this.onClose();
            }
        }

        hitParticles.removeIf(HitFakeParticle::tick);
    }

    @Override
    public void onClose()
    {
        modifiers.forEach(AbstractMinigameModifier::onRemove);

        if (!hasDistantHorizons())
            Minecraft.getInstance().options.guiScale().set(previousGuiScale);

        ModNetworking.sendToServer(new FishingCompletedPayload(-1, false, false, consecutiveHits));
        this.minecraft.popGuiLayer();
    }

    public void addParticles(float posInDegrees, int count, int color)
    {
        int xPos = (int) (30 * Math.cos(Math.toRadians(posInDegrees - 90)));
        int yPos = (int) (30 * Math.sin(Math.toRadians(posInDegrees - 90)));

        for (int i = 0; i < count; i++)
        {
            //todo tackle skin particle stuff? do i want this? who knows

//            if (tackleSkin.is(ModItems.PEARL_BOBBER_SMITHING_TEMPLATE))
//            {
//                hitParticles.add(new HitFakeParticle(
//                        xPos, yPos, new Vector2d(U.r.nextFloat() * 2 - 1, U.r.nextFloat() * 2 - 1),
//                        U.r.nextFloat(),
//                        U.r.nextFloat(),
//                        U.r.nextFloat(),
//                        1
//                ));
//                continue;
//            }

//            if (tackleSkin.is(ModItems.COLORFUL_BOBBER_SMITHING_TEMPLATE))
//            {
//                ColorfulSmithingTemplate.BobberColor bobberColor = ModDataComponents.get(tackleSkin, ModDataComponents.BOBBER_COLOR);
//                hitParticles.add(new HitFakeParticle(
//                        xPos, yPos, new Vector2d(U.r.nextFloat() * 2 - 1, U.r.nextFloat() * 2 - 1),
//                        bobberColor.r(),
//                        bobberColor.g(),
//                        bobberColor.b(),
//                        1
//                ));
//                continue;
//            }

            hitParticles.add(
                    new HitFakeParticle(
                            xPos,
                            yPos,
                            new Vector2d(U.r.nextFloat() * 2 - 1, U.r.nextFloat() * 2 - 1),
                            (float) U.intToRed(color) / 255,
                            (float) U.intToGreen(color) / 255,
                            (float) U.intToBlue(color) / 255,
                            1
                    ));
        }
    }

    /**
     * Renders a texture centered to the top left corner, to be moved with poseStack
     */
    public static void renderPoseCentered(GuiGraphics guiGraphics, ResourceLocation texture, int spriteSize)
    {
        guiGraphics.blit(
                texture, -spriteSize / 2, -spriteSize / 2,
                spriteSize, spriteSize, 0, 0, spriteSize, spriteSize, spriteSize, spriteSize);
    }

    public static void renderPoseCentered(GuiGraphics guiGraphics, ResourceLocation texture, int spriteWidth, int spriteHeight, int uOffset, int vOffset, int textureSize)
    {
        guiGraphics.blit(
                texture, -spriteWidth / 2, -spriteHeight / 2,
                spriteWidth, spriteHeight, uOffset, vOffset, spriteWidth, spriteHeight, textureSize, textureSize);
    }


    public boolean isHoldingInput()
    {
        return isHoldingMouse || isHoldingKey;
    }

    public static boolean hasDistantHorizons()
    {
        return ModList.get().isLoaded("distanthorizons");
    }

    public boolean isSettingsScreen()
    {
        return false;
    }

    public static int clampPos(int pos)
    {
        pos %= 360;
        if (pos < 0)
        {
            pos += 360;
        }
        return pos;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    public void refreshSweetSpotsAlphas()
    {
        activeSweetSpots.forEach(s -> s.alpha = 1);
    }

    public void removeAllSweetSpots()
    {
        for (var dws : activeSweetSpots)
        {
            dws.removed = true;
        }
    }
}
