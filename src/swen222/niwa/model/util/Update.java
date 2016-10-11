package swen222.niwa.model.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Class for sending Object updates from server to client, with a method to materialise the change.
 * @author Marc
 */
public interface Update extends Serializable {

	/**
	 * Apply this update to the local instance of the game. This should propagate an exact copy
	 * of this update to its Observers
	 */
	public void apply();

	static <T> Update applyValue(T value, Consumer<T> action) {
		return (Update) () -> action.accept(value);
	}

}
