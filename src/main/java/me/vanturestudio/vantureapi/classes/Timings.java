package me.vanturestudio.vantureapi.classes;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timings {

	private final JavaPlugin PLUGIN;
	public Timings(JavaPlugin plugin) {
		this.PLUGIN = plugin;
	}

	private final Map<UUID, BukkitTask> tasks = new HashMap<>();


	/**
	 * Runs a task after a delay (in ticks).
	 */
	public UUID runLater(Runnable runnable, long delayTicks) {
		UUID id = UUID.randomUUID();
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
				tasks.remove(id);
			}
		}.runTaskLater(PLUGIN, delayTicks);
		tasks.put(id, task);
		return id;
	}

	/**
	 * Runs a task repeatedly with a delay and period (in ticks).
	 */
	public UUID runRepeating(BukkitRunnable runnable, long delayTicks, long periodTicks) {
		UUID id = UUID.randomUUID();
		BukkitTask task = runnable.runTaskTimer(PLUGIN, delayTicks, periodTicks);
		tasks.put(id, task);
		return id;
	}

	/**
	 * Runs a task instantly on the main thread.
	 */
	public UUID run(Runnable runnable) {
		return runLater(runnable, 0L);
	}

	/**
	 * Cancels a task by ID.
	 */
	public void cancel(UUID id) {
		BukkitTask task = tasks.remove(id);
		if (task != null) {
			task.cancel();
		}
	}

	/**
	 * Cancels all tracked tasks.
	 */
	public void cancelAll() {
		tasks.values().forEach(BukkitTask::cancel);
		tasks.clear();
	}

	/**
	 * Checks if a task is still running.
	 */
	public boolean isRunning(UUID id) {
		return tasks.containsKey(id);
	}
}
