package com.outlook.shi_jing_kai.CreativeMagicMod.event;

import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = CreativeMagicMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){

        }
    }
}
