package com.br.fullPvp.utils;

import org.bukkit.*;

import lombok.Getter;

public class SerializeLocation {
	
	@Getter
	static SerializeLocation instance = new SerializeLocation();
	
	public String serializeLocation(Location loc, boolean isPlayer) {
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String location = null;
		if (isPlayer) {
			float yaw = loc.getYaw();
			float pitch = loc.getPitch();
			location = String.valueOf(world) + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
		} else {
			location = String.valueOf(world) + ";" + x + ";" + y + ";" + z;
		}
		return location;
	}

	public Location deserializeLocation(String name, boolean isPlayer) {
		String[] split = name.split(";");
		World world = Bukkit.getWorld(split[0]);
		double x = Double.valueOf(split[1]);
		double y = Double.valueOf(split[2]);
		double z = Double.valueOf(split[3]);
		Location location = null;
		if (isPlayer) {
			float yaw = Float.valueOf(split[4]);
			float pitch = Float.valueOf(split[5]);
			location = new Location(world, x, y, z, yaw, pitch);
		} else {
			location = new Location(world, x, y, z);
		}
		return location;
	}
}
