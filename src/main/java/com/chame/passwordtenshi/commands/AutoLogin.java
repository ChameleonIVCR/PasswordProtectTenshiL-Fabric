package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoLogin{

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("autologin")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());

                    if (playerSession.hasChangedAutoLogin()) {
                        ctx.getSource().sendFeedback(new LiteralText("§cYou can only change this setting once per game session.\n§eTo change your autologin value, please rejoin the server."), false);
                        return 1;
                    }

                    playerSession.setAutoLogin(!playerSession.isAutoLogin());
                    
                    final String feedback = playerSession.isAutoLogin() ? "activated." : "deactivated.";

                    ctx.getSource().sendFeedback(new LiteralText("§aAutologin has been "+feedback), false);

                    return 1;
                }));
    }
}
