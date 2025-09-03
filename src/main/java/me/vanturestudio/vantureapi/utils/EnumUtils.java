package me.vanturestudio.vantureapi.utils;

import java.util.*;

public class EnumUtils {

	/**
	 * Gets the next enum constant in the cycle.
	 * Wraps around to the first if it's at the end.
	 */
	public static <T extends Enum<T>> T next(T current) {
		T[] values = current.getDeclaringClass().getEnumConstants();
		int index = (current.ordinal() + 1) % values.length;
		return values[index];
	}

	/**
	 * Gets the previous enum constant in the cycle.
	 * Wraps around to the last if it's at the start.
	 */
	public static <T extends Enum<T>> T previous(T current) {
		T[] values = current.getDeclaringClass().getEnumConstants();
		int index = (current.ordinal() - 1 + values.length) % values.length;
		return values[index];
	}

	/**
	 * Returns a random value from an enum.
	 */
	public static <T extends Enum<T>> T random(Class<T> enumClass) {
		T[] values = enumClass.getEnumConstants();
		return values[new Random().nextInt(values.length)];
	}

	/**
	 * Gets an enum value by name, case-insensitive.
	 */
	public static <T extends Enum<T>> Optional<T> fromName(Class<T> enumClass, String name) {
		for (T constant : enumClass.getEnumConstants()) {
			if (constant.name().equalsIgnoreCase(name)) {
				return Optional.of(constant);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns a list of all enum constants.
	 */
	public static <T extends Enum<T>> List<T> list(Class<T> enumClass) {
		return Arrays.asList(enumClass.getEnumConstants());
	}

	/**
	 * Returns a list of all enum names.
	 */
	public static <T extends Enum<T>> List<String> names(Class<T> enumClass) {
		List<String> names = new ArrayList<>();
		for (T value : enumClass.getEnumConstants()) {
			names.add(value.name());
		}
		return names;
	}
}