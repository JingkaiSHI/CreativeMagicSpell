package com.outlook.shi_jing_kai.CreativeMagicMod.networking;

import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet.SyncManaS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CreativeMagicMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );



    public static void registerPackets() {
        int id = 0;

        // add new messages here!

        INSTANCE.messageBuilder(SyncManaS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncManaS2CPacket::decode)
                .encoder(SyncManaS2CPacket::encode)
                .consumerMainThread(SyncManaS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToClient(Object message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
