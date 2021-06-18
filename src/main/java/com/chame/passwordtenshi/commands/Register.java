package com.chame.passwordtenshi.commands;


import com.chame.passwordtenshi.PasswordTenshi;
import com.chame.passwordtenshi.utils.PasswordChecker;
import com.chame.passwordtenshi.utils.ConfigFile;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

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
