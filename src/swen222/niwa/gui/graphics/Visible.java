package swen222.niwa.gui.graphics;

import com.sun.istack.internal.Nullable;
import swen222.niwa.model.world.Direction;

import java.awt.*;

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
	 * @param camera the Direction that this Visible is being viewed from
	 * @return an Sprite as a graphical representation of this Object, or null to skip rendering.
	 */
	@Nullable
	Sprite sprite(Direction camera);

	default void drawSprite(Graphics g, Direction facing, int x, int y, double blockSize) {
		Sprite s = this.sprite(facing);
		if (s == null) return;
		s.draw(g, x, y, blockSize);
	}

}
