package me.vanturestudio.vantureapi.output;

import me.vanturestudio.vantureapi.utils.BitFlags;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputStream {

	// Enum for format flags
	public enum FormatFlag {
		ADD_BUKKIT_COLORS(1),      // Flag for Bukkit colors
		ADD_PLACEHOLDERS(1 << 1),       // Flag for placeholders
		ADD_MATHS(1 << 2),              // Flag for maths
		ADD_ALL_FORMATS(1 << 3),        // Flag for all formats
		REMOVE_ALL_FORMATS(1 << 4);     // Flag to remove all formats

		private final int flag;

		FormatFlag(int flag) {
			this.flag = flag;
		}

		public int getFlag() {
			return flag;
		}
	}

	// -------------------------- Format Methods --------------------------

	public static String format(String string) {
		return format(FormatFlag.ADD_ALL_FORMATS, string);
	}
	public static String format(FormatFlag formatFlags, String string) {
		return format(formatFlags.getFlag(), string);
	}
	public static String format(int formatFlags, String string) {
		if (string == null || string.isEmpty()) string = "&r";
		String formatted = string;
		EnumSet<FormatFlag> flags = BitFlags.fromInt(formatFlags);

		if (flags.contains(FormatFlag.ADD_ALL_FORMATS)) {
			formatted = formatBasicPlaceholders(formatted);
			formatted = formatGradient(formatted);  // Apply gradient
			formatted = formatHex(formatted);
			formatted = formatDiscord(formatted);
		}

		return formatted;
	}

	public static String formatGradient(String input) {

		Pattern pattern = Pattern.compile("<gradient:#([0-9a-fA-F]{6})-#([0-9a-fA-F]{6})>(.*?)</gradient>");
		Matcher matcher = pattern.matcher(input);

		StringBuffer buffer = new StringBuffer();
		Gradient gradient = new Gradient();

		while (matcher.find()) {
			Color from = hexToColor(matcher.group(1));
			Color to = hexToColor(matcher.group(2));
			String text = matcher.group(3);
			String gradientText = gradient.rgbGradient(text, from, to, new Gradient.LinearInterpolator()) + ChatColor.RESET;
			matcher.appendReplacement(buffer, gradientText);
		}
		matcher.appendTail(buffer);
		input = buffer.toString();

		return input;
	}
	@Contract("_ -> new")
	private static @NotNull Color hexToColor(@NotNull String hex) {
		if (hex.startsWith("#")) hex = hex.substring(1); // just in case
		if (hex.length() != 6) throw new IllegalArgumentException("Invalid hex color: " + hex);

		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);

		return new Color(r, g, b);
	}

	public static String formatBasicPlaceholders(String input) {
		if (input == null || input.isEmpty()) return input;
		LocalDateTime now = LocalDateTime.now();

		input = input.replace("%time_DD%", String.format("%02d", now.getDayOfMonth()))
				.replace("%time_MM%", String.format("%02d", now.getMonthValue()))
				.replace("%time_YYYY%", String.valueOf(now.getYear()))
				.replace("%time_YY%", String.valueOf(now.getYear()).substring(2))
				.replace("%time_HH%", String.format("%02d", now.getHour()))
				.replace("%time_mm%", String.format("%02d", now.getMinute()))
				.replace("%time_ss%", String.format("%02d", now.getSecond()))
				.replace("%time_SSS%", String.format("%03d", now.getNano() / 1_000_000));

		return input;
	}

	public static String formatHex(String input) {
		if (input == null || input.isEmpty()) return input;

		Pattern pattern = Pattern.compile("<#[a-fA-F0-9]{6}>");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			String hex = input.substring(matcher.start(), matcher.end());
			String replaceSharp = hex.replace('#', 'x').replace("<", "").replace(">", "");

			StringBuilder builder = new StringBuilder();
			for (char c : replaceSharp.toCharArray()) {
				builder.append("&").append(c);
			}
			input = input.replace(hex, builder.toString());
		}
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static String formatDiscord(String input) {
		if (input == null || input.isEmpty()) return input;
		// Discord-specific formatting
		input = formatDiscordStyle(input, "__", "&n");  // Underline
		input = formatDiscordStyle(input, "~~", "&m");  // Strikethrough
		input = formatDiscordStyle(input, "***", "&o&l"); // BoldItalic
		input = formatDiscordStyle(input, "**", "&l");   // Bold
		input = formatDiscordStyle(input, "*", "&o");    // Italic
		return input;
	}

	private static String formatDiscordStyle(String input, String symbol, String styleCode) {
		if (StringUtils.substringsBetween(input, symbol, symbol) != null) {
			for (String placeholder : StringUtils.substringsBetween(input, symbol, symbol)) {
				input = input.replace(symbol + placeholder + symbol, styleCode + placeholder + "&r");
			}
		}
		return input;
	}

	// -------------------------- Send Methods --------------------------

	/**
	 * Sends a formatted message to a player.
	 * @param player the player to send the message to
	 * @param formatFlags flags to apply while formatting
	 * @param messages messages to send
	 */
	public static void send(Player player, int formatFlags, String... messages) {
		if (player == null) {
			throw new NullPointerException("Player cannot be null.");
		}
		for (String message : messages) {
			player.sendMessage(format(formatFlags, message));
		}
	}

	@Contract("null, _, _ -> fail")
	public static void send(Player player, @NotNull FormatFlag formatFlags, String... messages) {
		send(player, formatFlags.getFlag(), messages);
	}

	/**
	 * Sends a formatted message to a player using default flags.
	 * @param player the player to send the message to
	 * @param messages messages to send
	 */
	public static void send(Player player, String... messages) {
		send(player, FormatFlag.ADD_ALL_FORMATS, messages);
	}

	/**
	 * Sends a message to the console.
	 * @param messages messages to log to the console
	 */
	public static void out(String... messages) {
		for (String message : messages) {
			System.out.println(format(FormatFlag.ADD_ALL_FORMATS, message));
		}
	}
}