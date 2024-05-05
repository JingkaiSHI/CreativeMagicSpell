package com.outlook.shi_jing_kai.CreativeMagicSpell;

import com.mojang.logging.LogUtils;
import com.outlook.shi_jing_kai.CreativeMagicSpell.Blocks.ModBlock;
import com.outlook.shi_jing_kai.CreativeMagicSpell.Items.ModItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(CreativeMagicSpellMod.MOD_ID)
public class CreativeMagicSpellMod {

    public static final String MOD_ID = "creativemagicmod";

    // might need this logger...... well the example file kept it, guess no harm keeping it.
    public static final Logger LOGGER = LogUtils.getLogger();

    // Register all add-on classes in this mod
    public CreativeMagicSpellMod(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItem.register(modEventBus);
        ModBlock.register(modEventBus);
    }
}
