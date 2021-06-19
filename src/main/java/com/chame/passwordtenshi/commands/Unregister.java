package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;

public class Unregister{

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("unregister")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());
                    
                    if (!playerSession.isAuthorized()) {
                        ctx.getSource().sendFeedback(new LiteralText("§cYou need to authenticate first."), false);
                        return 1;
                    } 

                    playerSession.removePasswordHash();
                    playerSession.setAuthorized(false);
                    playerSession.setSpectator();

                    ctx.getSource().sendFeedback(new LiteralText("§aSuccessfully unregistered! Please register again!"), false);

                    playerSession.authReminder();

                    return 1;
                }));
    }
}
