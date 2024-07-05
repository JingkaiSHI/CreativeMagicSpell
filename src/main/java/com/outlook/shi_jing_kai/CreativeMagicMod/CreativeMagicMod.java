package com.outlook.shi_jing_kai.CreativeMagicMod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import com.outlook.shi_jing_kai.CreativeMagicMod.Block.ModBlock;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.ModItem;
import com.outlook.shi_jing_kai.CreativeMagicMod.Tab.ModCreativeModTabs;
import com.outlook.shi_jing_kai.CreativeMagicMod.command.manaCommand;
import com.outlook.shi_jing_kai.CreativeMagicMod.event.ModEvents;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.ModMessages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreativeMagicMod.MOD_ID)
@Mod.EventBusSubscriber(modid = CreativeMagicMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        ModMessages.registerPackets();
    }


    @Mod.EventBusSubscriber(modid = "creativemagicmod", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void RegisterClientCommandsEvent(RegisterClientCommandsEvent event){
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            manaCommand.register(dispatcher);
            register(dispatcher);
        }

        private static void register(CommandDispatcher<CommandSourceStack> dispatcher){
            dispatcher.register(Commands.literal(CreativeMagicMod.MOD_ID));
        }
    }
}
