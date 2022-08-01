package fr.rayto.blockteleporter.listeners;

import fr.rayto.blockteleporter.BlockTeleporter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final BlockTeleporter main;
    public BlockBreakListener(BlockTeleporter main) {
        this.main = main;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(player.hasPermission("bt.*")){
            if(block.getType().equals(Material.LAPIS_ORE)){
                if(main.blockTeleporter.containsKey(block.getLocation())){
                    main.customConfig.set("BlocksData.Teleporter_"+main.blockTeleporter.get(block.getLocation()), null);
                    player.sendMessage("§aSytème » §7Vous avez détruit le téléporteur numéro " + main.blockTeleporter.get(block.getLocation()) + " .");

                    main.RemoveEntry(main.blockTeleporter.get(block.getLocation()));
                    main.blockTeleporter.remove(block.getLocation());

                }
            }
        } else {
            event.setCancelled(true);
            player.sendMessage("§cVous n'avez pas la permission de détruire le téléporteur !");
        }
    }
}
