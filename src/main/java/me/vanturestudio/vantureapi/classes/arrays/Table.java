package me.vanturestudio.vantureapi.classes.arrays;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Advanced table for 2D layout systems. Great for grid-based UIs like Minecraft inventories.
 */
public class Table<V> {
	private final int rows;
	private final int cols;
	private final Map<Integer, Map<Integer, V>> data = new HashMap<>();

	public Table(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}

	public void place(int row, int col, V value) {
		data.computeIfAbsent(row, k -> new HashMap<>()).put(col, value);
	}

	public void placeIfEmpty(int row, int col, V value) {
		if (!contains(row, col)) place(row, col, value);
	}

	public void replace(int row, int col, V value) {
		if (contains(row, col)) place(row, col, value);
	}

	public void remove(int row, int col) {
		Map<Integer, V> rowMap = data.get(row);
		if (rowMap != null) {
			rowMap.remove(col);
			if (rowMap.isEmpty()) data.remove(row);
		}
	}

	public V get(int row, int col) {
		Map<Integer, V> rowMap = data.get(row);
		return rowMap != null ? rowMap.get(col) : null;
	}

	public boolean contains(int row, int col) {
		return data.containsKey(row) && data.get(row).containsKey(col);
	}

	public void clear() {
		data.clear();
	}

	public int size() {
		return data.values().stream().mapToInt(Map::size).sum();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public List<V> getAll() {
		List<V> values = new ArrayList<>();
		data.values().forEach(map -> values.addAll(map.values()));
		return values;
	}

	public List<V> getRow(int row) {
		return data.containsKey(row) ? new ArrayList<>(data.get(row).values()) : Collections.emptyList();
	}

	public List<V> getColumn(int col) {
		List<V> column = new ArrayList<>();
		for (int row = 0; row < rows; row++) {
			V val = get(row, col);
			if (val != null) column.add(val);
		}
		return column;
	}

	public void autoPlace(List<V> items, BiPredicate<Integer, Integer> placementRule) {
		int index = 0;
		outer:
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (placementRule.test(row, col)) {
					if (index >= items.size()) break outer;
					place(row, col, items.get(index++));
				}
			}
		}
	}

	public void fill(V value, BiPredicate<Integer, Integer> where) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (where.test(row, col)) {
					place(row, col, value);
				}
			}
		}
	}

	public void swap(int row1, int col1, int row2, int col2) {
		V temp = get(row1, col1);
		place(row1, col1, get(row2, col2));
		place(row2, col2, temp);
	}

	public int getSlotIndex(int row, int col) {
		return row * cols + col;
	}

	public int[] getRowColFromIndex(int slot) {
		return new int[]{slot / cols, slot % cols};
	}

	public boolean isEdge(int col) {
		return col == 0 || col == cols - 1;
	}

	public boolean isCorner(int row, int col) {
		return (row == 0 || row == rows - 1) && isEdge(col);
	}

	public int getItemsPerRow() {
		return cols;
	}

	public int getItemsPerColumn() {
		return rows;
	}

	public Map<Integer, Map<Integer, V>> asMap() {
		return data;
	}

	public void forEach(BiConsumer<Integer, Integer> action) {
		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++)
				if (contains(row, col)) action.accept(row, col);
	}

	public Optional<int[]> getNextAvailable(BiPredicate<Integer, Integer> condition) {
		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++)
				if (!contains(row, col) && condition.test(row, col))
					return Optional.of(new int[]{row, col});
		return Optional.empty();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder("Table:\n");
		for (int row = 0; row < rows; row++) {
			out.append("Row ").append(row).append(": ");
			for (int col = 0; col < cols; col++) {
				V value = get(row, col);
				out.append(value != null ? value.toString() : "·").append(" ");
			}
			out.append("\n");
		}
		return out.toString();
	}
}
