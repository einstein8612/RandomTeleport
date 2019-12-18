package net.pillagecraft.randomtp;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.Random;

public class TeleportManager {

    private RandomTP rtp;

    public TeleportManager(RandomTP rtp) {
        this.rtp = rtp;
    }

    public Location getRandomLocation(Player p) {

        Location loc;

        WorldBorder wb = p.getWorld().getWorldBorder();

        double x,y,z;

        double lowX = wb.getCenter().getX() - (0.5*wb.getSize());
        double highX = wb.getCenter().getX() + (0.5*wb.getSize());

        double lowZ = wb.getCenter().getZ() - (0.5*wb.getSize());
        double highZ = wb.getCenter().getZ() + (0.5*wb.getSize());

        x = (double) new Random().nextInt((int)highX-(int)lowX) + lowX;
        z = (double) new Random().nextInt((int)highZ-(int)lowZ) + lowZ;

        y = p.getWorld().getHighestBlockYAt((int)x, (int)z);

        loc = new Location(p.getWorld(), x, y, z);


        return loc;
    }

}
