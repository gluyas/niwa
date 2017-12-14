package swen222.niwa.gui.graphics;

import org.joml.*;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.puzzle.Spell;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Tile;

import java.awt.*;
import java.lang.Math;
import java.util.*;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Draws the state of a Room onto a graphics object
 *
 * @author Marc
 */
public class RoomRenderer implements Observer {

	public static final double 	Z_SCALE = 1 / 3.0;
	public static final double	VIEW_ANGLE = Math.atan(Math.sqrt(2));

	public static final double	JITTER_MAG_MIN = 0.05;
	public static final double	JITTER_MAG_MAX = 0.10;
	public static final double	JITTER_Z_SCALE = 3.00;

	public static final long 	ANIM_ROT_DURATION = 350;
	public static final double 	ANIM_ROT_EXPONENT = 3;
	public static final double 	ANIM_ROT_EXPLODE_FACTOR = 1.15;
	public static final double	ANIM_ROT_EXPLODE_EXPONENT = 1.2;

	public static final long	ANIM_MOV_DURATION = 75;

	public static final long 	ANIM_SPL_STEP_DURATION = 150;
	public static final double 	ANIM_SPL_STEP_DELAY = 0.75;

	public static final long	ANIM_SPL_FAIL_FREEZE = 150;
	public static final long 	ANIM_SPL_FAIL_DURATION = 100;
	public static final double	ANIM_SPL_FAIL_DELAY = 0.25;

	public static final Visible ANIM_SPL_GOOD = SpriteSet.get("spellBlue");
	public static final Visible ANIM_SPL_FAIL = SpriteSet.get("spellOrange");

	private Room room;
	private ObservableEntityTable<?> et;

	private int heightDiff;
	private Vector3d centreOffset;

	private Vector3d[][] jitter;
	private Map<Entity, Vector4d> entityOffset = new HashMap<>();	// using hg coordinates

	private Visible[][] tileOverride;

	private Direction facing = Direction.NORTH; // the world direction that is northeast from the user's perspective
	private double bearing = Math.toRadians(45);

	private double explodeFactor = 1;

	private List<Animator> animations = new LinkedList<>();
	private List<Animator> animationQueue = new ArrayList<>();

	public RoomRenderer(Room subject, ObservableEntityTable<?> et) {
		setRoom(subject);
		setEntityTable(et);
	}

	public Direction getFacing() {
		return facing;
	}

	public void setEntityTable(ObservableEntityTable<?> et) {
		if (this.et != null) this.et.deleteObserver(this);
		this.et = et;
		if (et != null) et.addObserver(this);
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

		tileOverride = new Visible[r.width][r.height];

		// JITTER GENERATION

		Random rng = new Random();
		jitter = new Vector3d[r.width][r.height];
		
		for (int row = 0; row < r.height; row++) {
			for (int col = 0; col < r.width; col++) {
				Vector3d offset = new Vector3d(0, 0, lerp(rng.nextDouble(), JITTER_MAG_MIN, JITTER_MAG_MAX))
						.rotateX(rng.nextDouble() * Math.PI*2)
						.rotateY(rng.nextDouble() * Math.PI*2)
						.mul(1, 1, JITTER_Z_SCALE);
				jitter[col][row] = offset;
			}
		}
	}

	public void drawSpell(Spell spell) {
		Deque<Spell> stack = new ArrayDeque<>();
		spell.forEach(stack::addFirst);
		animations.add(spellAnimation(
			stack, ANIM_SPL_STEP_DURATION, ANIM_SPL_STEP_DELAY, ANIM_SPL_GOOD,
			spell.terminal != null ? null : () -> {
				spell.forEach(stack::addLast);
				animationQueue.add(new Animator(ANIM_SPL_FAIL_FREEZE, (t) -> {
					tileOverride[spell.loc.col][spell.loc.row] = ANIM_SPL_FAIL;
					if (t >= 1) {
						animationQueue.add(spellAnimation(
								stack, ANIM_SPL_FAIL_DURATION, ANIM_SPL_FAIL_DELAY,
								ANIM_SPL_FAIL, null)
						);
					}
					return false;
				}));
				return true;
			}
		));
	}

	private Animator spellAnimation(Deque<Spell> stack, long duration, double delay, Visible override,
									BooleanSupplier callback) {
		Spell spell = stack.poll();
		if (spell == null) {	// TODO: stop this from ever happening
			System.err.println("Spell animation failed");
			return new Animator(0, (t) -> true);
		}
		tileOverride[spell.loc.col][spell.loc.row] = override;

		return new Animator(duration, (t) -> {
			if (t == -1) {
				if (stack.isEmpty()) {
					if (callback != null && callback.getAsBoolean()) return true;
				} else {
					animationQueue.add(spellAnimation(stack, duration, delay, override, callback));
				}
			} else if (t == 1) {
				tileOverride[spell.loc.col][spell.loc.row] = null;
			}
			return false;
		}, delay);
	}

	@Override
	// Update is called whenever an Entity in the Room is updated - listen for animations here
	public void update(Observable o, Object arg) {
		if (arg instanceof Entity.LocationUpdate) {		// move animation
			Entity.LocationUpdate update = (Entity.LocationUpdate) arg;

			Vector4d move = update.from.toVector4().sub(update.to.toVector4());
			move.w = 1;
			entityOffset.put(update.subject(), move);

			animations.add(new Animator(ANIM_MOV_DURATION, (t) -> {
				if (t >= 1) {
					entityOffset.remove(update.subject());
				} else {
					move.w = 1 - t;					// using hg coords to interpolate for us!
				}
				return false;
			}));
		}
	}

	public void setDebugCoordinates(int value) {
		debugCoordinates = value;
	}

