package com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom;

import com.mojang.logging.LogUtils;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SpellBookItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String SPELL_EFFECTS_TAG = "spell_effects";

    public SpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        List<Effects> storedEffects = getStoredEffects(itemStack);

        if (storedEffects.isEmpty()) {
            if (!pLevel.isClientSide) {
                pPlayer.sendSystemMessage(Component.literal("This spell book is still blank."));
            }
            return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
        }

        String spellSummary = describeEffects(storedEffects);
        if (!pLevel.isClientSide) {
            LOGGER.info("Dummy spell cast by {} in {} with effects [{}]",
                    pPlayer.getScoreboardName(),
                    pLevel.dimension().location(),
                    spellSummary);
            pPlayer.sendSystemMessage(Component.literal("Dummy spell cast: " + spellSummary));
        }

        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        List<Effects> storedEffects = getStoredEffects(stack);
        if (storedEffects.isEmpty()) {
            tooltipComponents.add(Component.literal("Blank spell book").withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltipComponents.add(Component.literal("Stored spell: " + describeEffects(storedEffects)).withStyle(ChatFormatting.AQUA));
    }

    public static boolean isSpellBook(ItemStack stack) {
        return stack.getItem() instanceof SpellBookItem;
    }

    @Nullable
    public static InteractionHand findSpellBookHand(Player player) {
        if (isSpellBook(player.getMainHandItem())) {
            return InteractionHand.MAIN_HAND;
        }
        if (isSpellBook(player.getOffhandItem())) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public static void storeEffects(ItemStack stack, List<Effects> effects) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listTag = new ListTag();
        for (Effects effect : effects) {
            listTag.add(StringTag.valueOf(effect.name()));
        }
        tag.put(SPELL_EFFECTS_TAG, listTag);
    }

    public static void clearStoredEffects(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return;
        }

        tag.remove(SPELL_EFFECTS_TAG);
        if (tag.isEmpty()) {
            stack.setTag(null);
        }
    }

    public static List<Effects> getStoredEffects(ItemStack stack) {
        List<Effects> effects = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(SPELL_EFFECTS_TAG, Tag.TAG_LIST)) {
            return effects;
        }

        ListTag listTag = tag.getList(SPELL_EFFECTS_TAG, Tag.TAG_STRING);
        for (Tag value : listTag) {
            String rawEffect = value.getAsString();
            try {
                effects.add(Effects.valueOf(rawEffect));
            } catch (IllegalArgumentException ignored) {
                LOGGER.warn("Ignoring unknown stored spell effect {}", rawEffect);
            }
        }
        return effects;
    }

    public static String describeEffects(List<Effects> effects) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Effects effect : effects) {
            joiner.add(effect.name());
        }
        return joiner.toString();
    }
}
