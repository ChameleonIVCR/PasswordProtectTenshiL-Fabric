package com.chame.passwordtenshi.commands;

public class CommandUnregisterPlayer {

/*     private final PasswordTenshi pt;

    public CommandUnregisterPlayer(PasswordTenshi pt) {
        this.pt = pt;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, 
                             String label, String[] args) {
        
         if (!(sender instanceof Player) 
                 && !(sender instanceof ConsoleCommandSender)) {
             
            sender.sendMessage(
                    ConfigFile.getLocal(
                            "unregisterplayer.unrecognized_execution"));
            
            return true;
         }

         if (!(sender instanceof ConsoleCommandSender) && !sender.isOp()) {
             sender.sendMessage(
                     ConfigFile.getLocal("unregisterplayer.player_not_op"));
             
             return true;
         }

         if (args.length == 0) {
             sender.sendMessage(
                     ConfigFile.getLocal("unregisterplayer.no_arguments"));
             
             return false;
         }

        Bukkit.getScheduler().runTaskAsynchronously(pt, () -> {

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(
                        ConfigFile.getLocal("unregisterplayer.player_not_online"));
                return;
            }

            PlayerSession playersession = PlayerStorage.getPlayerSession(player.getUniqueId());

            try {
                playersession.removePasswordHash();
                playersession.setAuthorized(false);
                sender.sendMessage(
                        ConfigFile.getLocal(
                                "unregisterplayer.successful_unregister"));
                
                player.sendMessage(
                        ConfigFile.getLocal(
                                "unregisterplayer.target_player_unregister"));
                
                player.sendMessage(
                        ConfigFile.getLocal(
                                "unregister.register_again"));
                
                playersession.setGamemode(player.getGameMode());
                Bukkit.getScheduler().runTask(pt, () -> 
                        player.setGameMode(GameMode.SPECTATOR));

                playersession.registerLoginReminder();
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        return true;
    } */
}
