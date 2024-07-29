package com.outlook.shi_jing_kai.CreativeMagicMod.Client;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataStorage {
    private static final Map<String, CompoundTag> playerDataMap = new HashMap<>();

    public static void savePlayerData(String IDTuple, CompoundTag data){
        playerDataMap.put(IDTuple, data);
    }

    public static CompoundTag loadPlayerData(String IDTuple){
        System.out.println("Loading Player Data with tuple ID: " + IDTuple);
        return playerDataMap.get(IDTuple);
    }
}
