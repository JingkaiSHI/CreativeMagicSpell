package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.SpellBookItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DiscardSpellBookC2SPacket {
    private final InteractionHand hand;

    public DiscardSpellBookC2SPacket(InteractionHand hand) {
        this.hand = hand;
    }

    public static void encode(DiscardSpellBookC2SPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.hand);
    }

    public static DiscardSpellBookC2SPacket decode(FriendlyByteBuf buf) {
        return new DiscardSpellBookC2SPacket(buf.readEnum(InteractionHand.class));
    }

    public static void handle(DiscardSpellBookC2SPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            ItemStack heldItem = player.getItemInHand(packet.hand);
            if (!SpellBookItem.isSpellBook(heldItem)) {
                player.sendSystemMessage(Component.literal("Hold a spell book before discarding a spell."));
                return;
            }

            SpellBookItem.clearStoredEffects(heldItem);
            player.sendSystemMessage(Component.literal("Cleared the stored spell from the spell book."));
        });
        context.setPacketHandled(true);
    }
}
