package com.chame.passwordtenshi.player;

import com.chame.passwordtenshi.PasswordTenshi;
import net.minecraft.text.LiteralText;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PlayerRegisterReminder implements Runnable {
    public PlayerRegisterReminder(){
    }

    @Override
    public void run() {
        List<PasswordTenshi.PlayerPendingLogin> playerList = PasswordTenshi.getPlayersPendingLogin();
        ListIterator<PasswordTenshi.PlayerPendingLogin> playerIterator = playerList.listIterator();

        while (playerIterator.hasNext()){
            PasswordTenshi.PlayerPendingLogin playerPending = playerIterator.next();
            ServerPlayerEntity player = playerPending.player;
            try {
                PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());
                if (playerSession == null || !player.networkHandler.getConnection().isOpen()) {
                    playerIterator.remove();
                    continue;
                }
                if (playerSession.isAuthorized()) {
                    playerIterator.remove();
                    continue;
                }

                if (playerPending.reminderCounter > 9) {
                    player.networkHandler.disconnect(new LiteralText(
                            "You took too long to authenticate, please try again."
                    ));
                }
                playerPending.reminderCounter++;

                final String passwordHash = playerSession.getPasswordHash();

                if (passwordHash == null) {
                    player.sendMessage(
                            new LiteralText("§3Please register by using §c/register.\n§3E.g. §c/register <password>"),
                            false
                    );
                } else {
                    player.sendMessage(
                            new LiteralText("§3To continue, please login by using §c/login."),
                            false
                    );
                }
            } catch (Exception e) {
                playerIterator.remove();
            }
        }
    }
}