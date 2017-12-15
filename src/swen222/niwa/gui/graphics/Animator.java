package swen222.niwa.gui.graphics;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Animator implements Supplier<Boolean> {

	public final Instant start;
	public final Instant end;

	public final long duration;

	private final Predicate<Double> lambda;
	private final long[] triggers;
	private int triggerIndex = 0;

	/**
	 * Constructs an Animator using a lambda function.
	 * Triggers can optionally be used to introduce one-time event invocations on the lambda.
	 * @param duration 	Maximum duration of the animation
	 * @param lambda 	Function taking normalised time value over duration of the animation;
	 *                  Return true to trigger an early termination.
	 *                  A negative argument value indicates a trigger invocation; see below.
	 * @param triggers	An array containing normalised time stamps. the first invocation of apply() after
	 *                  after that time will call the lambda an additional time before the main invocation,
	 *                  with the argument -(trigger index + 1). Each index will be called at most once.
	 */
	public Animator(long duration, Predicate<Double> lambda, double ... triggers) {
		this.start = Instant.now();
		this.end = start.plusMillis(duration);

		this.duration = duration;

		this.lambda = lambda;

		this.triggers = new long[triggers.length];
		for (int i = 0; i < triggers.length; i++) {
			this.triggers[i] = (long) (duration * triggers[i]);
		}
	}

	/**
	 * Constructs an Animator using a lambda function.
	 * Triggers can optionally be used to introduce one-time event invocations on the lambda.
	 * @param duration 	Maximum duration of the animation
	 * @param lambda 	Function taking normalised time value over duration of the animation;
	 *                  Return true to trigger an early termination.
	 *                  A negative argument value indicates a trigger invocation; see below.
	 * @param triggers	An array containing millisecond time stamps. the first invocation of apply() after
	 *                  after that time will call the lambda an additional time before the main invocation,
	 *                  with the argument -(trigger index + 1). Each index will be called at most once.
	 */
	public Animator(long duration, Predicate<Double> lambda, long ... triggers) {
		this.start = Instant.now();
		this.end = start.plusMillis(duration);

		this.duration = duration;

		this.lambda = lambda;

		this.triggers = triggers;
	}

	/**
	 * Apply the animation at a given point of its duration
	 * @return 	true when the animation is complete
	 */
	public final boolean apply() {
		long elapsed = start.until(Instant.now(), ChronoUnit.MILLIS);

		if (triggerIndex < triggers.length && elapsed >= triggers[triggerIndex]) {
			if (lambda.test(-(triggerIndex + 1.0))) {
				return true;
			} else {
				triggerIndex++;
			}
		}

		if (elapsed >= duration) {
			lambda.test(1.0);
			return true;
		} else {
			return lambda.test((double) elapsed / duration);
		}
	}

	@Override
	public final Boolean get() {
		return apply();
	}

}