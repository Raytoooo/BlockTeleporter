package fr.rayto.blockteleporter.listeners;

import fr.rayto.blockteleporter.BlockTeleporter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final BlockTeleporter main;

    public PlayerInteractListener(BlockTeleporter main) {
        this.main = main;
    }

    @EventHandler
    public void onTeleporterClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if(block.getType().equals(Material.LAPIS_ORE)){
                if(main.blockTeleporter.containsKey(block.getLocation())){
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                    String command = main.customConfig.getString("BlocksData.Teleporter_" + main.blockTeleporter.get(block.getLocation()) + ".performCommand");
                    String[] strgs = command.split(" ");
                    String finalcommand = "";

                    for(int i = 0; i < strgs.length; i++){
                        Bukkit.broadcastMessage(strgs[i]);
                        if(i == 0){
                            finalcommand = (strgs[i]);
                        } else {
                            if(strgs[i].equalsIgnoreCase("<player>")){
                                finalcommand = (finalcommand + " " + (player.getName()));
                            } else{
                                finalcommand = (finalcommand + " " + strgs[i]);
                            }
                        }
                    }
                    Bukkit.broadcastMessage(finalcommand);
                    Bukkit.dispatchCommand(console, finalcommand);
                }
            }
        }
    }
}
