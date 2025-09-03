package me.vanturestudio.vantureapi.classes.arrays;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A custom list-like structure that stores elements with additional metadata, tags, and positional info.
 *
 * @param <T> The type of object stored in the Enumerator.
 */
public class Enumerator<T> implements Iterable<T>, Cloneable, ConfigurationSerializable {

	private final List<Element<T>> elements = new ArrayList<>();

	/**
	 * Loads an Enumerator from a ConfigurationSection.
	 * Note: Only string values are supported currently.
	 *
	 * @param section the section to read from
	 * @return a new Enumerator
	 */
	public static @NotNull Enumerator<String> deserialize(@NotNull ConfigurationSection section) {
		Enumerator<String> enumerator = new Enumerator<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection elementSection = section.getConfigurationSection(key);
			if (elementSection != null) {
				String value = elementSection.getString("value");
				List<String> tags = elementSection.getStringList("tags");
				Element<String> element = new Element<>(value);
				element.getTags().addAll(tags);
				enumerator.elements.add(element);
			}
		}
		enumerator.updatePositions();
		return enumerator;
	}

	/**
	 * Creates an Enumerator from a List.
	 *
	 * @param list the source list
	 * @return new Enumerator
	 */
	public static <T> @NotNull Enumerator<T> fromList(@NotNull List<T> list) {
		Enumerator<T> enumerator = new Enumerator<>();
		list.forEach(enumerator::add);
		return enumerator;
	}

	/**
	 * Creates an Enumerator from an array.
	 *
	 * @param array the source array
	 * @return new Enumerator
	 */
	public static <T> @NotNull Enumerator<T> fromArray(T[] array) {
		Enumerator<T> enumerator = new Enumerator<>();
		Arrays.stream(array).forEach(enumerator::add);
		return enumerator;
	}

	/**
	 * Adds a new value to the Enumerator.
	 *
	 * @param value the value to add
	 */
	public Enumerator<T> add(T value) {
		elements.add(new Element<>(value));
		updatePositions();
		return this;
	}

	/**
	 * Adds a collection of values to the Enumerator.
	 *
	 * @param values the values to add
	 */
	public Enumerator<T> addAll(@NotNull Collection<T> values) {
		values.forEach(this::add);
		return this;
	}

	/**
	 * Adds a collection of values to the Enumerator.
	 *
	 * @param values the values to add
	 */
	@SafeVarargs
	public final Enumerator<T> addAll(@NotNull T... values) {
		for (T value: values) this.add(value);
		return this;
	}

	/**
	 * Removes a value from the Enumerator.
	 *
	 * @param value the value to remove
	 * @return The Enumerator<T>
	 */
	public Enumerator<T> remove(T value) {
		elements.removeIf(e -> Objects.equals(e.getValue(), value));
		updatePositions();
		return this;
	}

	/**
	 * Removes a value from the Enumerator.
	 *
	 * @param value the value to remove
	 * @return The Enumerator<T>
	 */
	public Enumerator<T> remove(int index) {
		elements.removeIf(e -> Objects.equals(e.getIndex(), index));
		updatePositions();
		return this;
	}

	/**
	 * Clears the Enumerator.
	 *
	 * @return The Enumerator<T>
	 */
	public Enumerator<T> clear() {
		elements.clear();
		return this;
	}

	/**
	 * Gets the list of elements.
	 *
	 * @return unmodifiable list of elements
	 */
	public List<Element<T>> getElements() {
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Gets the raw values of all elements.
	 *
	 * @return list of values
	 */
	public List<T> values() {
		return elements.stream().map(Element::getValue).collect(Collectors.toList());
	}

	/**
	 * Filters the Enumerator based on a condition.
	 *
	 * @param predicate the condition
	 * @return a new filtered Enumerator
	 */
	public Enumerator<T> filter(Predicate<Element<T>> predicate) {
		Enumerator<T> filtered = new Enumerator<>();
		elements.stream().filter(predicate).forEach(e -> filtered.add(e.getValue()));
		return filtered;
	}

	/**
	 * Filters elements by a specific tag.
	 *
	 * @param tag the tag to match
	 * @return a new Enumerator with matching elements
	 */
	public Enumerator<T> filterByTag(String tag) {
		return filter(e -> e.hasTag(tag));
	}

	/**
	 * Checks if any element matches a condition.
	 *
	 * @param predicate the condition
	 * @return true if any match
	 */
	public boolean anyMatch(Predicate<Element<T>> predicate) {
		return elements.stream().anyMatch(predicate);
	}

	/**
	 * Checks if all elements match a condition.
	 *
	 * @param predicate the condition
	 * @return true if all match
	 */
	public boolean allMatch(Predicate<Element<T>> predicate) {
		return elements.stream().allMatch(predicate);
	}

	/**
	 * Returns the first element that matches a condition.
	 *
	 * @param predicate the condition
	 * @return optional matching element
	 */
	public Optional<Element<T>> returnIf(Predicate<Element<T>> predicate) {
		return elements.stream().filter(predicate).findFirst();
	}

	/**
	 * Sorts the Enumerator using a comparator.
	 *
	 * @param comparator the comparator
	 * @return The Enumerator<T>
	 */
	public Enumerator<T> sort(Comparator<T> comparator) {
		elements.sort(Comparator.comparing(Element::getValue, comparator));
		updatePositions();
		return this;
	}

	/**
	 * Shuffles the elements in the Enumerator.
	 *
	 * @return The Enumerator<T>
	 */
	public Enumerator<T> shuffle() {
		Collections.shuffle(elements);
		updatePositions();
		return this;
	}

	/**
	 * Gets the number of elements.
	 *
	 * @return the size
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Checks if the Enumerator is empty.
	 *
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/**
	 * Gets an element at a specific index.
	 *
	 * @param index the index
	 * @return the element
	 */
	public Element<T> get(int index) {
		return elements.get(index);
	}

	/**
	 * Maps the Enumerator to another Enumerator with a new type.
	 *
	 * @param mapper the transform function
	 * @return a new mapped Enumerator
	 */
	public <R> Enumerator<R> map(Function<T, R> mapper) {
		Enumerator<R> mapped = new Enumerator<>();
		for (Element<T> element : elements) {
			mapped.add(mapper.apply(element.getValue()));
		}
		return mapped;
	}

	public T[] array(Class<T> clazz) {
		List<T> list = new ArrayList<>();
		for (Element<T> element : getElements()) {
			list.add(element.getValue());
		}
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(clazz, list.size());
		return list.toArray(array);
	}

	public List<T> list() {
		List<T> list = new ArrayList<>();
		for (Element<T> element: getElements()) {
			list.add(element.getValue());
		}
		return list;
	}

	/**
	 * Converts the Enumerator to a simple JSON-style string.
	 *
	 * @return string representation
	 */
	public String toJson() {
		return elements.stream()
				.map(e -> "\"" + e.getValue().toString() + "\"")
				.collect(Collectors.joining(", ", "[", "]"));
	}

	public Map<T, T> toMap() throws ArrayIndexOutOfBoundsException {
		if (elements.size() % 2 != 0) throw new ArrayIndexOutOfBoundsException("List size has to be even.");
		Map<T, T> result = new HashMap<>();

		boolean even = true;
		T cache = null;
		for (Element<T> element : elements) {
			if (even)
				cache = element.getValue();
			else {
				result.put(cache, element.getValue());
				cache = null;
			}
			even = !even;
		}


		return result;
	}

	/**
	 * Clones the Enumerator and all its elements.
	 *
	 * @return a deep copy of the Enumerator
	 */
	@Override
	public Enumerator<T> clone() throws CloneNotSupportedException {
		Enumerator<T> cloned = new Enumerator<>();
		for (Element<T> element : this.elements) {
			Element<T> newElement = new Element<>(element.getValue());
			newElement.getTags().addAll(element.getTags());
			newElement.getMetadata().addAll(element.getMetadata());
			cloned.elements.add(newElement);
		}
		cloned.updatePositions();
		return cloned;
	}

	/**
	 * Converts the Enumerator to a string.
	 *
	 * @return string with all values
	 */
	@Override
	public String toString() {
		return "Enumerator" + toJson();
	}

	/**
	 * Serializes the Enumerator.
	 *
	 * @return The configuration section mapped out.
	 */
	@Override
	public @NotNull Map<String, Object> serialize() {
		Map<String, Object> section = new HashMap<>();
		int i = 0;
		for (Element<T> e : elements) {
			String key = "elements" + i++;
			section.put(key + "value", e.getValue().toString()); // assume toString is enough
			section.put(key + "tags", new ArrayList<>(e.getTags()));
		}
		return section;
	}

	private void updatePositions() {
		for (int i = 0; i < elements.size(); i++) {
			Element<T> e = elements.get(i);
			e.setIndex(i);
			e.setPosition(Position.from(i, elements.size()));
		}
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public @NotNull Iterator<T> iterator() {
		return list().iterator();
	}

	/**
	 * Performs the given action for each element of the {@code Iterable}
	 * until all elements have been processed or the action throws an
	 * exception.  Actions are performed in the order of iteration, if that
	 * order is specified.  Exceptions thrown by the action are relayed to the
	 * caller.
	 * <p>
	 * The behavior of this method is unspecified if the action performs
	 * side effects that modify the underlying source of elements, unless an
	 * overriding class has specified a concurrent modification policy.
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 * @implSpec <p>The default implementation behaves as if:
	 * <pre>{@code
	 *     for (T t : this)
	 *         action.accept(t);
	 * }</pre>
	 */
	@Override
	public void forEach(Consumer<? super T> action) {
		list().forEach(action);
	}

	/**
	 * Creates a {@link Spliterator} over the elements described by this
	 * {@code Iterable}.
	 *
	 * @return a {@code Spliterator} over the elements described by this
	 * {@code Iterable}.
	 * @implSpec The default implementation creates an
	 * <em><a href="../util/Spliterator.html#binding">early-binding</a></em>
	 * spliterator from the iterable's {@code Iterator}.  The spliterator
	 * inherits the <em>fail-fast</em> properties of the iterable's iterator.
	 * @implNote The default implementation should usually be overridden.  The
	 * spliterator returned by the default implementation has poor splitting
	 * capabilities, is unsized, and does not report any spliterator
	 * characteristics. Implementing classes can nearly always provide a
	 * better implementation.
	 * @since 1.8
	 */
	@Override
	public Spliterator<T> spliterator() {
		return list().spliterator();
	}


	/**
	 * Describes the position of an element in the Enumerator.
	 */
	public enum Position {
		ONLY,
		FIRST,
		MIDDLE,
		LAST;

		public static Position from(int index, int size) {
			if (size == 1) return ONLY;
			if (index == 0) return FIRST;
			if (index == size - 1) return LAST;
			return MIDDLE;
		}
	}

	/**
	 * Represents a value in the Enumerator, with index, position, tags, and metadata.
	 *
	 * @param <T> the element type
	 */
	public static class Element<T> {
		private final T value;
		private final Set<String> tags = new HashSet<>();
		private final List<Object> metadata = new ArrayList<>();
		private int index;
		private Position position;

		public Element(T value) {
			this.value = value;
		}

		public T getValue() {
			return value;
		}

		public int getIndex() {
			return index;
		}

		private void setIndex(int index) {
			this.index = index;
		}

		public Position getPosition() {
			return position;
		}

		private void setPosition(Position position) {
			this.position = position;
		}

		public Set<String> getTags() {
			return tags;
		}

		public boolean hasTag(String tag) {
			return tags.contains(tag);
		}

		public List<Object> getMetadata() {
			return metadata;
		}

		public Element<T> addTag(String tag) {
			tags.add(tag);
			return this;
		}

		public Element<T> removeTag(String tag) {
			tags.remove(tag);
			return this;
		}

		public Element<T> addMetadata(Object data) {
			metadata.add(data);
			return this;
		}

		public Element<T> removeMetadata(Object data) {
			metadata.remove(data);
			return this;
		}

		public <U> Optional<U> findMetadataOfType(@NotNull Class<U> type) {
			return metadata.stream()
					.filter(type::isInstance)
					.map(type::cast)
					.findFirst();
		}
	}

	public PaginatedEnumerator<T> paginate(int page, int itemsPerPage) {
		return new PaginatedEnumerator<>(this, page, itemsPerPage);
	}
}
