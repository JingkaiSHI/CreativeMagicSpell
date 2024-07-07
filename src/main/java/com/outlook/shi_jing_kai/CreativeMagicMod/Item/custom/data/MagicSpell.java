package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MagicSpell {
    private List<SpellTouch> components = new ArrayList<>();

    public MagicSpell(List<SpellTouch> components){
        this.components = components;
    }

    public void executeSpell(Level pLevel, Player pPlayer, InteractionHand pUseHand){
        // execute an existing spell in following steps, assuming the spell is valid (check before using this method!):
        // 1. iterate over spell touches to identify combos hit, remove those spell touches if needed (like some combos will replace the touches themselves, 'merging' touches)
        // 2. create combo magic spell touch, put them at the end of the components, modify in place
        // 3. execute each spell touch, invoke different combos methods in their respective classes
        processComboMagic();
        for(int i = 0; i < components.size(); i++){
            SpellTouch currentComponent = components.get(i);
            currentComponent.execute(pLevel, pPlayer, pUseHand);
        }
    }

    private void processComboMagic() {
        // Define the Combos to be satisfied here
        // 1. full-set-armor: triggered by all "formXXX" touches at the same time
        // 2. Same-element-concurrency: triggered by all touches are of the same elements
        // 3. fire-thunder-sync: triggered by both presence of Fire and Thunder EMIT
        // 4. reality-marble: triggered by more than 2 touches that is about REALITY MARBLE
        // TO BE EXTENDED......

        // Detect Combos

        // Remove Redundant Touches

        // add combos at the end
    }
}
