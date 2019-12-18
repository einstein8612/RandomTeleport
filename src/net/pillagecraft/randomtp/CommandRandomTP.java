package net.pillagecraft.randomtp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandRandomTP implements CommandExecutor {

    private RandomTP rtp;

    public CommandRandomTP(RandomTP rtp) {
        this.rtp = rtp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }

        Player p = (Player) sender;

        if(!p.hasPermission("pillagecraft.rtp")) {
            p.sendMessage(rtp.getConfigManager().getMessage("noPerms"));
            return true;
        }

        p.teleport(rtp.getTeleportManager().getRandomLocation(p));

        String x = Integer.toString((int)p.getLocation().getX());
        String y = Integer.toString((int)p.getLocation().getY());
        String z = Integer.toString((int)p.getLocation().getZ());

        String msg = rtp.getConfigManager().getMessage("randomTP");

        String format = msg.replaceAll("\\{x\\}", x)
                .replaceAll("\\{y\\}", y)
                .replaceAll("\\{z\\}", z);

        p.sendMessage(format);

        return true;
    }
}
