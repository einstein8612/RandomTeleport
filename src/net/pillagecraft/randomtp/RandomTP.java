package net.pillagecraft.randomtp;

import org.bukkit.plugin.java.JavaPlugin;

public class RandomTP extends JavaPlugin {

    private RandomTP rtp;
    private TeleportManager tpm;
    private ConfigManager cm;

    @Override
    public void onEnable() {

        this.rtp = this;
        this.tpm = new TeleportManager(rtp);
        this.cm = new ConfigManager(rtp);

        getCommand("randomteleport").setExecutor(new CommandRandomTP(rtp));

    }

    public TeleportManager getTeleportManager() {
        return tpm;
    }

    public ConfigManager getConfigManager() {
        return cm;
    }
}
