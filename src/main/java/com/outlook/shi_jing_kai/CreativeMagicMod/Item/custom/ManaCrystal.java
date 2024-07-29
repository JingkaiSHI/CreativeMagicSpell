package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom;

import com.outlook.shi_jing_kai.CreativeMagicMod.Client.PlayerDataStorage;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerMana;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class ManaCrystal extends Item {
    public ManaCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand){
        System.out.println("mana crystal use method called");
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound((Player)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        PlayerMana currentPlayerMana = pPlayer.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        ///////////////// debugging part //////////////////
        int addition_status = 3;
        if(currentPlayerMana != null){
            CompoundTag updatedData = new CompoundTag();
            addition_status = currentPlayerMana.increaseMaxMana(100);
            currentPlayerMana.saveNBTData(updatedData);
            String curWorldId = "404";
            if(pLevel instanceof ServerLevel){
                ServerLevel curWorld = (ServerLevel) pLevel;
                long worldID = curWorld.getSeed();
                curWorldId = Long.toString(worldID);
            }
            String playerID = pPlayer.getUUID().toString();
            String tupleID = playerID + ":" + curWorldId;
            PlayerDataStorage.savePlayerData(tupleID, updatedData);
        }
        if(!pLevel.isClientSide){
            System.out.println("Server side execution");
            if(addition_status == 0){
                pPlayer.sendSystemMessage(Component.literal("Your body rejected the crystal, it seems it is a limit for now......"));
            }else if(addition_status == 1){
                pPlayer.sendSystemMessage(Component.literal("You feel your magic power resonate with the crystal... stronger and stronger"));
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }else{
                pPlayer.sendSystemMessage(Component.literal("Ouch! your teeth fall off!"));
            }
            System.out.println("Current Player's Max mana: " + currentPlayerMana.getMaxMana());
            System.out.println("Current Player's mana: " + currentPlayerMana.getMana());
        }
        ///////////////////////////////////////////////////

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}
