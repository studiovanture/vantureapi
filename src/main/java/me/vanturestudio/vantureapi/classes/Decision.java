package me.vanturestudio.vantureapi.classes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * A powerful, flexible decision engine for selecting a result of type T
 * based on predicates, priorities, scoring functions, and default fallbacks.
 * <p>
 * Usage example:
 * <p>
 * Decision<String> decision = Decision.of(String.class)
 *     .when(user::isAdmin, () -> "Admin")
 *     .withPriority(2, user::isPremium, () -> "Premium")
 *     .score(user::getKarma, () -> "Popular")
 *     .defaultResult("Guest")
 *     .timeLimitMillis(50)
 *     .decide();
 *
 * @param <T> the type of result this decision engine returns
 */

public class Decision<T> {

	private static class DecisionCase<T> {
		Predicate<VOID> predicate;
		Supplier<T> supplier;
		int priority;
		double score;
		boolean isScored;

		DecisionCase(Predicate<VOID> predicate, Supplier<T> supplier, int priority) {
			this.predicate = predicate;
			this.supplier = supplier;
			this.priority = priority;
			this.score = 0;
			this.isScored = false;
		}

		DecisionCase(@NotNull Supplier<Double> scoringFunc, Supplier<T> supplier) {
			this.predicate = v -> true;
			this.supplier = supplier;
			this.priority = 0;
			this.score = scoringFunc.get();
			this.isScored = true;
		}
	}

	private final List<DecisionCase<T>> cases = new ArrayList<>();
	private T defaultResult = null;
	private long timeLimitMillis = -1;

	private Decision() {}

	/**
	 * Creates a new Decision engine instance.
	 *
	 * @param clazz the class of the return type
	 * @param <T>   type of the decision result
	 * @return a new Decision instance
	 */
	@Contract("_ -> new")
	public static <T> @NotNull Decision<T> of(Class<T> clazz) {
		return new Decision<>();
	}

	/**
	 * Adds a condition-based decision case.
	 *
	 * @param condition the predicate condition to check
	 * @param resultSupplier the supplier to run if condition is true
	 * @return this Decision instance
	 */
	public Decision<T> when(BooleanSupplier condition, Supplier<T> resultSupplier) {
		cases.add(new DecisionCase<>(v -> condition.getAsBoolean(), resultSupplier, 0));
		return this;
	}

	/**
	 * Adds a priority-based condition. Higher priority wins over lower.
	 *
	 * @param priority the priority level (higher = stronger)
	 * @param condition the predicate to test
	 * @param resultSupplier the result if it passes
	 * @return this Decision instance
	 */
	public Decision<T> withPriority(int priority, BooleanSupplier condition, Supplier<T> resultSupplier) {
		cases.add(new DecisionCase<>(v -> condition.getAsBoolean(), resultSupplier, priority));
		return this;
	}

	/**
	 * Adds a scoring-based decision case. The one with the highest score wins.
	 *
	 * @param scoringFunction the function to determine score
	 * @param resultSupplier the result if chosen
	 * @return this Decision instance
	 */
	public Decision<T> score(Supplier<Double> scoringFunction, Supplier<T> resultSupplier) {
		cases.add(new DecisionCase<>(scoringFunction, resultSupplier));
		return this;
	}

	/**
	 * Sets the fallback result if no conditions pass.
	 *
	 * @param fallback the fallback value
	 * @return this Decision instance
	 */
	public Decision<T> defaultResult(T fallback) {
		this.defaultResult = fallback;
		return this;
	}

	/**
	 * Sets a max time limit in milliseconds for decision evaluation.
	 * Useful to prevent expensive or infinite logic.
	 *
	 * @param millis time limit in milliseconds
	 * @return this Decision instance
	 */
	public Decision<T> timeLimitMillis(long millis) {
		this.timeLimitMillis = millis;
		return this;
	}

	/**
	 * Evaluates all cases and returns the most suitable result.
	 *
	 * @return the decided result
	 */
	public T decide() {
		long startTime = System.currentTimeMillis();

		// Separate scored and unscored cases
		List<DecisionCase<T>> scored = new ArrayList<>();
		List<DecisionCase<T>> matched = new ArrayList<>();

		for (DecisionCase<T> c : cases) {
			if (timeLimitMillis > 0 && System.currentTimeMillis() - startTime > timeLimitMillis) break;

			if (c.isScored) {
				scored.add(c);
			} else if (c.predicate.test(null)) {
				matched.add(c);
			}
		}

		// Highest priority wins among matched
		matched.sort((a, b) -> Integer.compare(b.priority, a.priority));
		if (!matched.isEmpty()) return matched.get(0).supplier.get();

		// Otherwise, highest score wins
		scored.sort((a, b) -> Double.compare(b.score, a.score));
		if (!scored.isEmpty()) return scored.get(0).supplier.get();

		return defaultResult;
	}

	/**
	 * Clears all added cases and resets the decision engine.
	 */
	public void reset() {
		cases.clear();
		defaultResult = null;
		timeLimitMillis = -1;
	}

	/**
	 * Returns the number of decision cases added.
	 *
	 * @return the number of cases
	 */
	public int caseCount() {
		return cases.size();
	}

	/**
	 * Returns true if the decision has a default result set.
	 *
	 * @return true if fallback exists
	 */
	public boolean hasDefault() {
		return defaultResult != null;
	}
}
