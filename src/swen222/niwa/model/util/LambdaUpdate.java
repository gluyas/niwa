package swen222.niwa.model.util;

import java.util.function.Consumer;

/**
 * Created by Marc on 9/10/2016.
 */
public class LambdaUpdate<T> implements Update {

	public final T value;
	public final Consumer<T> action;

	public LambdaUpdate(T value, Consumer<T> action) {
		this.value = value;
		this.action = action;
	}

	@Override
	public void apply() {
		action.accept(value);
	}
}
