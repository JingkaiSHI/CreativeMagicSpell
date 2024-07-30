package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GiveManaC2SPacket {
    private final int manaGiven;

    public GiveManaC2SPacket(int manaGiven) {
        this.manaGiven = manaGiven;

    }

    public static void encode(GiveManaC2SPacket packet, FriendlyByteBuf buf){
        buf.writeInt(packet.manaGiven);
        System.out.println("Encoding Used Mana Amount to Server: " + packet.manaGiven);
    }

    public static GiveManaC2SPacket decode(FriendlyByteBuf buffer){
        int manaGiven = buffer.readInt();
        System.out.println("Decoding packet data on amount of mana to be used: " + manaGiven);
        return new GiveManaC2SPacket(manaGiven);
    }

    public static void handle(GiveManaC2SPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if(player != null){
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    mana.addMana(packet.manaGiven);
                    System.out.println("added Mana amount: " + packet.manaGiven);
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}
