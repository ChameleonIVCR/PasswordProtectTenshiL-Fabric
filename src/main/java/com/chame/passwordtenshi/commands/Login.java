package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.PasswordChecker;
import com.chame.passwordtenshi.PasswordTenshi;
import com.chame.passwordtenshi.utils.PasswordChecker;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import java.util.logging.Logger;
import java.util.Collections;
import java.util.List;

public class Login {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("login")
                .then(argument("password", StringArgumentType.word())
                    .executes(ctx -> {
                        String password = StringArgumentType.getString(ctx, "password");
                        //String username = ctx.getSource().getPlayer().getEntityName();
                        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUUID());
                        String hash = playerSession.getPasswordHash();

                        if (hash == null) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYou're not registered! Use /register instead."), false);

                        } else if (password.equals("")  || password == null){
                            ctx.getSource().sendFeedback(new LiteralText("§cYour password cannot be empty."), false);

                        } else if (PasswordChecker.check(password, hash)) {
                            playerSession.setAuthorized(true);
                            playerSession.setGameMode("SURVIVAL");
                            ctx.getSource().sendFeedback(new LiteralText("§aLogged in."), false);
                            player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:block.note_block.pling"), SoundCategory.MASTER, player.getPos(), 100f, 0f));
                            
                        } else {
                            player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:entity.zombie.attack_iron_door"), SoundCategory.MASTER, player.getPos(), 100f, 0.5f));
                            ctx.getSource().sendFeedback(new LiteralText("§cIncorrect password!"), false);
                        }
                        return 1;
        })));
    }
}