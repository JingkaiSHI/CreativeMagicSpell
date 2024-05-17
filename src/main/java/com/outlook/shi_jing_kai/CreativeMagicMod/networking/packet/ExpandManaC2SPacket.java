package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExpandManaC2SPacket {
    private static final String MESSAGE_EXPAND_MANA = "message.creativemagicmod.expand_mana";
    private static final String MAX_MANA_REACHED = "message.creativemagicmod.max_mana_reached";
    public ExpandManaC2SPacket() {

    }

    public ExpandManaC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            // EntityType.COW.spawn(level, null, null, player.blockPosition(),
            //         MobSpawnType.COMMAND, true, false);
        });
        return true;
    }

}
