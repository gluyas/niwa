package swen222.niwa.gui.graphics;

import org.joml.*;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Tile;

import java.awt.*;
import java.lang.Math;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Draws the state of a Room onto a graphics object
 *
 * @author Marc
 */
public class RoomRenderer {

	private Room room;
	private int heightDiff;
	private Vector3d centreOffset;
	private EntityTable<?> et;

	private Direction facing = Direction.NORTH; // the world direction that is northeast from the user's perspective
	private double bearing = Math.toRadians(45);

	public RoomRenderer(Room subject, EntityTable<?> et) {
		setRoom(subject);
		setET(et);
	}

	public Direction getFacing() {
		return facing;
	}

	public void setET(EntityTable<?> et) {
		this.et = et;
	}

	public void setDebugCoordinates(int value) {
		debugCoordinates = value;
	}

	public int cycleDebugCoordinates() {
		debugCoordinates = (debugCoordinates +1)%3;
		return debugCoordinates;
	}

	public void rotateCW() {
		facing = facing.turnCW();
		bearing -= Math.toRadians(90);
	}

	public void rotateCCW() {
		facing = facing.turnCCW();
		bearing += Math.toRadians(90);
	}

	public void setRoom(Room r) {
		this.room = r;
		if (r == null) return;

		int minH = Integer.MAX_VALUE;
		int maxH = Integer.MIN_VALUE;
		for (Tile t : r) {
			if (t.height < minH) minH = t.height;
			if (t.height > maxH) maxH = t.height;
		}

		heightDiff = maxH - minH;

		centreOffset = new Vector3d(
				(r.width - 1) / 2.0,
				(r.height - 1) / 2.0,
				0							// TODO: get correct offset
		).negate();
	}

	/**
	 * Renders this Room in a rectangle on a given swing graphics object
	 * @param g
	 * @param width
	 * @param height
	 */
	public void draw(Graphics g, int width, int height) {

		if (room == null || et == null) return;

		double scale = getRoomScale(width, height);
		double blockSize = scale * Math.sqrt(2);

		g.translate(width/2, height/2);

		Matrix4d projection = new Matrix4d()					// orthographic projection matrix
				.rotate(ELEVATION_ANGLE, 1, 0, 0)
				.rotate(bearing, 0, 0, 1)
				.translate(centreOffset)						// centred around centroid of level
				.scale(1, 1, HEIGHT_TO_WIDTH);			// scale block height to be a fraction of width

		for (Location loc : new BackToFrontIterator(room, facing)) {
			Tile tile = room.tileAt(loc);

			Vector4d pos = new Vector4d(loc.col, loc.row, tile.height, 1);
			projection.transform(pos);

			pos.mul(pos.w * scale);								// convert from hg coordinates & apply block scale
			pos.w = 1;

			tile.drawSprite(g, facing, (int) pos.x, (int) pos.y, blockSize);
			if (tile.prop != null) tile.prop.drawSprite(g, facing, (int) pos.x, (int) pos.y, blockSize);

			if (et != null) {
				for (Entity e : et.get(loc)) {
					e.sprite(facing).draw(g, (int) pos.x, (int) pos.y, blockSize);
				}
			}

			// EDITOR MODE

			if (debugCoordinates != DEBUG_COORDS_DISABLED) {
				switch (debugCoordinates) {
					case DEBUG_COORDS_WHITE:
						g.setColor(Color.white);
						break;
					case DEBUG_COORDS_BLACK:
						g.setColor(Color.black);
						break;
				}
				FontMetrics m = g.getFontMetrics();
				String coords = loc.toStringCoords();
				g.drawString(
					coords,
					(int) pos.x - m.stringWidth(coords) / 2,
					(int) pos.y - m.getHeight() / 2 + m.getAscent()
				);
			}
		}
	}

	// GRAPHICS CODE

	public static final double JITTER = 0.14;
	public static final double HEIGHT_TO_WIDTH = 1 / 4.0;
	public static final double ELEVATION_ANGLE = Math.atan(Math.sqrt(2));

	public static final double X_Y = Math.sqrt(3)/2; // 3D X to 2D Y
	//public static final double X_Y = 0.5; // 3D X to 2D Y
	public static final double Y_Y = X_Y; // 3D Y to 2D Y
	public static final double Z_Y = 2*X_Y/3; //2X_Y/2

	private int debugCoordinates = 0;

	public final int DEBUG_COORDS_DISABLED = 0;
	public final int DEBUG_COORDS_WHITE = 2;
	public final int DEBUG_COORDS_BLACK = 1;

	public double getRoomScale(int width, int height) {
		return 75;	// TODO: scale correctly
	}

	/**
	 * Iterates over all Locations in a Room from perspective-north to perspective-south (background to foreground)
	 * to facilitate the painter's algorithm
	 */
	private static class BackToFrontIterator implements Iterator<Location>, Iterable<Location> {

		public final Room r;
		private final int rowStep;
		private final int colStep;

		private int col;
		private int row;

		public BackToFrontIterator(Room r, Direction facing) {
			this.r = r;

			// set the starting corner in the diagonal of the direction minus 45 degrees
			Direction left = facing.turnCCW();
			int startRowDir = facing.relativeY() + left.relativeY();
			int startColDir = facing.relativeX() + left.relativeX();

			// start outside bounds so that we iterate onto first one
			this.row = startRowDir < 0 ? 0 : r.height-1;
			this.col = startColDir < 0 ? -1 : r.width;

			// now make it so we move in the opposite directions to that
			this.colStep = -startColDir;
			this.rowStep = -startRowDir;
		}

		@Override
		public boolean hasNext() {
			int nextCol = col+colStep;
			int nextRow = row+rowStep;
			// if either of these is in range then there must be another value
			return (nextRow >= 0 && nextRow < r.height) || (nextCol >= 0 && nextCol < r.width);
		}

		@Override
		public Location next() {
			col += colStep;
			if (col < 0) {
				col = r.width-1;
				row += rowStep;
			} else if (col >= r.width) {
				col = 0;
				row += rowStep;
			}
			// cols is nested in rows' iteration, so no more rows means no more cols either
			if (row < 0 || row >= r.height) throw new NoSuchElementException();
			else return Location.at(r, col, row);
		}

		@Override
		public Iterator<Location> iterator() {
			return this;
		}
	}
}
