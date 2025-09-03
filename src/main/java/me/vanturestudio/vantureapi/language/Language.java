package me.vanturestudio.vantureapi.language;

import java.util.Map;

public interface Language {

	/**
	 * @return the language code e.g. "en-us", "fr"
	 * */
	String getCode(); // e.g. "en"

	/**
	 * @return the display name e.g. "English (US)", "French"
	 * */
	String getDisplayName(); // e.g. "English"

	/**
	 * @return A map of the default messages, "key": "message"
	 * */
	Map<String, String> getDefaults(); // built-in default messages
}