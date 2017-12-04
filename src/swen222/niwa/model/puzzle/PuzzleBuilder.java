package swen222.niwa.model.puzzle;

import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PuzzleBuilder {

	private BuilderCell[][] cells;

	public PuzzleBuilder(Room room) {
		this.cells = new BuilderCell[room.width][room.height];
	}

	public Puzzle build(Room room) {
		Puzzle puzzle = new Puzzle();

		for (int col = 0; col < cells.length; col++) {
			for (int row = 0; row < cells[0].length; row++) {
				BuilderCell data = cells[col][row];
				if (data != null) {
					Puzzle.Cell cell = puzzle.new Cell(Location.at(room, col, row), data.rooted, data.getWalls());
					if (data.plant != null) {
						cell.plant = data.plant.make();		// TODO: implement more complex plant creation
					}
				}
			}
		}
		return puzzle;
	}

	public void addCell(int col, int row, Plant.Type plant, boolean rooted) {
		setCell(new BuilderCell(plant, rooted), col, row);
	}

	public void deleteCell(int col, int row) {
		setCell(null, col, row);
	}

	private void setCell(BuilderCell cell, int col, int row) {
		for (Direction edge : Direction.values()) {

			int adjCol = col + edge.relativeX();
			int adjRow = row + edge.relativeY();
			if (adjCol >= 0 && adjCol < cells.length && adjRow >= 0 && adjRow < cells[0].length) {

				BuilderCell adjCell = cells[adjCol][adjRow];
				if (adjCell != null) {
					adjCell.setWall(edge.opposite(), cell == null);
					continue;
				}
			}
			if (cell != null) {
				cell.setWall(edge, true);
			}
		}

		cells[col][row] = cell;
	}

	private class BuilderCell {
		private BitSet edges = new BitSet(Direction.values().length);
		private Plant.Type plant;
		private boolean rooted;

		public BuilderCell(Plant.Type plant, boolean rooted) {
			this.rooted = rooted;
			this.plant = plant;
		}

		public boolean getWall(Direction edge) {
			return edges.get(edge.ordinal());
		}

		public Direction[] getWalls() {
			List<Direction> walls = new ArrayList<>();
			for (Direction edge : Direction.values()) {
				if (getWall(edge)) walls.add(edge);
			}
			return walls.toArray(new Direction[walls.size()]);
		}

		public boolean setWall(Direction edge, boolean value) {
			boolean old = getWall(edge);
			edges.set(edge.ordinal(), value);
			return old;
		}
	}

}
