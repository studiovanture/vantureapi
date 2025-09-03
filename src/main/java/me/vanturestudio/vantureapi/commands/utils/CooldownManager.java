package me.vanturestudio.vantureapi.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
	private static final CooldownManager manager = new CooldownManager();

	private final Map<Command, Map<Player, Long>> cooldowns = new HashMap<>();
	private final Map<Command, Long> durations = new HashMap<>();

	public static CooldownManager getCooldownManager() {
		return manager;
	}

	public void registerCooldown(Command command, int seconds) {
		durations.put(command, seconds * 1000L);
	}

	public boolean isOnCooldown(Player player, Command command) {
		if (!cooldowns.containsKey(command)) return false;
		Long last = cooldowns.get(command).get(player);
		if (last == null) return false;
		long elapsed = System.currentTimeMillis() - last;
		return elapsed < durations.getOrDefault(command, 0L);
	}

	public double getRemaining(Player player, Command command) {
		if (!cooldowns.containsKey(command)) return 0;
		Long last = cooldowns.get(command).get(player);
		long duration = durations.getOrDefault(command, 0L);
		return Math.max(0, (duration - (System.currentTimeMillis() - last)));
	}

	public void applyCooldown(Player player, Command command) {
		cooldowns.computeIfAbsent(command, k -> new HashMap<>()).put(player, System.currentTimeMillis());
	}
}