package me.vanturestudio.vantureapi.classes;

/**
 * Represents a numeric range between two values (inclusive).
 */
public record Range<T extends Comparable<T>>(
		T min,
		T max) {
	public Range {
		if (min.compareTo(max) > 0)
			throw new IllegalArgumentException("Min cannot be greater than max.");
	}


	public boolean contains(T value) {
		return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
	}

	public T clamp(T value) {
		if (value.compareTo(min) < 0)
			return min;
		if (value.compareTo(max) > 0)
			return max;
		return value;
	}

	@Override
	public String toString() {
		return "[" + min + " .. " + max + "]";
	}
}
