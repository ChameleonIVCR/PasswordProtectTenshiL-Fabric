package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;


public class UnregisterPlayer{

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("unregisterplayer")
                .requires(source -> source.hasPermissionLevel(2))
                    .then(argument("player", StringArgumentType.word())
                        .executes(ctx -> {
                            ServerPlayerEntity targetPlayer = ctx.getSource().getMinecraftServer().getPlayerManager().getPlayer(StringArgumentType.getString(ctx, "player"));

                            if (targetPlayer == null){
                                ctx.getSource().sendFeedback(new LiteralText("§cThe specified player does not exist or is not online."), false);
                                return 1;
                            }

                            PlayerSession playerSession = PlayerStorage.getPlayerSession(targetPlayer.getUuid());

                            if (playerSession == null){
                                //This could be solved by creating a PlayerSession without using the PlayerStorage.
                                ctx.getSource().sendFeedback(new LiteralText("§cThe specified player has not been found in the player storage cache. Try to have them rejoin."), false);
                                return 1;
                            }

                            playerSession.removePasswordHash();
                            playerSession.setAuthorized(false);
                            playerSession.setSpectator();

                            targetPlayer.sendMessage(new LiteralText("§aYou have been unregistered by an administrador. Your login credentials have been erased. Please register again."), false);
                            ctx.getSource().sendFeedback(new LiteralText("§aThe user credentials have been removed, and the user has been deauthorized."), false);
                            playerSession.authReminder();

                            return 1;
        })));
    }
}