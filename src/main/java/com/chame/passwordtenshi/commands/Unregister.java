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

                    playerSession.removeUser();
                    playerSession.setAuthorized(false);
                    playerSession.getPlayer().networkHandler.disconnect(new LiteralText("Successfully unregistered!, please rejoin and register again!"));

                    return 1;
                }));
    }
}
