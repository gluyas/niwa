package swen222.niwa.gui;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Tile;

import java.awt.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Draws the state of a Room onto a graphics object
 *
 * @author Marc
 */
public class RoomRenderer {

	public static final double X_Y = Math.sqrt(3)/2; // 3D X to 2D Y
	//public static final double X_Y = 0.5; // 3D X to 2D Y
	public static final double Y_Y = X_Y; // 3D Y to 2D Y
	public static final double Z_Y = 2*X_Y/3; //2X_Y/2

	public final Room r;

	int width;
	int height;

	private Direction facing = Direction.NORTH; // the world direction that is northeast from the user's perspective

	public RoomRenderer(Room subject) {
		this.r = subject;
	}

	public Direction getFacing() {
		return facing;
	}

	public void rotateCW() {
		facing = facing.turnCW();
	}

	public void rotateCCW() {
		facing = facing.turnCCW();
	}

	/**
	 * Renders this Room in a rectangle on a given //todo: cbf this rn
	 * @param g
	 * @param width
	 * @param height
	 */
	public void draw(Graphics g, int width, int height) {
		g.translate(width/2, height/2);
		double blockSize = getBlockSize(width, height);
		double scalar = blockSize/2.85;

		EntityTable<? extends Entity> et = r.getEntityTable();

		for (Location loc : new BackToFrontIterator(r, facing)) {
			Tile t = r.tileAt(loc);
			if (t == null) {
				System.err.println("null at "+loc.toString());
				continue;
			}
			int[] pos = project(loc.col, loc.row, t.height, scalar);
			t.drawSprite(g, facing, pos[0], pos[1], blockSize);
			if (t.prop != null) t.prop.drawSprite(g, facing, pos[0], pos[1], blockSize);

			for (Entity e : et.get(loc)) {
				e.sprite(facing).draw(g, pos[0], pos[1], blockSize);
			}

		}
	}

	public double getBlockSize(int width, int height) {
		return (width*0.9)/(r.width*1.5);
		//return 50;
	}

	/**
	 * Converts real-world co-ordinates into screen co-ordinates. Takes view Direction into account.
	 * Scales based on the scale field; centres based on the centre field. TODO: update this with final impl.
	 * @param x real-world x (column)
	 * @param y real-world y (row)
	 * @param z real-world z (height)
	 * @return 2-length array: [0] = x, [1] = y
	 */
	public int[] project(double x, double y, double z, double scale) {
		// first, translate x and y into co-ordinates that are more useful to us:
		int[] out = new int[2];

		x -= (r.width-1)/2.0;
		y -= (r.height-1)/2.0;

		double[][] mat = transformationMatrix();

		out[0] = (int) (scale * (x*mat[0][0] + y*mat[0][1] + z*mat[0][2]));
		out[1] = (int) (scale * (x*mat[1][0] + y*mat[1][1] + z*mat[1][2]));

		return out;
	}

	public int[] project(double x, double y, double scale) {
		return project(x, y, 0, scale);
	}

	/**
	 *
	 * @return 2x3 matrix A such that s = Aw for screen co-ordinate s and world co-ordinate w
	 */
	private double[][] transformationMatrix() {
		// factors of each position in matrix A - this allows the matricies below to simply look like +ve or -ve factors
		double a11 = Math.sqrt(2);
		double a12 = Math.sqrt(2);
		double a21 = X_Y;
		double a22 = Y_Y;
		switch (facing) {
			// pretty horrible to use discrete cases, but it's ultimately more simmple and easier to read than
			// using the magic direction numbers
			case NORTH:
				return new double[][] {
						{ a11,-a12, 0},
						{ a21, a22,-Z_Y}
				};
			case EAST:
				return new double[][] {
						{ a11, a12, 0},
						{-a21, a22,-Z_Y}
				};
			case SOUTH:
				return new double[][] {
						{-a11, a12, 0},
						{-a21,-a22,-Z_Y}
				};
			case WEST:
				return new double[][] {
						{-a11,-a12, 0},
						{ a21,-a22,-Z_Y}
				};
			default:
				throw new AssertionError("facing unknown direction");
		}
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
