package fr.rayto.blockteleporter;

import fr.rayto.blockteleporter.commands.BlockTeleporterCommand;
import fr.rayto.blockteleporter.listeners.BlockBreakListener;
import fr.rayto.blockteleporter.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class BlockTeleporter extends JavaPlugin {
    public File customYml = new File(this.getDataFolder() + "/config.yml");
    public FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);

    public HashMap<Location, Integer> blockTeleporter = new HashMap<Location, Integer>();

    @Override
    public void onEnable() {
        saveCustomYml(customConfig, customYml);
        getListEntry();

        getCommand("bt").setExecutor(new BlockTeleporterCommand(this));

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[BlockTeleporter] Enabled.");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractListener(this), this);
        pm.registerEvents(new BlockBreakListener(this), this);

    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[BlockTeleporter] Disabled.");
    }

    public void AddEntry(int id, Block block, String performCommand){
        customConfig.set("BlocksData.Teleporter_" + id + ".location.world", block.getLocation().getWorld().getName());
        customConfig.set("BlocksData.Teleporter_" + id + ".location.x", block.getLocation().getX());
        customConfig.set("BlocksData.Teleporter_" + id + ".location.y", block.getLocation().getY());
        customConfig.set("BlocksData.Teleporter_" + id + ".location.z", block.getLocation().getZ());
        customConfig.set("BlocksData.Teleporter_" + id + ".performCommand", performCommand);
        blockTeleporter.put(block.getLocation(), id);
        saveCustomYml(customConfig, customYml);

    }

    public void getListEntry(){
        if(customConfig.get("BlocksData.Teleporter_1") != null){
            int i = customConfig.getConfigurationSection("BlocksData").getKeys(false).size() + 1;
            for(int id = 1 ; id <= i ; id++){
                if (customConfig.get("BlocksData.Teleporter_" + id) != null) {
                    World world = Bukkit.getWorld(customConfig.getString("BlocksData.Teleporter_" + id + ".location.world"));
                    double x = customConfig.getInt("BlocksData.Teleporter_" + id + ".location.x");
                    double y = customConfig.getInt("BlocksData.Teleporter_" + id + ".location.y");
                    double z = customConfig.getInt("BlocksData.Teleporter_" + id + ".location.z");
                    Location teleporterLocation = new Location(world, x, y, z);
                    Block block = world.getBlockAt(teleporterLocation);
                    blockTeleporter.put(teleporterLocation, id);
                }
            }
        }
    }

    public void RemoveEntry(int id){
        int i = customConfig.getConfigurationSection("BlocksData").getKeys(false).size() + 1;

        for(int n = id+1 ; n <= i ; n++){
            if (customConfig.get("BlocksData.Teleporter_" + n) != null) {
                double x = customConfig.getInt("BlocksData.Teleporter_" + n + ".location.x");
                double y = customConfig.getInt("BlocksData.Teleporter_" + n + ".location.y");
                double z = customConfig.getInt("BlocksData.Teleporter_" + n + ".location.z");
                String worldname = customConfig.getString("BlocksData.Teleporter_" + n + ".location.world");
                String performCommand = customConfig.getString("BlocksData.Teleporter_" + n + ".performCommand");
                Location loc = new Location(Bukkit.getWorld(worldname), x, y,z);
                Block block = Bukkit.getWorld(worldname).getBlockAt(loc);

                blockTeleporter.remove(block.getLocation());
                blockTeleporter.put(block.getLocation(), (n-1));
                customConfig.set("BlocksData.Teleporter_" + n, null);
                AddEntry((n-1), block,performCommand);
            }
        }
        saveCustomYml(customConfig, customYml);
    }

    public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
