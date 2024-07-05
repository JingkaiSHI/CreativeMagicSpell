package com.outlook.shi_jing_kai.CreativeMagicMod.event;

import com.outlook.shi_jing_kai.CreativeMagicMod.Client.PlayerDataStorage;
import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerMana;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreativeMagicMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaRegenHandler {

    private static PlayerMana getPlayerMana(Player player){
        return player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player = event.player;

        PlayerMana mana = getPlayerMana(player);
        if(mana != null){
            handleManaRegeneration(player, mana);
        }
    }

    private static void handleManaRegeneration(Player player, PlayerMana mana) {
        int cooldown = mana.getCooldownTimer();

        if(cooldown > 0){
            // decrease cooldown timer
            mana.setCooldownTimer(cooldown - 1);
        }else{
            // regenerate mana
            if(mana.getMana() < mana.getMaxMana()){
                mana.addMana(1);
                CompoundTag updatedData = new CompoundTag();
                mana.saveNBTData(updatedData);
                PlayerDataStorage.savePlayerData(player.getUUID(), updatedData);
            }
        }

    }

}
