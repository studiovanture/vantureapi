package me.vanturestudio.vantureapi.output;

import org.bukkit.Color;

import java.util.function.Function;

public enum Level {
	OFF("OFF", s -> s),
	SEVERE("SEVERE", s -> String.format("&4SEVERE: %s", s)),
	WARNING("WARNING", s -> String.format("&cWARNING: %s", s)),
	INFO("INFO", s -> String.format("&eINFO: %s", s)),
	CONFIG("CONFIG", s -> String.format("&4CONFIG: %s", s)),
	FINE("FINE", s -> String.format("&fFINE: %s", s)),
	FINER("FINER", s -> String.format("%sFINER: %s", Color.SILVER, s)),
	FINEST("FINEST", s -> String.format("%sFINEST: %s", Color.GRAY, s)),
	ALL("ALL", s -> s);

	private final String name;
	private final Function<String, String> format;

	Level(String name, Function<String, String> format) {
		this.name = name;
		this.format = format;
	}

	public String getName() {
		return name;
	}

	public Function<String, String> getFormat() {
		return format;
	}
}
