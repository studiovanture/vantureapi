package me.vanturestudio.vantureapi.chat;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.stream.IntStream;

public class Message implements CharSequence {

	public static Message of(String content) {
		return new Message(java.util.UUID.randomUUID(), content, null);
	}

	@Override
	public @NotNull String toString() {
		return CONTENT;
	}

	private final UUID ID;
	private final String CONTENT;
	private final Player PLAYER;

	public Message(UUID uuid, String content, @Nullable Player sender) {
		this.ID = uuid;
		this.CONTENT = content;
		this.PLAYER = sender;
	}

	public UUID getID() {
		return this.ID;
	}

	public String getContent() {
		return this.CONTENT;
	}

	public Player getPlayer() {
		return this.PLAYER;
	}

	/**
	 * Returns the length of this character sequence.  The length is the number
	 * of 16-bit {@code char}s in the sequence.
	 *
	 * @return the number of {@code char}s in this sequence
	 */
	@Override
	public int length() {
		return this.CONTENT.length();
	}

	/**
	 * Returns the {@code char} value at the specified index.  An index ranges from zero
	 * to {@code length() - 1}.  The first {@code char} value of the sequence is at
	 * index zero, the next at index one, and so on, as for array
	 * indexing.
	 *
	 * <p>If the {@code char} value specified by the index is a
	 * {@linkplain Character##unicode surrogate}, the surrogate value
	 * is returned.
	 *
	 * @param index the index of the {@code char} value to be returned
	 * @return the specified {@code char} value
	 * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than
	 *                                   {@code length()}
	 */
	@Override
	public char charAt(int index) {
		return this.CONTENT.charAt(index);
	}

	/**
	 * Returns {@code true} if this character sequence is empty.
	 *
	 * @return {@code true} if {@link #length()} is {@code 0}, otherwise
	 * {@code false}
	 * @implSpec The default implementation returns the result of calling {@code length() == 0}.
	 * @since 15
	 */
	@Override
	public boolean isEmpty() {
		return this.CONTENT.isEmpty();
	}

	/**
	 * Returns a {@code CharSequence} that is a subsequence of this sequence.
	 * The subsequence starts with the {@code char} value at the specified index and
	 * ends with the {@code char} value at index {@code end - 1}.  The length
	 * (in {@code char}s) of the
	 * returned sequence is {@code end - start}, so if {@code start == end}
	 * then an empty sequence is returned.
	 *
	 * @param start the start index, inclusive
	 * @param end   the end index, exclusive
	 * @return the specified subsequence
	 * @throws IndexOutOfBoundsException if {@code start} or {@code end} are negative,
	 *                                   if {@code end} is greater than {@code length()},
	 *                                   or if {@code start} is greater than {@code end}
	 */
	@Override
	public @NotNull CharSequence subSequence(int start, int end) {
		return this.CONTENT.subSequence(start, end);
	}

	/**
	 * Returns a stream of {@code int} zero-extending the {@code char} values
	 * from this sequence.  Any char which maps to a
	 * {@linkplain Character##unicode surrogate code point} is passed
	 * through uninterpreted.
	 *
	 * <p>The stream binds to this sequence when the terminal stream operation
	 * commences (specifically, for mutable sequences the spliterator for the
	 * stream is <a href="../util/Spliterator.html#binding"><em>late-binding</em></a>).
	 * If the sequence is modified during that operation then the result is
	 * undefined.
	 *
	 * @return an IntStream of char values from this sequence
	 * @since 1.8
	 */
	@Override
	public @NotNull IntStream chars() {
		return this.CONTENT.chars();
	}

	/**
	 * Returns a stream of code point values from this sequence.  Any surrogate
	 * pairs encountered in the sequence are combined as if by {@linkplain
	 * Character#toCodePoint Character.toCodePoint} and the result is passed
	 * to the stream. Any other code units, including ordinary BMP characters,
	 * unpaired surrogates, and undefined code units, are zero-extended to
	 * {@code int} values which are then passed to the stream.
	 *
	 * <p>The stream binds to this sequence when the terminal stream operation
	 * commences (specifically, for mutable sequences the spliterator for the
	 * stream is <a href="../util/Spliterator.html#binding"><em>late-binding</em></a>).
	 * If the sequence is modified during that operation then the result is
	 * undefined.
	 *
	 * @return an IntStream of Unicode code points from this sequence
	 * @since 1.8
	 */
	@Override
	public @NotNull IntStream codePoints() {
		return this.CONTENT.codePoints();
	}
}
