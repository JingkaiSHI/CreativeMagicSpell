package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UseManaC2SPacket {

    private final int manaUsed;

    public UseManaC2SPacket(int manaUsed) {
        this.manaUsed = manaUsed;

    }

    public static void encode(UseManaC2SPacket packet, FriendlyByteBuf buf){
        buf.writeInt(packet.manaUsed);
        System.out.println("Encoding Used Mana Amount to Server: " + packet.manaUsed);
    }

    public static UseManaC2SPacket decode(FriendlyByteBuf buffer){
        int manaUsed = buffer.readInt();
        System.out.println("Decoding packet data on amount of mana to be used: " + manaUsed);
        return new UseManaC2SPacket(manaUsed);
    }

    public static void handle(UseManaC2SPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if(player != null){
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    mana.useMana(packet.manaUsed);
                    System.out.println("Used Mana amount: " + packet.manaUsed);
                });
            }
        });
        context.get().setPacketHandled(true);
    }

}
