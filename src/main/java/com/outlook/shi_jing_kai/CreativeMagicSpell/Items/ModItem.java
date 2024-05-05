package com.outlook.shi_jing_kai.CreativeMagicSpell.Items;

import com.outlook.shi_jing_kai.CreativeMagicSpell.CreativeMagicSpellMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItem {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreativeMagicSpellMod.MOD_ID);

    // location to define custom items - beginning
    // things to check before launching or committing:
    // 1. the constructor is properly replaced by the custom constructor instead of the dummy default item used
    // 2. ensure the item's texture is properly setup with size compatible to resolution
    // 3. ensure test defined in test package's item section is passing


    // location to define custom items - ending

    // register ITEMS using deferred register
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
