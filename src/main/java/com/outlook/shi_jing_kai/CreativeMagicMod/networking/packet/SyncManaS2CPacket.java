package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncManaS2CPacket {
    private final CompoundTag data;

    public SyncManaS2CPacket(CompoundTag data) {
        this.data = data;
    }

    public static void encode(SyncManaS2CPacket packet, FriendlyByteBuf buffer){
        buffer.writeNbt(packet.data);
        System.out.println("Encoding packet data: " + packet.data);
    }
    public static SyncManaS2CPacket decode(FriendlyByteBuf buffer){
        CompoundTag data = buffer.readNbt();
        System.out.println("Decoding packet data: " + data);
        return new SyncManaS2CPacket(data);
    }

    public static void handle(SyncManaS2CPacket packet, Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() ->{
           Player player = Minecraft.getInstance().player;
           if(player != null){
               player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                   System.out.println("Client received mana data: " + packet.data);
                   mana.loadNBTData(packet.data);
               });
           }
        });
        context.get().setPacketHandled(true);
    }
}
