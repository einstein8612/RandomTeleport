package net.pillagecraft.randomtp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public final class RandomTP extends JavaPlugin {

	private int id;

	private Logger logger;

	private LocationUtils tpm;
	private ConfigManager cm;

	private HashMap<UUID, Long> coolDowns = new HashMap<>();

	@Override
	public void onEnable() {
		this.postBanner();

		this.logger = getLogger();
		this.logger.log(Level.INFO, "Registered logger");

		this.cm = new ConfigManager(this);
		this.logger.log(Level.INFO, "Registered config manager");

		getCommand("randomteleport").setExecutor(new CommandRandomTP(cm, coolDowns));
		this.logger.log(Level.INFO, "Registered command");

		this.startScheduler();
		this.logger.log(Level.INFO, "Registered scheduler");

		this.logger.log(Level.INFO, "RandomTP has been enabled!");
	}

	@Override
	public void onDisable() {
		this.logger.log(Level.INFO, "Unregistered scheduler");
		getServer().getScheduler().cancelTask(this.id);

		this.logger.log(Level.INFO, "Disabled RandomTP!");
	}

	public LocationUtils getTeleportManager() {
		return tpm;
	}

	public ConfigManager getConfigManager() {
		return cm;
	}

	private void postBanner() {
		final ConsoleCommandSender sender = getServer().getConsoleSender();
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6  _____ _______ _____  "));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 |  __ \\__   __|  __ \\ "));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 | |__) | | |  | |__) |"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 |  _  /  | |  |  ___/ "));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 | | \\ \\  | |  | |     "));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 |_|  \\_\\ |_|  |_|     "));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fVersion: &6" + getDescription().getVersion()
				+ "&f | &fServer: &6" + getServer().getBukkitVersion()));
		sender.sendMessage(" ");
	}

	private void startScheduler() {
		ArrayList<UUID> toRemove = new ArrayList<UUID>();
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				if (coolDowns.isEmpty())
					return;
				coolDowns.keySet().forEach(key -> {
					coolDowns.compute(key, (uuid, cooldown) -> cooldown - 1);

					if (coolDowns.get(key) <= 0) {
						toRemove.add(key);
					}
				});
				toRemove.forEach(uuid -> {
					coolDowns.remove(uuid);
				});
			}
		}.runTaskTimerAsynchronously(this, 0, 20);
		this.id = task.getTaskId();
	}
}
