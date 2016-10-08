package swen222.niwa.model.entity;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Marc
 */
public interface EntityUpdate extends Serializable {

	public void apply();

}
