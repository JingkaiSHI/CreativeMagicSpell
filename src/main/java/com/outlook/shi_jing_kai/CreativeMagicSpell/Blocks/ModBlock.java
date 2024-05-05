package com.outlook.shi_jing_kai.CreativeMagicSpell.Blocks;

import com.outlook.shi_jing_kai.CreativeMagicSpell.CreativeMagicSpellMod;
import com.outlook.shi_jing_kai.CreativeMagicSpell.Items.ModItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreativeMagicSpellMod.MOD_ID);

    // location to define custom blocks - beginning
    // things to check before launching or committing:
    // 1. the constructor is properly replaced by the custom constructor instead of the dummy default block used
    // 2. ensure the block's texture is properly setup with size compatible to resolution
    // 3. ensure test defined in test package's block section is passing


    // location to define custom blocks - ending

    // register BLOCKS using deferred register
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<BlockItem> registerBlockItem(String name, Supplier<T> block){
        return ModItem.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
