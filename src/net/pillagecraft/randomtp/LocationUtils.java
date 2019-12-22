package net.pillagecraft.randomtp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;

public final class LocationUtils {

	private final static List<Material> safe = Arrays.asList(Material.AIR, Material.CAVE_AIR);

	public static Location getWorldBorderRandomLocation(final World world) {
		final WorldBorder wb = world.getWorldBorder();

		final double wbX = wb.getCenter().getX();
		final double wbZ = wb.getCenter().getZ();
		final double halfSize = 0.5 * wb.getSize();

		final double xMax = wbX + halfSize;
		final double xMin = wbX - halfSize;
		final double zMax = wbZ + halfSize;
		final double zMin = wbZ - halfSize;

		return getRandomLocation(world, xMax, xMin, zMax, zMin);

	}

	public static Location getConfigRandomLocation(final World world, final ConfigurationSection borderSection) {

		final double xMax = borderSection.getDouble("xMax", 0);
		final double xMin = borderSection.getDouble("xMin", 0);
		final double zMax = borderSection.getDouble("zMax", 0);
		final double zMin = borderSection.getDouble("zMin", 0);

		return getRandomLocation(world, xMax, xMin, zMax, zMin);
	}

	public static Location getRandomLocation(final World world, final double xMax, final double xMin, final double zMax,
			final double zMin) {

		Location loc;

		double x, y, z;

		boolean isBlacklisted = true;

		do {
			x = ((double) new Random().nextInt((int) xMax - (int) xMin) + xMin) + 0.5;
			z = ((double) new Random().nextInt((int) zMax - (int) zMin) + zMin) + 0.5;

			loc = new Location(world, x, 0, z);

			y = world.getHighestBlockAt(loc).getY();
			loc.setY(y);

			if (!loc.getBlock().getBiome().toString().contains("OCEAN")) {
				if (isSafe(loc)) {
					isBlacklisted = false;
				}
			}
		} while (isBlacklisted);
		return loc;
	}

	public static boolean isSafe(final Location location) {
		final Block middle = location.getBlock();
		final Block feet = middle.getRelative(BlockFace.DOWN);
		final Block head = middle.getRelative(BlockFace.UP);

		if (!safe.contains(middle.getType())) {
			return false; // Middle block isn't air
		}

		if (!safe.contains(head.getType())) {
			return false; // Head block isn't air
		}

		if (feet.isLiquid()) {
			return false; // Block player stands on is air and therefore will make him/her drop
		}

		return true;
	}

}
