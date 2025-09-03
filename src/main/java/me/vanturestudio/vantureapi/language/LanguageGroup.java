package me.vanturestudio.vantureapi.language;

import java.util.Map;
import java.util.Set;

public interface LanguageGroup extends LanguageOther {

	String groupKey();

	/**
	 * @return A map of the default messages, "key": "message"
	 * */
	Map<String, String> getDefaults(); // built-in default messages
}
