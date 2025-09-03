package me.vanturestudio.vantureapi.classes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.*;

/**
 * Conditional DSL + static helpers for elegant logic.
 */
public class Unary {

	// ----- DSL Core: When -----

	@Contract(value = "_ -> new", pure = true)
	public static <T> @NotNull When<T> when(boolean condition) {
		return new When<>(condition);
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull RunWhen runWhen(boolean condition) {
		return new RunWhen(condition);
	}

	public static class When<T> {
		private boolean matched = false;
		private Supplier<T> result;

		public When(boolean condition) {
			this.matched = condition;
		}

		public When<T> then(Supplier<T> value) {
			if (!matched) return this;
			this.result = value;
			return this;
		}

		public When<T> elseIf(boolean condition) {
			if (!matched && condition) matched = true;
			return this;
		}

		public When<T> not(boolean condition) {
			return elseIf(!condition);
		}

		public T orElse(Supplier<T> fallback) {
			return matched && result != null ? result.get() : fallback.get();
		}

		public T orElse(T fallback) {
			return matched && result != null ? result.get() : fallback;
		}

		public T orElseThrow(Supplier<RuntimeException> exceptionSupplier) {
			if (matched && result != null) return result.get();
			throw exceptionSupplier.get();
		}

		public Optional<T> orElseEmpty() {
			return matched && result != null ? Optional.ofNullable(result.get()) : Optional.empty();
		}
	}

	public static class RunWhen {
		private boolean matched = false;
		private Runnable action;

		public RunWhen(boolean condition) {
			this.matched = condition;
		}

		public RunWhen then(Runnable action) {
			if (matched && this.action == null) this.action = action;
			return this;
		}

		public RunWhen elseIf(boolean condition) {
			if (!matched && condition) matched = true;
			return this;
		}

		public RunWhen not(boolean condition) {
			return elseIf(!condition);
		}

		public void orElse(Runnable fallback) {
			if (matched && action != null) action.run();
			else fallback.run();
		}
	}

	// ----- Fluent API (Classic style) -----

	@Contract(value = "_ -> new", pure = true)
	public static <T> @NotNull If<T> ifTrue(boolean condition) {
		return new If<>(condition);
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull IfVoid ifTrueRun(boolean condition) {
		return new IfVoid(condition);
	}

	public static class If<T> {
		private final boolean condition;

		public If(boolean condition) {
			this.condition = condition;
		}

		public T then(Supplier<T> ifTrue, Supplier<T> ifFalse) {
			return condition ? ifTrue.get() : ifFalse.get();
		}

		public T thenOrNull(Supplier<T> ifTrue) {
			return condition ? ifTrue.get() : null;
		}

		public Optional<T> thenOptional(Supplier<T> ifTrue) {
			return condition ? Optional.ofNullable(ifTrue.get()) : Optional.empty();
		}

		public IfElse<T> then(Supplier<T> ifTrue) {
			return new IfElse<>(condition, ifTrue);
		}
	}

	public static class IfElse<T> {
		private final boolean condition;
		private final Supplier<T> ifTrue;

		public IfElse(boolean condition, Supplier<T> ifTrue) {
			this.condition = condition;
			this.ifTrue = ifTrue;
		}

		public T orElse(Supplier<T> ifFalse) {
			return condition ? ifTrue.get() : ifFalse.get();
		}

		public T orElse(T fallback) {
			return condition ? ifTrue.get() : fallback;
		}

		public Optional<T> orElseEmpty() {
			return condition ? Optional.ofNullable(ifTrue.get()) : Optional.empty();
		}
	}

	public static class IfVoid {
		private final boolean condition;

		public IfVoid(boolean condition) {
			this.condition = condition;
		}

		public void then(Runnable ifTrue) {
			if (condition) ifTrue.run();
		}

		public void then(Runnable ifTrue, Runnable ifFalse) {
			if (condition) ifTrue.run();
			else ifFalse.run();
		}

		public void then(@NotNull Consumer<Boolean> action) {
			action.accept(condition);
		}
	}

	// ----- Classic Methods -----

	public static <T> T chooseUnary(boolean condition, T ifTrue, T ifFalse) {
		return condition ? ifTrue : ifFalse;
	}

	public static String stringUnary(boolean condition, String ifTrue, String ifFalse) {
		return condition ? ifTrue : ifFalse;
	}

	public static int numberUnary(boolean condition, int ifTrue, int ifFalse) {
		return condition ? ifTrue : ifFalse;
	}

	public static void runUnary(boolean condition, Runnable ifTrue, Runnable ifFalse) {
		if (condition) ifTrue.run(); else ifFalse.run();
	}

	public static void runUnary(boolean condition, Runnable ifTrue) {
		if (condition) ifTrue.run();
	}

	public static <T> Optional<T> optionalUnary(boolean condition, Supplier<T> value) {
		return condition ? Optional.ofNullable(value.get()) : Optional.empty();
	}

	public static void logUnary(boolean condition, Supplier<String> message) {
		if (condition) System.out.println(message.get());
	}
}
