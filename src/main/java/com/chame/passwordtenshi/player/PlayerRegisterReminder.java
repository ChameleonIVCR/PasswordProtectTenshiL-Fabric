package com.chame.passwordtenshi.player;

import com.chame.passwordtenshi.PasswordTenshi;
import net.minecraft.text.LiteralText;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class PlayerRegisterReminder implements Runnable {
    private final List<PasswordTenshi.PlayerPendingLogin> playerList;

    public PlayerRegisterReminder(List<PasswordTenshi.PlayerPendingLogin> pList){
        this.playerList = pList;
    }

    @Override
    public void run() {
        for(PasswordTenshi.PlayerPendingLogin playerPending : playerList){
            ServerPlayerEntity player = playerPending.player;
            try {
                PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());
                if (playerSession == null || !player.networkHandler.getConnection().isOpen()) {
                    playerList.remove(playerPending);
                    continue;
                }
                if (playerSession.isAuthorized()) {
                    playerList.remove(playerPending);
                    continue;
                }

                if (playerPending.reminderCounter > 11) {
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
                playerList.remove(playerPending);
            }
        }
    }
}