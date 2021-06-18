package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.utils.PasswordChecker;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class Register {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("register")
                .then(argument("password", StringArgumentType.word())
                    .executes(ctx -> {
                        String password = StringArgumentType.getString(ctx, "password");
                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());

                        if (password.length() == 0) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYour password cannot be empty."), false);
                            return 1;

                        } else if (playerSession.isAuthorized()) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYou're already authorized."), false);
                            return 1;

                        } else if (playerSession.getPasswordHash() != null) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYou're already registered."), false);
                            return 1;
                        }

                        try{
                            String hash = PasswordChecker.getSaltedHash(password);
                            playerSession.setPasswordHash(hash);
                            playerSession.setAuthorized(true);
                            playerSession.setSurvival();
                        } catch (Exception i){
                            i.printStackTrace();
                        }
                        ctx.getSource().sendFeedback(new LiteralText("§aWelcome."), false);
                        return 1;
        })));
    }
}
