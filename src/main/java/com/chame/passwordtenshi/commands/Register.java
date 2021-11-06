package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.utils.PasswordChecker;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class Register {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("register")
                .then(argument("password", StringArgumentType.word())
                    .executes(Register::run)));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final String password = StringArgumentType.getString(context, "password");
        ServerPlayerEntity player = source.getPlayer();
        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());

        if (playerSession == null) return 1;

        if (password.length() == 0) {
            source.sendFeedback(new LiteralText("§cYour password cannot be empty."), false);
            return 1;

        } else if (playerSession.isAuthorized()) {
            source.sendFeedback(new LiteralText("§cYou are already authorized."), false);
            return 1;

        } else if (playerSession.getPasswordHash() != null) {
            source.sendFeedback(new LiteralText("§cYou're already registered."), false);
            return 1;
        }

        try{
            String hash = PasswordChecker.getSaltedHash(password);
            playerSession.addUser(hash);
            playerSession.setAuthorized(true);
            playerSession.setSurvival();
        } catch (Exception i){
            i.printStackTrace();
        }

        source.sendFeedback(new LiteralText("§aWelcome."), false);
        return 1;
    }
}
