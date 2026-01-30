package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.advancement.MinigameCompletedTrigger;
import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends ForgeAdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    private static class Generator implements ForgeAdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
            Advancement.Builder
                    .advancement()
                    .display(
                            ModItems.AURORA.get(),
                            Component.translatable("advancements.husbandry.starcatcher.fisherman.title"),
                            Component.translatable("advancements.husbandry.starcatcher.fisherman.description"),
                            null,
                            FrameType.CHALLENGE,
                            true,
                            true,
                            false
                    ).addCriterion("perfect_catch", MinigameCompletedTrigger
                            .builder()
                            .perfect()
                            .build()
                    ).parent(new ResourceLocation("husbandry/fishy_business")).save(saver, Starcatcher.rl("husbandry/fisherman"), existingFileHelper);
        }
    }
}
