package me.vanturestudio.vantureapi.classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

/**
 * An extended version of Bukkit's Location with additional utility methods.
 */
public class ExtendedLocation extends Location {

	public enum FacingDirection {
		NORTH, NORTH_EAST, EAST, SOUTH_EAST,
		SOUTH, SOUTH_WEST, WEST, NORTH_WEST, UNKNOWN
	}

	public enum VerticalDirection {
		UP, DOWN, LEVEL
	}

	public enum CoordinateAxis {
		X, Y, Z
	}

	public ExtendedLocation(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public ExtendedLocation(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public ExtendedLocation(Location location) {
		super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public ExtendedLocation cloneLocation() {
		return new ExtendedLocation(this);
	}

	public ExtendedLocation offset(CoordinateAxis axis, double value) {
		switch (axis) {
			case X -> setX(getX() + value);
			case Y -> setY(getY() + value);
			case Z -> setZ(getZ() + value);
		}
		return this;
	}

	public boolean isSameBlock(Location other) {
		return getBlockX() == other.getBlockX()
				&& getBlockY() == other.getBlockY()
				&& getBlockZ() == other.getBlockZ()
				&& isSameWorld(other);
	}

	public boolean isAir() {
		return getBlock().getType() == Material.AIR;
	}

	public boolean isSafe() {
		Material type = getBlock().getType();
		return type != Material.LAVA && type != Material.FIRE && type != Material.CACTUS;
	}

	public VerticalDirection getVerticalDirectionTo(Location other) {
		if (other.getY() > getY()) return VerticalDirection.UP;
		if (other.getY() < getY()) return VerticalDirection.DOWN;
		return VerticalDirection.LEVEL;
	}

	public ExtendedLocation withYaw(float yaw) {
		this.setYaw(yaw);
		return this;
	}

	public ExtendedLocation withPitch(float pitch) {
		this.setPitch(pitch);
		return this;
	}

	public ExtendedLocation subtract(double x, double y, double z) {
		this.setX(getX() - x);
		this.setY(getY() - y);
		this.setZ(getZ() - z);
		return this;
	}

	public ExtendedLocation midpoint(Location other) {
		return new ExtendedLocation(
				getWorld(),
				(getX() + other.getX()) / 2,
				(getY() + other.getY()) / 2,
				(getZ() + other.getZ()) / 2,
				(getYaw() + other.getYaw()) / 2,
				(getPitch() + other.getPitch()) / 2
		);
	}

	public double horizontalDistance(Location other) {
		double dx = getX() - other.getX();
		double dz = getZ() - other.getZ();
		return Math.sqrt(dx * dx + dz * dz);
	}

	public Block getRelativeBlock(int dx, int dy, int dz) {
		return getWorld().getBlockAt(getBlockX() + dx, getBlockY() + dy, getBlockZ() + dz);
	}

	public boolean isChunkLoaded() {
		return getWorld().isChunkLoaded(getBlockX() >> 4, getBlockZ() >> 4);
	}

	public boolean isAboveGround() {
		return getY() > getWorld().getHighestBlockYAt(getBlockX(), getBlockZ());
	}

	public ExtendedLocation copy() {
		return new ExtendedLocation(this);
	}

	public Block getBlock() {
		return getWorld().getBlockAt(this);
	}

	public Block getBlockBelow() {
		return getWorld().getBlockAt(getBlockX(), getBlockY() - 1, getBlockZ());
	}

	public boolean isWithin(Location other, double radius) {
		return this.distance(other) <= radius;
	}

	public ExtendedLocation addOffset(double dx, double dy, double dz) {
		this.add(dx, dy, dz);
		return this;
	}

	public ExtendedLocation withDirection(float yaw, float pitch) {
		this.setYaw(yaw);
		this.setPitch(pitch);
		return this;
	}

	public ExtendedLocation center() {
		this.setX(getBlockX() + 0.5);
		this.setZ(getBlockZ() + 0.5);
		return this;
	}

	public void teleport(Entity entity) {
		entity.teleport(this);
	}

	public void teleportAll(Collection<? extends Player> players) {
		for (Player player : players) {
			player.teleport(this);
		}
	}

	public ExtendedLocation floored() {
		return new ExtendedLocation(getWorld(), getBlockX(), getBlockY(), getBlockZ());
	}

	public Vector toVector() {
		return new Vector(getX(), getY(), getZ());
	}

	public ExtendedLocation apply(Consumer<ExtendedLocation> modifier) {
		modifier.accept(this);
		return this;
	}

	public String toSimpleString() {
		return String.format("%s [x=%.2f, y=%.2f, z=%.2f, yaw=%.1f, pitch=%.1f]",
				getWorld().getName(), getX(), getY(), getZ(), getYaw(), getPitch());
	}

	public String getChunkCoords() {
		return String.format("Chunk [%d, %d]", getBlockX() >> 4, getBlockZ() >> 4);
	}

	public boolean isSameWorld(Location other) {
		return other != null && Objects.equals(getWorld(), other.getWorld());
	}

	public double distanceSquaredSafe(Location other) {
		return isSameWorld(other) ? this.distanceSquared(other) : Double.MAX_VALUE;
	}

	public ExtendedLocation offset(Vector direction) {
		return new ExtendedLocation(getWorld(), getX() + direction.getX(), getY() + direction.getY(), getZ() + direction.getZ());
	}

	public ExtendedLocation inFacingDirection() {
		Vector direction = getDirection().normalize();
		return offset(direction);
	}

	public FacingDirection getFacingDirection() {
		float yaw = (getYaw() - 90) % 360;
		if (yaw < 0) yaw += 360;

		if (yaw >= 337.5 || yaw < 22.5) return FacingDirection.NORTH;
		if (yaw < 67.5) return FacingDirection.NORTH_EAST;
		if (yaw < 112.5) return FacingDirection.EAST;
		if (yaw < 157.5) return FacingDirection.SOUTH_EAST;
		if (yaw < 202.5) return FacingDirection.SOUTH;
		if (yaw < 247.5) return FacingDirection.SOUTH_WEST;
		if (yaw < 292.5) return FacingDirection.WEST;
		if (yaw < 337.5) return FacingDirection.NORTH_WEST;

		return FacingDirection.UNKNOWN;
	}

	public static Optional<ExtendedLocation> fromString(String input) {
		try {
			String[] parts = input.split(";");
			World world = Bukkit.getWorld(parts[0]);
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
			float yaw = parts.length > 4 ? Float.parseFloat(parts[4]) : 0f;
			float pitch = parts.length > 5 ? Float.parseFloat(parts[5]) : 0f;
			return Optional.of(new ExtendedLocation(world, x, y, z, yaw, pitch));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("world", getWorld().getName());
		map.put("x", getX());
		map.put("y", getY());
		map.put("z", getZ());
		map.put("yaw", getYaw());
		map.put("pitch", getPitch());
		return map;
	}
}

