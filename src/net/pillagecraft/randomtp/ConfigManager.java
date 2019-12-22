package net.pillagecraft.randomtp;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigManager {

    private RandomTP rtp;
    private File dataFolder;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(RandomTP rtp) {
        this.rtp = rtp;
        this.dataFolder = rtp.getDataFolder();
        this.configFile = new File(dataFolder, "config.yml");

        if(dataFolder.exists()) {
            loadConfigs();
        } else {
            setup();
        }
    }

    public String getMessage(String entry) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(entry));
    }
    
    public FileConfiguration getConfig() {
    	return this.config;
    }

    private void loadConfigs() {
        this.configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists())
            rtp.saveResource("config.yml", true);
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void setup() {
        dataFolder.mkdirs();
        loadConfigs();
    }

}
