package com.chame.passwordtenshi.commands;

public class CommandUnregister{

/*     private final PasswordTenshi pt;

    public CommandUnregister(PasswordTenshi pt) {
        this.pt = pt;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, 
                             String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ConfigFile.getLocal("console.console_not_allowed"));
            
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(pt, () -> {

            Player player = (Player) sender;
            PlayerSession playersession 
                    = PlayerStorage
                    .getPlayerSession(player.getUniqueId());

            try {
                playersession.removePasswordHash();
                sender.sendMessage(
                        ConfigFile.getLocal("unregister.player_unregister"));
                
                playersession.setAuthorized(false);
                playersession.setGamemode(player.getGameMode());
                Bukkit.getScheduler().runTask(pt, () -> 
                        player.setGameMode(GameMode.SPECTATOR));
                
                player.sendMessage(
                        ConfigFile.getLocal("unregister.register_again"));
                
                playersession.registerLoginReminder();
                
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        return true;
    } */
}
