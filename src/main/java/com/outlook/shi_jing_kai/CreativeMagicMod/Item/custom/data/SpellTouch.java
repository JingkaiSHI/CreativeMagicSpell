package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class SpellTouch {

    public Effects effect;
    public Effects[] validEffects;

    public void execute(Level pLevel, Player pPlayer, InteractionHand pUseHand){

        switch (this.effect){
            case FORM_HELMET:
            case FORM_CHESTPLATE:
            case FORM_LEGGING:
            case FORM_BOOTS:
            case FORM_SWORD:
            case FORM_SHIELD:
            case PROJECTILE:
            case EMIT:
            case REALITY_MARBLE:
            case WALL:
            case ENHANCE:
            case CONSTRAINT:
            case CONFUSE:
            case ACCELERATE:
            case SLOW:
            case STOP:
            case ERASE:
            default:
        }

    }

    public abstract void formHelmet(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formChestPlate(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formLegging(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formBoots(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formSword(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formShield(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void createProjectile(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void emit(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formRealityMarble(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void formWall(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void enhance(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void constraint(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void confuse(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void accelerate(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void stop(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void slow(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public abstract void erase(Level pLevel, Player pPlayer, InteractionHand pUseHand);

    public void handleMeaninglessTouches(){

    }
}
