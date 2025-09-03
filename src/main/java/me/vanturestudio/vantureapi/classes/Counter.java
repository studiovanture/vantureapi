package me.vanturestudio.vantureapi.classes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter<T> {

	private final Map<T, Integer> counts = new HashMap<>();

	public void add(T element) {
		add(element, 1);
	}

	public void add(T element, int amount) {
		counts.put(element, counts.getOrDefault(element, 0) + amount);
	}

	public void remove(T element) {
		remove(element, 1);
	}

	public void remove(T element, int amount) {
		int current = counts.getOrDefault(element, 0);
		int newCount = Math.max(0, current - amount);
		if (newCount == 0) {
			counts.remove(element);
		} else {
			counts.put(element, newCount);
		}
	}

	public int count(T element) {
		return counts.getOrDefault(element, 0);
	}

	public boolean contains(T element) {
		return count(element) > 0;
	}

	public Set<T> elements() {
		return counts.keySet();
	}

	public Map<T, Integer> asMap() {
		return Collections.unmodifiableMap(counts);
	}

	public void clear() {
		counts.clear();
	}

	public boolean isEmpty() {
		return counts.isEmpty();
	}

	public int totalCount() {
		return counts.values().stream().mapToInt(Integer::intValue).sum();
	}

	public Map.Entry<T, Integer> mostCommon() {
		return counts.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.orElse(null);
	}

	public Map.Entry<T, Integer> leastCommon() {
		return counts.entrySet()
				.stream()
				.min(Map.Entry.comparingByValue())
				.orElse(null);
	}
}
