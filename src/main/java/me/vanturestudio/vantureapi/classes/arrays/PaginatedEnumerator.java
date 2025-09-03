package me.vanturestudio.vantureapi.classes.arrays;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PaginatedEnumerator<T> {

	private final Enumerator<T> source;
	private final int page;
	private final int itemsPerPage;
	private final List<Enumerator.Element<T>> currentPageElements;
	private final int totalPages;

	public PaginatedEnumerator(@NotNull Enumerator<T> source, int page, int itemsPerPage) {
		this.source = source;
		this.page = Math.max(1, page);
		this.itemsPerPage = Math.max(1, itemsPerPage);

		int total = source.size();
		this.totalPages = (int) Math.ceil((double) total / this.itemsPerPage);

		int fromIndex = (this.page - 1) * this.itemsPerPage;
		int toIndex = Math.min(fromIndex + this.itemsPerPage, total);

		this.currentPageElements = fromIndex >= total ? Collections.emptyList() :
				source.getElements().subList(fromIndex, toIndex);
	}

	public List<Enumerator.Element<T>> getElements() {
		return Collections.unmodifiableList(currentPageElements);
	}

	public int getPage() {
		return page;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean hasNextPage() {
		return page < totalPages;
	}

	public boolean hasPreviousPage() {
		return page > 1;
	}

	public Enumerator<T> toEnumerator() {
		Enumerator<T> enumerator = new Enumerator<>();
		currentPageElements.forEach(e -> enumerator.add(e.getValue()));
		return enumerator;
	}

	public List<T> toList() {
		return currentPageElements.stream().map(Enumerator.Element::getValue).toList();
	}

	@SuppressWarnings("unchecked")
	public T[] toArray(Class<T> clazz) {
		T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, currentPageElements.size());
		for (int i = 0; i < currentPageElements.size(); i++) {
			array[i] = currentPageElements.get(i).getValue();
		}
		return array;
	}

	public boolean isEmpty() {
		return currentPageElements.isEmpty();
	}

	public int size() {
		return currentPageElements.size();
	}

	// Allows action on each element (full page context)
	public PaginatedEnumerator<T> forEach(Consumer<Enumerator.Element<T>> action) {
		currentPageElements.forEach(action);
		return this;
	}

	// Filters elements within this page (non-destructive)
	public PaginatedEnumerator<T> filter(Predicate<Enumerator.Element<T>> predicate) {
		Enumerator<T> temp = new Enumerator<>();
		currentPageElements.stream().filter(predicate).forEach(e -> temp.add(e.getValue()));
		return new PaginatedEnumerator<>(temp, 1, temp.size()); // just one filtered page
	}

	// Returns true if any element in the page matches the condition
	public boolean anyMatch(Predicate<Enumerator.Element<T>> predicate) {
		return currentPageElements.stream().anyMatch(predicate);
	}

	// Finds the first match in this page
	public Optional<Enumerator.Element<T>> findFirst(Predicate<Enumerator.Element<T>> predicate) {
		return currentPageElements.stream().filter(predicate).findFirst();
	}

	// Returns a Stream of page elements
	public Stream<Enumerator.Element<T>> stream() {
		return currentPageElements.stream();
	}

	// Maps current page to a list of another type
	public <R> List<R> map(Function<Enumerator.Element<T>, R> mapper) {
		return currentPageElements.stream().map(mapper).toList();
	}

	// Gets raw values of current page
	public List<T> values() {
		return currentPageElements.stream().map(Enumerator.Element::getValue).toList();
	}

	// Checks if page contains a specific value
	public boolean containsValue(T value) {
		return currentPageElements.stream().anyMatch(e -> Objects.equals(e.getValue(), value));
	}

	// Gets element at relative page index
	public Optional<Enumerator.Element<T>> get(int index) {
		if (index < 0 || index >= currentPageElements.size()) return Optional.empty();
		return Optional.of(currentPageElements.get(index));
	}

	// Searches the elements by a given condition (predicate) and returns a new paginated result
	public PaginatedEnumerator<T> search(Predicate<Enumerator.Element<T>> condition, int page, int itemsPerPage) {
		Enumerator<T> filteredEnumerator = new Enumerator<>();
		currentPageElements.stream()
				.filter(condition)
				.forEach(e -> filteredEnumerator.add(e.getValue()));

		return new PaginatedEnumerator<>(filteredEnumerator, page, itemsPerPage);
	}

	@Override
	public String toString() {
		return "PaginatedEnumerator{page=" + page + ", totalPages=" + totalPages + ", size=" + size() + "}";
	}
}