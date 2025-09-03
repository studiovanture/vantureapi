package me.vanturestudio.vantureapi.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

public class StringUtils {
	
	/**
	 * Centers the given text within a string of the specified length.
	 * If the text is longer than the specified length, it is returned unchanged.
	 * Pads with spaces on both sides to make the text appear centered.
	 *
	 * @param str    The text to center.
	 * @param length The total length of the resulting string, including padding.
	 * @return A string with the text centered and padded with spaces.
	 */
	public static String center(String str, int length) {
		if (str == null) return null;
		str = str.trim();
		int padding = Math.max(0, length - str.length());
		int padStart = padding / 2;
		int padEnd = padding - padStart;
		return " ".repeat(padStart) + str + " ".repeat(padEnd);
	}
	
	/**
	 * Repeats the given string a specified number of times.
	 * If the repetition count is zero or negative, returns an empty string.
	 *
	 * @param str   The string to repeat.
	 * @param times The number of times to repeat the string.
	 * @return The resulting repeated string.
	 */
	public static @NotNull String repeat(@NotNull String str, int times) {
		return str.repeat(Math.max(0, times));
	}
	
	/**
	 * Pads the input string on the left with spaces until it reaches the desired length.
	 * If the input string is already longer than or equal to the specified length, it is returned unchanged.
	 *
	 * @param str    The string to pad.
	 * @param length The total length of the resulting string.
	 * @return The padded string with spaces on the left.
	 */
	public static @NotNull String padLeft(@NotNull String str, int length) {
		return " ".repeat(Math.max(0, length - str.length())) + str;
	}
	
	/**
	 * Pads the input string on the right with spaces until it reaches the desired length.
	 * If the input string is already longer than or equal to the specified length, it is returned unchanged.
	 *
	 * @param str    The string to pad.
	 * @param length The total length of the resulting string.
	 * @return The padded string with spaces on the right.
	 */
	public static @NotNull String padRight(@NotNull String str, int length) {
		return str + " ".repeat(Math.max(0, length - str.length()));
	}
	
	/**
	 * Truncates the string to a maximum length. If truncated, appends "..." to indicate missing content.
	 * If the string is shorter than or equal to the maximum length, it is returned unchanged.
	 *
	 * @param str       The string to truncate.
	 * @param maxLength The maximum allowed length of the string.
	 * @return The truncated string, possibly with ellipsis.
	 */
	public static @NotNull String truncate(@NotNull String str, int maxLength) {
		if (str.length() <= maxLength) return str;
		return str.substring(0, Math.max(0, maxLength - 3)) + "...";
	}
	
	/**
	 * Capitalizes the first character of the string.
	 * If the string is empty or null, it is returned as-is.
	 *
	 * @param str The string to capitalize.
	 * @return The string with the first character capitalized.
	 */
	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) return str;
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	/**
	 * Checks if the given string contains the specified substring, ignoring case.
	 *
	 * @param source The string to search within.
	 * @param sub    The substring to search for.
	 * @return True if the substring is found, ignoring case; otherwise false.
	 */
	public static boolean containsIgnoreCase(String source, String sub) {
		if (source == null || sub == null) return false;
		return source.toLowerCase().contains(sub.toLowerCase());
	}
	
	/**
	 * Reverses the characters in the given string.
	 *
	 * @param str The string to reverse.
	 * @return The reversed string.
	 */
	public static @NotNull String reverse(String str) {
		return new StringBuilder(str).reverse().toString();
	}
	
	/**
	 * Counts the number of times a substring appears in the given string.
	 *
	 * @param str The string to search in.
	 * @param sub The substring to count.
	 * @return The number of occurrences of the substring.
	 */
	public static int countOccurrences(String str, String sub) {
		if (str == null || sub == null || sub.isEmpty()) return 0;
		int count = 0, fromIndex = 0;
		while ((fromIndex = str.indexOf(sub, fromIndex)) != -1) {
			count++;
			fromIndex += sub.length();
		}
		return count;
	}
	
	/**
	 * Checks whether the given string contains only numeric characters (optional minus sign or decimal point included).
	 *
	 * @param str The string to check.
	 * @return True if the string is a valid number, false otherwise.
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) return false;
		return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	/**
	 * Removes all non-alphabetic characters from the string.
	 *
	 * @param str The input string.
	 * @return A new string containing only alphabetic characters (A-Z, a-z).
	 */
	@Contract(pure = true)
	public static @NotNull String removeNonAlpha(@NotNull String str) {
		return str.replaceAll("[^A-Za-z]", "");
	}
	
	/**
	 * Generates a random alphanumeric string of the specified length.
	 *
	 * @param length The length of the generated string.
	 * @return A random string made of A-Z, a-z, and 0-9 characters.
	 */
	public static @NotNull String randomString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
	
	/**
	 * Converts a string to a URL-friendly slug by lowercasing it,
	 * replacing spaces with dashes, and removing non-alphanumeric characters.
	 *
	 * @param str The string to convert.
	 * @return A slugified version of the string.
	 */
	public static @NotNull String toSlug(@NotNull String str) {
		return str.toLowerCase()
				.replaceAll("[^a-z0-9\\s]", "")
				.trim()
				.replaceAll("\\s+", "-");
	}
	
	/**
	 * Abbreviates a string in the middle if it exceeds the given max length,
	 * inserting "..." to represent the cut-off middle section.
	 * e.g. abbreviate("HelloWorld", 7) -> "Hel...ld"
	 *
	 * @param str       The input string.
	 * @param maxLength The max allowed length of the result string.
	 * @return The abbreviated string, or original if within limit.
	 */
	public static @NotNull String abbreviate(@NotNull String str, int maxLength) {
		if (str.length() <= maxLength || maxLength < 5) return str;
		int partLength = (maxLength - 3) / 2;
		return str.substring(0, partLength) + "..." + str.substring(str.length() - partLength);
	}
	
	
}
