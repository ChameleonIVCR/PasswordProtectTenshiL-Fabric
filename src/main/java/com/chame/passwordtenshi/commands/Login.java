package com.chame.passwordtenshi.commands;

import com.chame.passwordtenshi.utils.PasswordChecker;
import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class Login {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("login")
                .then(argument("password", word())
                    .executes(Login::run)));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final String password = getString(context, "password");
        ServerPlayerEntity player = source.getPlayer();
        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());

        if (playerSession == null) return 1;

        if (playerSession.isAuthorized()) {
            source.sendFeedback(new LiteralText("§cYou are already authorized."), false);
            return 1;
        }

        String hash = playerSession.getPasswordHash();

        if (hash == null) {
            source.sendFeedback(new LiteralText("§cYou're not registered! Use /register instead."), false);
            return 1;
        } else if (password.length() == 0){
            source.sendFeedback(new LiteralText("§cYour password cannot be empty."), false);
            return 1;
        }

        try {
            if (PasswordChecker.check(password, hash)) {
                playerSession.setAuthorized(true);
                playerSession.setSurvival();
                source.sendFeedback(new LiteralText("§aLogged in"), false);
                player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:block.note_block.pling"), SoundCategory.MASTER, player.getPos(), 100f, 0f));

            } else {
                player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:entity.zombie.attack_iron_door"), SoundCategory.MASTER, player.getPos(), 20f, 0.5f));
                source.sendFeedback(new LiteralText("§cIncorrect password"), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            playerSession.removeUser();
            playerSession.setAuthorized(false);
            playerSession.getPlayer().networkHandler.disconnect(new LiteralText("Your password has been reset, please rejoin and register again."));
        }

        if (playerSession.wasDead()) {
            playerSession.getPlayer().networkHandler.disconnect(new LiteralText("As you've been respawned, please join again."));
        }

        return 1;
    }
}