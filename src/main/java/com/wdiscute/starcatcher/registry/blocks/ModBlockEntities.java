package com.wdiscute.starcatcher.registry.blocks;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.blocks.Telescope.TelescopeBlockEntity;
import com.wdiscute.starcatcher.registry.blocks.display.DisplayBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Starcatcher.MOD_ID);


    public static final Supplier<BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITIES.register("trophy",
            () -> BlockEntityType.Builder.of(TrophyBlockEntity::new,
                            ModBlocks.TROPHY_GOLD.get(),
                            ModBlocks.TROPHY_SILVER.get(),
                            ModBlocks.TROPHY_BRONZE.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<StandBlockEntity>> STAND = BLOCK_ENTITIES.register("stand",
            () -> BlockEntityType.Builder.of(StandBlockEntity::new,
                            ModBlocks.STAND.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<TelescopeBlockEntity>> TELESCOPE = BLOCK_ENTITIES.register("telescope",
            () -> BlockEntityType.Builder.of(TelescopeBlockEntity::new,
                    ModBlocks.TELESCOPE.get()
            ).build(null));

    public static final Supplier<BlockEntityType<DisplayBlockEntity>> DISPLAY = BLOCK_ENTITIES.register("display",
            () -> BlockEntityType.Builder.of(DisplayBlockEntity::new,
                    ModBlocks.DISPLAY.get()
            ).build(null));




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
