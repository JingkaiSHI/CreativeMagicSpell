package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncManaC2SPacket {
    private final int mana;
    private final int maxMana;

    public SyncManaC2SPacket(int mana, int maxMana) {
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public static void encode(SyncManaC2SPacket packet, FriendlyByteBuf buffer){
        buffer.writeInt(packet.mana);
        buffer.writeInt(packet.maxMana);
    }
    public static SyncManaC2SPacket decode(FriendlyByteBuf buffer){
        return new SyncManaC2SPacket(buffer.readInt(), buffer.readInt());
    }

    public static void handle(SyncManaC2SPacket packet, Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() ->{
           Player player = Minecraft.getInstance().player;
           if(player != null){
               player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                   mana.setMana(packet.mana);
                   mana.setMaxMana(packet.maxMana);
               });
           }
        });
        context.get().setPacketHandled(true);
    }
}
