package me.vanturestudio.vantureapi.language;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class LanguageManager {

	private final File langFolder;
	private final FileConfiguration config;
	private final Map<String, Language> registeredLanguages = new HashMap<>();
	private final Map<String, FileConfiguration> languageFiles = new HashMap<>();
	private final JavaPlugin plugin;
	private final String defaultLanguage;

	public LanguageManager(@NotNull JavaPlugin plugin) {
		this.plugin = plugin;
		this.langFolder = new File(plugin.getDataFolder(), "lang");
		if (!langFolder.exists()) langFolder.mkdirs();

		File configFile = new File(langFolder, "languages.yml");
		if (!configFile.exists()) plugin.saveResource("lang/languages.yml", false);

		this.config = YamlConfiguration.loadConfiguration(configFile);
		this.defaultLanguage = config.getString("default-language", "en-us");
	}

	public void registerConfig() {

	}

	public void registerLanguage(@NotNull Language language, LanguageOther... others) {
		String code = language.getCode();
		registeredLanguages.put(code, language);

		File langFile = new File(langFolder, code + ".yml");
		if (!langFile.exists()) {
			try {
				langFile.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "Couldn't create language file: " + langFile.getName());
				return;
			}
		}

		YamlConfiguration yml = YamlConfiguration.loadConfiguration(langFile);
		boolean updated = false;

		// Fill in missing keys from defaults
		for (Map.Entry<String, String> entry : language.getDefaults().entrySet()) {
			if (!yml.contains(entry.getKey())) {
				yml.set(entry.getKey(), entry.getValue());
				updated = true;
			}
		}

		if (updated) {
			try {
				yml.save(langFile);
			} catch (
					IOException e) {
				plugin.getLogger().warning("Failed to save language file: " + langFile.getName());
			}
		}

		languageFiles.put(code, yml);
	}

	public String getMessage(String key, @Nullable String langCode) {
		if (langCode == null) langCode =  this.getDefaultLanguage();
		FileConfiguration yml = this.languageFiles.get(langCode);
		if (yml != null) {
			String msg = yml.getString(key);
			if (msg != null && !msg.isEmpty()) return msg;
		}

		// Fallback
		FileConfiguration fallback = this.languageFiles.get(defaultLanguage);
		if (fallback != null) {
			String msg = fallback.getString(key);
			if (msg != null && !msg.isEmpty()) return msg;
		}

		return "[Missing message: " + key + "]";
	}

	public Set<String> getAvailableLanguages() {
		return registeredLanguages.keySet();
	}

	public Language getLanguage(String key) {
		return registeredLanguages.get(key);
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}
}
