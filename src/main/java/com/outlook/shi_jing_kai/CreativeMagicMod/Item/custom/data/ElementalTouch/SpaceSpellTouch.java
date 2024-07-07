package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.ElementalTouch;

import com.outlook.shi_jing_kai.CreativeMagicMod.Error.InvalidEffectError;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.SpellTouch;

import java.util.ArrayList;
import java.util.List;

import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.*;
import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.WALL;

public class SpaceSpellTouch extends SpellTouch {
    public List<Effects> validEffects = new ArrayList<>();
    public SpaceSpellTouch(Effects effect){
        this.validEffects.add(CONFUSE);
        this.validEffects.add(CONSTRAINT);
        this.validEffects.add(WALL);
        this.validEffects.add(ERASE);

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
