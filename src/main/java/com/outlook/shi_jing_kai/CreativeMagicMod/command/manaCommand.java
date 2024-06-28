package com.outlook.shi_jing_kai.CreativeMagicMod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerMana;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class manaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("mana")
                .then(Commands.literal("give").then(Commands.argument("amount", integer()).executes(manaCommand::giveMana)))
                .then(Commands.literal("show").executes(manaCommand::showMana)));
    }

    // give some amount of mana as specified by the command to the specified player
    private static int giveMana(CommandContext<CommandSourceStack> context){
        if(context.getSource().getEntity() instanceof Player player){
            int amount = getInteger(context, "amount");
            Player curPlayer = (Player) context.getSource().getEntity();
            PlayerMana cur_mana = curPlayer.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
            if(cur_mana != null){
                cur_mana.addMana(amount);
                player.sendSystemMessage(Component.literal("Mana granted! Amount granted:"));
                player.sendSystemMessage(Component.literal(String.valueOf(amount)));
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int showMana(CommandContext<CommandSourceStack> context){
        if(context.getSource().getEntity() instanceof Player player){
            Player curPlayer = (Player) context.getSource().getEntity();
            PlayerMana cur_mana = curPlayer.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
            if(cur_mana != null){
                int cur_mana_amount = cur_mana.getMana();
                int cur_max_mana = cur_mana.getMaxMana();
                player.sendSystemMessage(Component.literal("Current Mana Remaining:"));
                player.sendSystemMessage(Component.literal(String.valueOf(cur_mana_amount)));
                player.sendSystemMessage(Component.literal("With Maximal Mana:"));
                player.sendSystemMessage(Component.literal(String.valueOf(cur_max_mana)));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
