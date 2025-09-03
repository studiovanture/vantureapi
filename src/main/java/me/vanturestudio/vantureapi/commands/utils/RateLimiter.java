package me.vanturestudio.vantureapi.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RateLimiter {

	public static RateLimiter rateLimiter;

	private final JavaPlugin PLUGIN;

	private final Map<Command, Map<UUID, Deque<Long>>> commandExecutions = new HashMap<>();
	private final int maxExecutions;
	private final long timeFrameMillis;

	public RateLimiter(JavaPlugin plugin, int maxExecutions, long timeFrameSeconds) {
		this.PLUGIN = plugin;
		this.maxExecutions = maxExecutions;
		this.timeFrameMillis = timeFrameSeconds * 1000L;
		rateLimiter = this;
	}

	public static RateLimiter getRateLimiter() {
		return rateLimiter;
	}

	public boolean isRateLimited(@NotNull Player player, Command command) {
		long now = System.currentTimeMillis();
		UUID uuid = player.getUniqueId();

		// init maps
		commandExecutions.putIfAbsent(command, new HashMap<>());
		Map<UUID, Deque<Long>> playerMap = commandExecutions.get(command);
		playerMap.putIfAbsent(uuid, new ArrayDeque<>());

		Deque<Long> executions = playerMap.get(uuid);

		// remove expired entries
		while (!executions.isEmpty() && now - executions.peekFirst() > timeFrameMillis) {
			executions.pollFirst(); // remove oldest
		}

		// check limit
		return executions.size() >= maxExecutions;
	}

	public void recordExecution(@NotNull Player player, Command command) {
		long now = System.currentTimeMillis();
		UUID uuid = player.getUniqueId();

		commandExecutions.putIfAbsent(command, new HashMap<>());
		Map<UUID, Deque<Long>> playerMap = commandExecutions.get(command);
		playerMap.putIfAbsent(uuid, new ArrayDeque<>());

		playerMap.get(uuid).addLast(now);
	}
}
