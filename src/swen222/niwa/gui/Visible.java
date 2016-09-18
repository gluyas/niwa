package swen222.niwa.gui;

import com.sun.istack.internal.Nullable;

/**
 * Simple interface which all rendered game elements should implement.
 * Provides methods used for rendering objects.
 *
 * @author Marc
 */
public interface Visible {

	/**
	 * Core of this interface. This method will be used every time the Object is rendered.
	 * If the sprite of an object changes, it should notify its Observers to ensure this is reflected in the GUI.
	 *
	 * @return an Sprite as a graphical representation of this Object, or null to skip rendering.
	 */
	@Nullable
	Sprite sprite();

}
