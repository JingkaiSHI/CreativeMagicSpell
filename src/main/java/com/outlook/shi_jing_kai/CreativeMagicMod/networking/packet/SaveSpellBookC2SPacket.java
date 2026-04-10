package com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet;

import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.SpellBookItem;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SaveSpellBookC2SPacket {
    private final InteractionHand hand;
    private final List<Effects> effects;

    public SaveSpellBookC2SPacket(InteractionHand hand, List<Effects> effects) {
        this.hand = hand;
        this.effects = effects;
    }

    public static void encode(SaveSpellBookC2SPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.hand);
        buf.writeInt(packet.effects.size());
        for (Effects effect : packet.effects) {
            buf.writeUtf(effect.name());
        }
    }

    public static SaveSpellBookC2SPacket decode(FriendlyByteBuf buf) {
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        int size = buf.readInt();
        List<Effects> effects = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            effects.add(Effects.valueOf(buf.readUtf()));
        }
        return new SaveSpellBookC2SPacket(hand, effects);
    }

    public static void handle(SaveSpellBookC2SPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            ItemStack heldItem = player.getItemInHand(packet.hand);
            if (!SpellBookItem.isSpellBook(heldItem)) {
                player.sendSystemMessage(Component.literal("Hold a spell book before saving a spell."));
                return;
            }

            if (packet.effects.isEmpty()) {
                player.sendSystemMessage(Component.literal("No rune effects are queued for this spell yet."));
                return;
            }

            SpellBookItem.storeEffects(heldItem, packet.effects);
            player.sendSystemMessage(Component.literal("Saved spell book: " + SpellBookItem.describeEffects(packet.effects)));
        });
        context.setPacketHandled(true);
    }
}
