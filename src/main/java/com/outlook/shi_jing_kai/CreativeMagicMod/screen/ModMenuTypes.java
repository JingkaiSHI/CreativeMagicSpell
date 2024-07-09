package com.outlook.shi_jing_kai.CreativeMagicMod.screen;

import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CreativeMagicMod.MOD_ID);

    public static final RegistryObject<MenuType<MagicCreationMenu>> MAGIC_CREATION_MENU = registerMenuType("magic_creation_menu", (IContainerFactory<MagicCreationMenu>) MagicCreationMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}
