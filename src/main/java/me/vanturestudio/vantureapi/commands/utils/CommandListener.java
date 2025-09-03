package me.vanturestudio.vantureapi.commands.utils;

import me.vanturestudio.vantureapi.classes.VOID;
import me.vanturestudio.vantureapi.classes.arrays.Dictionary;
import me.vanturestudio.vantureapi.classes.arrays.Pair;
import me.vanturestudio.vantureapi.output.OutputStream;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CommandListener {

	private final JavaPlugin PLUGIN;
	public CommandListener(JavaPlugin plugin) {
		this.PLUGIN = plugin;
	}

	public enum CommandPreprocessResult {
		PASSED,
		RATE_LIMITED,
		ON_COOLDOWN,
		VOIDED

	}

	@Contract("_, null -> new")
	public static @NotNull Pair<CommandPreprocessResult, Object> onCommand(Player player, Command command) {

		if (command == null) {
			return new Pair<>(CommandPreprocessResult.VOIDED, VOID.a);
		}

		boolean limited = RateLimiter.getRateLimiter().isRateLimited(player, command);

		RateLimiter.getRateLimiter().recordExecution(player, command);
		if (limited) {
			OutputStream.send(player, "&cYou're being rate limited.");
			return new Pair<>(CommandPreprocessResult.RATE_LIMITED, VOID.a);
		}

		// Check cooldowns
		if (CooldownManager.getCooldownManager().isOnCooldown(player, command)) {
			Dictionary<String, Object> dictionary = new Dictionary<>();
			double remaining = CooldownManager.getCooldownManager().getRemaining(player, command) / 1000.0;
			dictionary.createDictionary("remaining", remaining);
			OutputStream.send(player, "&7Wait &c" + String.format("%.1f", remaining) + "&7s.");
			return new Pair<>(CommandPreprocessResult.ON_COOLDOWN, new Dictionary<>().createDictionary("remaining", remaining));
		}

		// Passed everything
		CooldownManager.getCooldownManager().applyCooldown(player, command);
		return new Pair<>(CommandPreprocessResult.PASSED, VOID.a);
	}

}