package com.outlook.shi_jing_kai.CreativeMagicMod;

import com.mojang.logging.LogUtils;
import com.outlook.shi_jing_kai.CreativeMagicMod.Block.ModBlock;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.ModItem;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerMana;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import com.outlook.shi_jing_kai.CreativeMagicMod.Tab.ModCreativeModTabs;
import com.outlook.shi_jing_kai.CreativeMagicMod.event.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreativeMagicMod.MOD_ID)
public class CreativeMagicMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "creativemagicmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "shi_jing_kai" namespace

    public CreativeMagicMod(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);


        ModItem.register(modEventBus);
        ModBlock.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new ModEvents());
    }
}
