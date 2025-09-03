package me.vanturestudio.vantureapi.classes.arrays;

/**
 * Represents a tuple of three values.
 */
public record Triplet<A, B, C>(
		A first,
		B second,
		C third) {


	@Override
	public String toString() {
		return "(" + first + ", " + second + ", " + third + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Triplet))
			return false;
		Triplet<?, ?, ?> t = (Triplet<?, ?, ?>) o;
		return first.equals(t.first) && second.equals(t.second) && third.equals(t.third);
	}

	@Override
	public int hashCode() {
		return first.hashCode() ^ second.hashCode() ^ third.hashCode();
	}
}
