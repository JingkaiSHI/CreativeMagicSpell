package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.ElementalTouch;

import com.outlook.shi_jing_kai.CreativeMagicMod.Error.InvalidEffectError;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.SpellTouch;

import java.util.ArrayList;
import java.util.List;

import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.*;
import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.WALL;

public class WindSpellTouch extends SpellTouch {
    public List<Effects> validEffects = new ArrayList<>();
    public WindSpellTouch(Effects effect){
        this.validEffects.add(FORM_HELMET);
        this.validEffects.add(FORM_CHESTPLATE);
        this.validEffects.add(FORM_LEGGING);
        this.validEffects.add(FORM_BOOTS);
        this.validEffects.add(FORM_SWORD);
        this.validEffects.add(FORM_SHIELD);
        this.validEffects.add(PROJECTILE);
        this.validEffects.add(EMIT);
        this.validEffects.add(REALITY_MARBLE);
        this.validEffects.add(WALL);

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
}
