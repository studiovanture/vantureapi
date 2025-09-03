package me.vanturestudio.vantureapi.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.function.Supplier;

public class SecureDataStore<T extends SecureStorable> {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final String ALGORITHM = "AES";

	private final JavaPlugin plugin;
	private final File file;
	private final SecretKey secretKey;
	private final Class<T> type;

	private T cache; // in-memory copy
	private String cacheHash; // last hash to detect tampering

	public SecureDataStore(JavaPlugin plugin, File file, Class<T> type) throws Exception {
		this.plugin = plugin;
		this.file = file;
		this.type = type;
		this.secretKey = generateKey(plugin);

		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) parent.mkdirs();
	}

	private SecretKey generateKey(JavaPlugin plugin) throws Exception {
		String serverName = Bukkit.getServer().getName();
		String version = plugin.getDescription().getVersion();
		String runtimePassword = serverName + "_2B1oi65VYe_" + version;

		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		byte[] key = sha.digest(runtimePassword.getBytes(StandardCharsets.UTF_8));
		return new SecretKeySpec(key, ALGORITHM);
	}

	public void save() throws Exception {
		if (cache == null) return;

		cache.setModifiedAt(Instant.now().toEpochMilli());
		String json = gson.toJson(cache);

		byte[] encrypted = encrypt(json);
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(encrypted);
		}

		cacheHash = hash(json); // update tamper hash
	}

	public void save(T object) throws Exception {
		this.cache = object;
		save();
	}

	public T load() {
		try {
			if (!file.exists()) return null;

			byte[] encrypted = Files.readAllBytes(file.toPath());
			String json = decrypt(encrypted);

			T obj = gson.fromJson(json, type);
			cache = obj;
			cacheHash = hash(json);
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public T getData(Supplier<T> defaultInstance) {
		if (cache != null) return cache;

		T loaded = load();
		if (loaded == null) {
			try {
				loaded = defaultInstance.get();
				save(loaded);
			} catch (Exception e) {
				throw new RuntimeException("Failed to create default data", e);
			}
		}
		return loaded;
	}

	public T getData() {
		return getData(() -> null);
	}

	/** Check if file tampered (current JSON hash doesn’t match cache) */
	public boolean isTampered() {
		if (!file.exists() || cache == null) return false;

		try {
			byte[] encrypted = Files.readAllBytes(file.toPath());
			String json = decrypt(encrypted);
			return !hash(json).equals(cacheHash);
		} catch (Exception e) {
			return true; // assume tampered if error
		}
	}

	private byte[] encrypt(String data) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
	}

	private String decrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
	}

	private String hash(String json) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = digest.digest(json.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hashBytes);
	}

	public static <T extends SecureStorable> SecureDataStore<T> createSecureDataStore(
			JavaPlugin plugin,
			String filename,
			Class<T> model,
			Supplier<T> defaultInstance
	) {
		try {
			File file = new File(plugin.getDataFolder(), filename);
			SecureDataStore<T> store = new SecureDataStore<>(plugin, file, model);

			T data = store.load();
			if (data == null) {
				data = defaultInstance.get();
				store.save(data);
			}
			return store;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
