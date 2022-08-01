package fr.rayto.blockteleporter.commands;

import fr.rayto.blockteleporter.BlockTeleporter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BlockTeleporterCommand implements CommandExecutor {

    private final BlockTeleporter main;
    public BlockTeleporterCommand(BlockTeleporter main) {
        this.main = main;
    }
    @SuppressWarnings("deprecated")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(player.hasPermission("bt.*")){
                if(args.length >= 1) {

                    Block targetBlock = player.getTargetBlock(null, 6);

                    if(targetBlock.getType().equals(Material.LAPIS_ORE)){
                        if(!main.blockTeleporter.containsKey(targetBlock.getLocation())) {
                            if(main.customConfig.get("BlocksData.Teleporter_1") != null){
                                int len = main.customConfig.getConfigurationSection("BlocksData").getKeys(false).size();
                                String str = "";
                                for(int i = 0; i < args.length ; i++){
                                    if(i == 0){
                                        str = (args[i]);
                                    } else {
                                        str = (str + " " +  args[i]);
                                    }
                                }
                                main.AddEntry(len+1, targetBlock, str);
                            } else {
                                String str = "";
                                for(int i = 0; i < args.length ; i++){
                                    if(i == 0){
                                        str = (args[i]);
                                    } else {
                                        str = (str + " " +  args[i]);
                                    }
                                }
                                main.AddEntry(1, targetBlock, str);
                            }
                            player.sendMessage(ChatColor.GREEN + "Le téléporteur a été définis avec succès");
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "Erreur: Ce block est déjà un téléporteur.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Erreur: Vous ne regardez pas un block de type téléporteur.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /bt <performCommand>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Erreur: Vous n'avez pas la permission d'executer cette commande. !");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Erreur: Vous devez être un joueur pour exécuter cette commande !");

        }
        return false;
    }



}