	public int cycleDebugCoordinates() {
		debugCoordinates = (debugCoordinates +1)%3;
		return debugCoordinates;
	}

	public void rotateCW() {
		animations.add(rotationAnimation(facing.turnCW(), true));
//		facing = facing.turnCW();
//		bearing -= Math.toRadians(90);
	}

	public void rotateCCW() {
		animations.add(rotationAnimation(facing.turnCCW(), false));
//		facing = facing.turnCCW();
//		bearing += Math.toRadians(90);
	}

	private Animator rotationAnimation(Direction target, boolean clockwise) {
		double e0 = explodeFactor;
		double b0 = bearing;
		double b1 = Math.toRadians((target.bearingDeg() + 45));

		double b1v;									// 'virtual' bearing endpoint to ensure correct lerp direction
		if (clockwise && b1 > b0) {
			b1v = b1 - Math.PI*2;
		} else if (!clockwise && b1 < b0) {
			b1v = b1 + Math.PI*2;
		} else {
			b1v = b1;
		}

		return new Animator(ANIM_ROT_DURATION, (t) -> {
			if (t >= 1) {
				this.bearing = b1;
				this.explodeFactor = 1;
			} else {
				this.bearing = lerp(easeInOutPoly(t, ANIM_ROT_EXPONENT), b0, b1v);
				this.explodeFactor = lerp(
						easeInOutPoly(1 - 2 * Math.abs(t - 0.5), ANIM_ROT_EXPLODE_EXPONENT),
						e0, ANIM_ROT_EXPLODE_FACTOR
				);
			}

			if (t >= 0.5) this.facing = target;
			return false;
		});
	}

	public boolean animationsPending() {
		return !animations.isEmpty();
	}

	/**
	 * Renders this Room in a rectangle on a given swing graphics object
	 * @param g
	 * @param width
	 * @param height
	 */
	public void draw(Graphics g, int width, int height) {
		if (room == null || et == null) return;

		animations.removeIf(Animator::apply);					// advance and cull animations
		animations.addAll(animationQueue);
		animationQueue.clear();

		double scale = getRoomScale(width, height);
		double blockSize = scale * Math.sqrt(2);

		scale *= explodeFactor;

		g.translate(width/2, height/2);

		Matrix4d projection = new Matrix4d()					// orthographic projection matrix
				.rotate(VIEW_ANGLE, 1, 0, 0)
				.rotate(bearing, 0, 0, 1)
				.scale(1, 1, Z_SCALE);					// scale block height to be a fraction of width

		Matrix4d camera = projection							// camera position
				.translate(centreOffset, new Matrix4d());		// translate to centroid of level


		for (Location loc : new BackToFrontIterator(room, facing)) {
			Tile tile = room.tileAt(loc);

			Vector4d pos = loc.toVector4();
			pos.add(new Vector4d(jitter[loc.col][loc.row], 0));	// apply jitter

			camera.transform(pos);

			pos.mul(pos.w * scale);								// convert from hg coordinates & apply block scale
			pos.w = 1;

			if (tileOverride[loc.col][loc.row] == null) {
				tile.drawSprite(g, facing, (int) pos.x, (int) pos.y, blockSize);
			} else {
				tileOverride[loc.col][loc.row].drawSprite(g, facing, (int) pos.x, (int) pos.y, blockSize);
			}

			if (tile.prop != null) tile.prop.drawSprite(g, facing, (int) pos.x, (int) pos.y, blockSize);

			if (et != null) {
				for (Entity entity : et.get(loc)) {
					Vector4d entPos = pos;

					if (entityOffset.containsKey(entity)) {		// apply entity offset vector if present
						entPos = projection.transform(entityOffset.get(entity), new Vector4d());

						entPos.mul(entPos.w * scale);			// convert from hg coordinates & apply block scale
						entPos.add(pos);
						entPos.w = 1;
					}

					entity.sprite(facing).draw(g, (int) entPos.x, (int) entPos.y, blockSize);
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

	private int debugCoordinates = 0;

	public final int DEBUG_COORDS_DISABLED = 0;
	public final int DEBUG_COORDS_WHITE = 2;
	public final int DEBUG_COORDS_BLACK = 1;

	public double getRoomScale(int width, int height) {
		return 75;	// TODO: scale correctly
	}

	private static double lerp(double t, double v0, double v1) {
		return v0 + t * (v1 - v0);
	}

	private static Vector3d lerp(double t, Vector3d v0, Vector3d v1) {
		return lerp(t, v0, v1, new Vector3d());
	}

	private static Vector3d lerp(double t, Vector3d v0, Vector3d v1, Vector3d dest) {
		dest.x = lerp(t, v0.x, v1.x);
		dest.y = lerp(t, v0.y, v1.y);
		dest.x = lerp(t, v0.z, v1.z);
		return dest;
	}

	private static double clamp(double x, double min, double max) {
		if	    (x < min) x = min;
		else if (x > max) x = max;
		return x;
	}

	// EASING FUNCTIONS - see here for diagram: https://www.desmos.com/calculator/5ufledm5fp

	// ease-in: smooth at t0 (start); +ve p: ascending acceleration
	private static double easeInPoly(double t, double p) {
		return Math.pow(t, p);
	}

	// ease-out: smooth at t1 (end); +ve p: descending acceleration
	private static double easeOutPoly(double t, double p) {
		return 1 - Math.pow(1 - t, p);
	}

	// ease-in-out: +ve p: start slow, fast through middle; -ve p: start fast, slow through middle
	private static double easeInOutPoly(double t, double p) {
		if (t < 0.5) {
			return Math.pow(t * 2, p) / 2;
		} else {
			return 1 - Math.pow((1 - t) * 2, p) / 2;
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

			// run outside bounds so that we iterate onto first one
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
