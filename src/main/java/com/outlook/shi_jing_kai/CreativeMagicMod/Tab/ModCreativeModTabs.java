package com.outlook.shi_jing_kai.CreativeMagicMod.Tab;

import com.outlook.shi_jing_kai.CreativeMagicMod.Block.ModBlock;
import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.ModItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreativeMagicMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("magical_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlock.MANA_CRYSTAL_ORE.get()))
                    .title(Component.translatable("creativetab.magical_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // register the blocks into the tab
                        pOutput.accept(ModBlock.MANA_CRYSTAL_ORE.get());

                        // register the items into the tab
                        pOutput.accept(ModItem.MANA_CRYSTAL.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
