package net.pillagecraft.randomtp;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandRandomTP implements CommandExecutor {

	private ConfigManager cm;
	private HashMap<UUID, Long> coolDowns;

	public CommandRandomTP(ConfigManager cm, HashMap<UUID, Long> coolDowns) {
		this.cm = cm;
		this.coolDowns = coolDowns;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("You must be a player to use this command!");
			return true;
		}

		final Player player = (Player) sender;

		if (!player.hasPermission("randomtp.rtp")) {
			player.sendMessage(cm.getMessage("noPerms"));
			return true;
		}

		if (coolDowns.containsKey(player.getUniqueId()) && !player.hasPermission("randomtp.bypasscooldown")) {
			final long second = coolDowns.get(player.getUniqueId()) % 60;
			final long minute = coolDowns.get(player.getUniqueId()) / 60 % 60;

			final String entry = cm.getMessage("timeLeft");
			final String format = entry.replaceAll("\\{MINUTES\\}", Long.toString(minute)).replaceAll("\\{SECONDS\\}",
					Long.toString(second));

			player.sendMessage(format);
			return true;
		}

		this.coolDowns.put(player.getUniqueId(), cm.getConfig().getLong("cooldown"));

		player.sendMessage(cm.getMessage("choosingLoc"));

		final Location newLocation = cm.getMessage("Border.type").equalsIgnoreCase("CONFIG")
				? LocationUtils.getConfigRandomLocation(player.getWorld(),
						cm.getConfig().getConfigurationSection("Border"))
				: LocationUtils.getWorldBorderRandomLocation(player.getWorld());

		player.teleport(newLocation);

		final String x = Integer.toString((int) newLocation.getX());
		final String y = Integer.toString((int) newLocation.getY());
		final String z = Integer.toString((int) newLocation.getZ());

		final String msg = cm.getMessage("randomTP");
		final String format = msg.replaceAll("\\{x\\}", x).replaceAll("\\{y\\}", y).replaceAll("\\{z\\}", z);
		player.sendMessage(format);

		return true;
	}
}
