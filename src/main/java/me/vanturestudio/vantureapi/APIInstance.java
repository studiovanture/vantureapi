package me.vanturestudio.vantureapi;

import me.vanturestudio.vantureapi.classes.Timings;
import me.vanturestudio.vantureapi.commands.CommandHandler;
import me.vanturestudio.vantureapi.commands.utils.CommandListener;
import me.vanturestudio.vantureapi.commands.utils.RateLimiter;
import me.vanturestudio.vantureapi.language.LanguageManager;
import me.vanturestudio.vantureapi.output.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class APIInstance {

	final JavaPlugin PLUGIN;

	final Logger LOGGER;

	public APIInstance(JavaPlugin plugin) {
		this.PLUGIN = plugin;
		this.LOGGER = new Logger(plugin);
	}

	public CommandHandler newCommandHandler() {
		return new CommandHandler(this.PLUGIN);
	}

	public CommandListener newCommandListener() {
		return new CommandListener(this.PLUGIN);
	}

	public LanguageManager newLanguageManager() {
		return new LanguageManager(this.PLUGIN);
	}

	public RateLimiter newRateLimiter(int maxExecutions, long timeFrameSeconds) {
		return new RateLimiter(this.PLUGIN, maxExecutions, timeFrameSeconds);
	}

	public Timings newTimings() {
		return new Timings(this.PLUGIN);
	}

	public Logger getLogger() {
		return this.LOGGER;
	}
}
