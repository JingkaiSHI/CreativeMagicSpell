package com.outlook.shi_jing_kai.CreativeMagicMod.Mana;

import net.minecraft.nbt.CompoundTag;

public class PlayerMana {
    private final int MIN_MANA = 0;



    // defines initial value of Mana for a player as a proportion of MAX_MANA_CURRENT
    private final double MANA_INITIAL = 0.4;

    // set initial value of maximum mana


    // max mana available to be increased to
    // Phase 1 - Overworld
    private final int MAX_MANA_FINAL_OVERWORLD = 1000;
    // Phase 2 - Nether
    private final int MAX_MANA_FINAL_NETHER = 5000;
    // Phase 3 - the End
    private final int MAX_MANA_FINAL_END = 20000;

    private int COOL_DOWN_TIMER = 5;
    private int MAX_MANA_FINAL_CURRENT = MAX_MANA_FINAL_OVERWORLD;

    private int MAX_MANA_CURRENT = 100;
    private int mana;
    // phase indicate the phase in player's mana's logic:
    // phase = 0: overworld
    // phase = 1: nether
    // phase = 2: end
    private int phase = 0;

    // Default constructor of a player's Mana with default value;
    public PlayerMana(){
        resetMana();
    }

    public void resetMana(){
        this.mana = (int) (MANA_INITIAL * MAX_MANA_CURRENT);
    }

    // WARNING: this function needs to be changed if decided that more phases needed to be added!!!
    // progressPhase: used to progress a player's Mana max phase
    public void progressPhase(){
        if(this.phase == 0){
            this.MAX_MANA_CURRENT = MAX_MANA_FINAL_NETHER;
        }else {
            this.MAX_MANA_CURRENT = MAX_MANA_FINAL_END;
        }
    }

    public int getMana(){
        return this.mana;
    }

    public void setMana(int mana){
        this.mana = Math.min(mana, this.MAX_MANA_CURRENT);
    }

    public void setMaxMana(int maxMana){
        this.MAX_MANA_CURRENT = Math.min(maxMana, this.MAX_MANA_FINAL_CURRENT);
    }

    public void addMana(int add){
        this.mana = Math.min(mana + add, MAX_MANA_CURRENT);
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

    public int getPhase(){
        return this.phase;
    }

    public void copyFrom(PlayerMana source){
        this.mana = source.mana;
        this.phase = source.phase;
        this.MAX_MANA_CURRENT = source.MAX_MANA_CURRENT;
        this.MAX_MANA_FINAL_CURRENT = source.MAX_MANA_FINAL_CURRENT;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("mana", this.mana);
        nbt.putInt("phase", this.phase);
        nbt.putInt("max_mana_current", this.MAX_MANA_CURRENT);
        nbt.putInt("max_mana_final_current", this.MAX_MANA_FINAL_CURRENT);
    }

    public void loadNBTData(CompoundTag nbt){
        this.mana = nbt.getInt("mana");
        this.phase = nbt.getInt("phase");
        this.MAX_MANA_CURRENT = nbt.getInt("max_mana_current");
        this.MAX_MANA_FINAL_CURRENT = nbt.getInt("max_mana_final_current");
    }

    // Get the current max mana limit
    public int getMaxMana() {
        return this.MAX_MANA_CURRENT;
    }

    // Increase the max mana limit if it does not exceed the final max limit
    public int increaseMaxMana(int increase) {
        int status = 1;
        this.MAX_MANA_CURRENT = Math.min(MAX_MANA_CURRENT + increase, MAX_MANA_FINAL_CURRENT);
        if(MAX_MANA_CURRENT + increase >= MAX_MANA_FINAL_CURRENT){
            status = 0;
        }
        return status;
    }

    public int getCooldownTimer(){
        return this.COOL_DOWN_TIMER;
    }

    public void setCooldownTimer(int time){
        this.COOL_DOWN_TIMER = time;
    }
}
