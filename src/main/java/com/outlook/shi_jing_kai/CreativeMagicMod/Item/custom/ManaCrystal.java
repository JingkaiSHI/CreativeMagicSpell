package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom;

import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerMana;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.network.chat.Component;
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
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound((Player)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        PlayerMana currentPlayerMana = pPlayer.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        if(currentPlayerMana != null){
            currentPlayerMana.increaseMaxMana(20);
        }
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        pPlayer.sendSystemMessage(Component.literal("Max Mana granted! Amount granted: 20"));
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}
