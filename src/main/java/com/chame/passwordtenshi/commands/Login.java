package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.utils.PasswordChecker;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

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

public class Login {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("login")
                .then(argument("password", StringArgumentType.word())
                    .executes(ctx -> {
                        String password = StringArgumentType.getString(ctx, "password");
                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());
                        String hash = playerSession.getPasswordHash();

                        if (hash == null) {
                            ctx.getSource().sendFeedback(new LiteralText("§cYou're not registered! Use /register instead."), false);
                            return 1;

                        } else if (password.equals("")  || password == null || password.length() == 0){
                            ctx.getSource().sendFeedback(new LiteralText("§cYour password cannot be empty."), false);
                            return 1;
                        } 
                        try{
                            if (PasswordChecker.check(password, hash)) {
                                playerSession.setAuthorized(true);
                                playerSession.setSurvival();
                                ctx.getSource().sendFeedback(new LiteralText("§aLogged in."), false);
                                player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:block.note_block.pling"), SoundCategory.MASTER, player.getPos(), 100f, 0f));
                                
                            } else {
                                player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:entity.zombie.attack_iron_door"), SoundCategory.MASTER, player.getPos(), 20f, 0.5f));
                                ctx.getSource().sendFeedback(new LiteralText("§cIncorrect password!"), false);
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        return 1;
        })));
    }
}