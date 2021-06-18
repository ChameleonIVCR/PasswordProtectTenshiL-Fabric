package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.PasswordChecker;
import com.chame.passwordtenshi.PasswordTenshi;
import com.chame.passwordtenshi.utils.ConfigFile;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Register {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("register")
                .then(argument("password", StringArgumentType.word())
                    .executes(ctx -> {
                        String password = StringArgumentType.getString(ctx, "password");
                        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUUID());

                        if (password.equals("")  || password == null) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYour password cannot be empty."), false);
                            return 1;
                        } else if (playerSession.isAuthorized()) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYou're already authorized."), false);
                            return 1;
                        }

                        String hash = PasswordChecker.getSaltedHash(password);
                        
                        playerSession.setPasswordHash(hash);
                        playerSession.setAuthorized(true);
                        playerSession.setGameMode("SURVIVAL");

                        return 1;
        })));
    }
}
