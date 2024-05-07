package com.outlook.shi_jing_kai.CreativeMagicSpell.Tabs;

import com.outlook.shi_jing_kai.CreativeMagicSpell.Blocks.ModBlock;
import com.outlook.shi_jing_kai.CreativeMagicSpell.CreativeMagicSpellMod;
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
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreativeMagicSpellMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("magical_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlock.MANA_CRYSTAL_ORE.get()))
                    .title(Component.translatable("creativetab.magical_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlock.MANA_CRYSTAL_ORE.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
