package me.vanturestudio.vantureapi.commands;

import me.vanturestudio.vantureapi.commands.annotation.Cooldown;
import me.vanturestudio.vantureapi.commands.utils.CooldownManager;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class CommandHandler {

	private final JavaPlugin PLUGIN;
	public CommandHandler(JavaPlugin plugin) {
		this.PLUGIN = plugin;
	}

	public void register(String label, ImplCommand executor, @Nullable ImplTabCompleter completer) {
		if (label == null || label.isBlank()) throw new NullPointerException("CommandHandler.register(String command, CommandExecutor executor, @Nullable TabCompleter completer) -> 'command' cannot be null");
		if (executor == null) throw new NullPointerException("CommandHandler.register(String command, CommandExecutor executor, @Nullable TabCompleter completer) -> 'executor' cannot be null");

		Command command = PLUGIN.getCommand(label);
		if (command == null)
			throw new IllegalArgumentException("Command not found in plugin.yml: " + label);

		Cooldown cooldown = executor.getClass().getAnnotation(Cooldown.class);
		if (cooldown != null) {
			CooldownManager.getCooldownManager().registerCooldown(command, cooldown.seconds());
		}

		PLUGIN.getCommand(label).setExecutor(executor);
		if (completer != null)
			PLUGIN.getCommand(label).setTabCompleter(completer);

	}
}
