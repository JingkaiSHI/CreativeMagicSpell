package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.ElementalTouch;

import com.outlook.shi_jing_kai.CreativeMagicMod.Error.InvalidEffectError;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.SpellTouch;

import java.util.ArrayList;
import java.util.List;

import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.*;
import static com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects.ERASE;

public class TimeSpellTouch extends SpellTouch {
    public List<Effects> validEffects = new ArrayList<>();
    public TimeSpellTouch(Effects effect){
        this.validEffects.add(STOP);
        this.validEffects.add(SLOW);
        this.validEffects.add(ACCELERATE);
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
