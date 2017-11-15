package swen222.niwa.gui.graphics;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Animator implements Supplier<Boolean> {

	public final Instant start;
	public final Instant end;

	public final long duration;

	private Predicate<Float> lambda;

	/**
	 * Constructs an Animator using a lambda function
	 * @param duration maximum duration of the animation
	 * @param lambda function taking normalised time value over duration of the animation; return true for early termination
	 */
	public Animator(long duration, Predicate<Float> lambda) {
		this.start = Instant.now();
		this.end = start.plusMillis(duration);

		this.duration = duration;

		this.lambda = lambda;
	}

	/**
	 * Apply the animation at a given point of its duration
	 * @return 	true when the animation is complete
	 */
	public final boolean apply() {
		Instant now = Instant.now();
		if (now.isAfter(end)) {
			lambda.test(1f);
			return true;
		} else {
			return lambda.test((float) start.until(now, ChronoUnit.MILLIS) / duration);
		}
	}

	@Override
	public final Boolean get() {
		return apply();
	}

}