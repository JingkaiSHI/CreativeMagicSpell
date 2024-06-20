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

    public int useMana(int sub){
        int mana_penalty = 0;
        if (this.mana < sub){
            mana_penalty = sub - this.mana;
            this.mana = MIN_MANA;
        } else {
            this.mana = mana - sub;
        }
        return mana_penalty;
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

    // Get the current max mana limit
    public int getMaxMana() {
        return MAX_MANA_INITIAL;
    }

    // Increase the max mana limit if it does not exceed the final max limit
    public void increaseMaxMana(int increase) {
        this.MAX_MANA_INITIAL = Math.min(MAX_MANA_INITIAL + increase, MAX_MANA_FINAL);
    }
}
