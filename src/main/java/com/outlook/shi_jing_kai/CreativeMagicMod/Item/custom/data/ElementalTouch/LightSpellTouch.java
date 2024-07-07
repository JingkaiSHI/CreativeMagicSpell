package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.ElementalTouch;

import com.outlook.shi_jing_kai.CreativeMagicMod.Error.InvalidEffectError;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.SpellTouch;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.*;
import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.WALL;

public class LightSpellTouch extends SpellTouch {

    public List<Effects> validEffects = new ArrayList<>();
    public LightSpellTouch(Effects effect){
        this.validEffects.add(FORM_HELMET);
        this.validEffects.add(FORM_CHESTPLATE);
        this.validEffects.add(FORM_LEGGING);
        this.validEffects.add(FORM_BOOTS);
        this.validEffects.add(FORM_SWORD);
        this.validEffects.add(FORM_SHIELD);
        this.validEffects.add(PROJECTILE);
        this.validEffects.add(REALITY_MARBLE);
        this.validEffects.add(WALL);
        this.validEffects.add(ENHANCE);

        try{
            if(this.validEffects.contains(effect)){
                this.effect = effect;
            }else{
                throw new InvalidEffectError();
            }
        }catch (InvalidEffectError e){
            System.out.println("Invalid Effect assigned to this element!");
            // Other logic for handling wrong effect assigning
        }

    }

    @Override
    public void formHelmet(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formChestPlate(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formLegging(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formBoots(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formSword(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formShield(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void createProjectile(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void emit(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formRealityMarble(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void formWall(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void enhance(Level pLevel, Player pPlayer, InteractionHand pUseHand) {

    }

    @Override
    public void constraint(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        this.handleMeaninglessTouches();
    }

    @Override
    public void confuse(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        this.handleMeaninglessTouches();
    }

    @Override
    public void accelerate(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        this.handleMeaninglessTouches();
    }

    @Override
    public void stop(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        this.handleMeaninglessTouches();
    }

    @Override
    public void slow(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        this.handleMeaninglessTouches();
    }

    @Override
    public void erase(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        this.handleMeaninglessTouches();
    }
}
