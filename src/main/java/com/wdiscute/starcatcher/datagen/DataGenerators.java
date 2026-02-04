package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID,  bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        PackOutput output = gen.getPackOutput();
        //fish properties
        gen.addProvider(
                event.includeServer(),
                new DGFishingPropertiesProvider(output, registries)
        );

        gen.addProvider(event.includeServer(), new DGTrophyPropertiesProvider(output, registries));

        //fish models
        gen.addProvider(event.includeServer(), new DGModItemModelProvider(output, existingFileHelper));

        //block tags
        BlockTagsProvider btp = new DGModBlocksTagProvider(output, registries, existingFileHelper);
        gen.addProvider(event.includeServer(), btp);

        //item tags
        ItemTagsProvider itp = new DGModItemsTagProvider(output, registries, btp.contentsGetter(), existingFileHelper);
        gen.addProvider(event.includeServer(), itp);

        //advancements
        gen.addProvider(event.includeServer(), new DGModAdvancementProvider(output, registries, existingFileHelper));

        //biome tags
        gen.addProvider(event.includeServer(), new DGBiomeTagsProvider(output, registries, existingFileHelper));
    }
}
