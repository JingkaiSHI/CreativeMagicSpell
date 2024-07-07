package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data;

public abstract class SpellTouch {

    public Effects effect;
    public Effects[] validEffects;

    public void execute(){

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
            default:
        }

    }
}
