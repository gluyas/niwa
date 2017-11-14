package swen222.niwa.gui.graphics;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

public abstract class Animator implements Supplier<Boolean> {

	public final Instant start;
	public final Instant end;

	public final long duration;

	public Animator(long duration) {
		this.start = Instant.now();
		this.end = start.plusMillis(duration);

		this.duration = duration;
	}

	public final boolean apply() {
		Instant now = Instant.now();
		return now.isAfter(end) || apply(start.until(now, ChronoUnit.MILLIS) / duration);
	}

	@Override
	public final Boolean get() {
		return apply();
	}

	/**
	 * Apply the animation at a given point of its duration
 	 * @param t normalised time value over duration of the animation
	 * @return 	true to force an early termination of the animation
	 */
	protected abstract boolean apply(float t);

}
