package com.outlook.shi_jing_kai.CreativeMagicMod.Client;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataStorage {
    private static final Map<UUID, CompoundTag> playerDataMap = new HashMap<>();

    public static void savePlayerData(UUID playerID, CompoundTag data){
        playerDataMap.put(playerID, data);
    }

    public static CompoundTag loadPlayerData(UUID playerID){
        return playerDataMap.get(playerID);
    }
}
