package com.outlook.shi_jing_kai.CreativeMagicMod.Mana;

import net.minecraft.nbt.CompoundTag;

public class PlayerMana {
    private int mana;
    private final int MIN_MANA = 0;

    // set initial value of maximum mana
    private int MAX_MANA_INITIAL = 100;

    // max mana available to be increased to
    private final int MAX_MANA_FINAL = 1000;

    public int getMana(){
        return mana;
    }

    public void addMana(int add){
        this.mana = Math.min(mana + add, MAX_MANA_INITIAL);
    }

    public void useMana(int sub){
        this.mana = Math.max(mana - sub, MIN_MANA);
    }

    public void copyFrom(PlayerMana source){
        this.mana = source.mana;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("mana", mana);
    }

    public void loadNBTData(CompoundTag nbt){
        mana = nbt.getInt("mana");
    }
}
