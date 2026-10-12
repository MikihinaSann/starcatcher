package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxMenu;
import com.wdiscute.starcatcher.tournament.StandMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import com.wdiscute.starcatcher.neoforged.bus.api.IEventBus;
import com.wdiscute.starcatcher.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import com.wdiscute.starcatcher.neoforged.neoforge.network.IContainerFactory;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredHolder;
import com.wdiscute.starcatcher.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCMenuTypes
{
    DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Starcatcher.MOD_ID);

    Supplier<MenuType<StandMenu>> STAND_MENU =
            registerMenuType("stand_menu", StandMenu::new);

    Supplier<MenuType<TackleBoxMenu>> TACKLE_BOX =
            registerMenuType("tackle_box.png", TackleBoxMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                              IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
