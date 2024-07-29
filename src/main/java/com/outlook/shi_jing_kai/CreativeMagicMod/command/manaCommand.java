package com.outlook.shi_jing_kai.CreativeMagicMod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.outlook.shi_jing_kai.CreativeMagicMod.Client.PlayerDataStorage;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerMana;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.ModMessages;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet.SyncManaS2CPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.arguments.EntityArgument.player;

public class manaCommand {
    // define client side command for testing client side code for mana usage
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("mana")
                .then(Commands.literal("give").then(Commands.argument("amount", integer()).executes(manaCommand::giveMana)))
                .then(Commands.literal("show").executes(manaCommand::showMana))
                .then(Commands.literal("use").then(Commands.argument("amount", integer()).executes(manaCommand::useMana)))
                .then(Commands.literal("reset").executes(manaCommand::resetMana)));
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

    private static int useMana(CommandContext<CommandSourceStack> context){
        return Command.SINGLE_SUCCESS;
    }


    private static int resetMana(CommandContext<CommandSourceStack> context){
        if(context.getSource().getEntity() instanceof Player player){
            Player curPlayer = player;
            PlayerMana playerMana = curPlayer.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
            if(playerMana != null){
                playerMana = new PlayerMana();
                playerMana.setMaxMana(100);
                playerMana.setPhase(0);
                System.out.println("Reset Complete! Current Player's Max mana: " + playerMana.getMaxMana());
                System.out.println("Reset Complete! Current Player's mana: " + playerMana.getMana());
                System.out.println("Reset Complete! Current Player's mana phase: " + playerMana.getPhase());
                CompoundTag curManaData = new CompoundTag();
                playerMana.saveNBTData(curManaData);
                if(curPlayer.level() instanceof ServerLevel){
                    ServerLevel curWorld = (ServerLevel) curPlayer.level();
                    long worldID = curWorld.getSeed();
                    String curWorldId = Long.toString(worldID);
                    String playerID = curPlayer.getUUID().toString();
                    String tupleID = playerID + ":" + curWorldId;
                    PlayerDataStorage.savePlayerData(tupleID, curManaData);
                    System.out.println("Saving into existing Server world");
                }

                player.sendSystemMessage(Component.literal("Reset Complete, please kill your current player to complete the MANA reset procedure!"));
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static void syncManaDataWithClient(Player player){
        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            CompoundTag tag = new CompoundTag();
            mana.saveNBTData(tag);
            System.out.println("syncing mana data with client: " + tag);
            ModMessages.sendToClient(new SyncManaS2CPacket(tag), (ServerPlayer) player);
        });
    }
}
