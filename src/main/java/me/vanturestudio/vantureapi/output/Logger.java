package me.vanturestudio.vantureapi.output;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

public class Logger {

	final JavaPlugin PLUGIN;

	public Logger(JavaPlugin plugin) {
		this.PLUGIN = plugin;
	}

	public String log(Level level, String str, Object... arguments) {
		// Add level prefix and color codes (you can change '&' to whatever if needed)
		String finalm = "[" + level.getName() + "] " + String.format(str, arguments);
		if (level == Level.SEVERE) {
			System.err.println(finalm);
		} else {
			System.out.println(finalm);
		}
		return finalm;
	}

	public String println(String line) {
		return line;
	}

}
