package swen222.niwa.editor;

import swen222.niwa.gui.graphics.RoomRenderer;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.puzzle.Plant;
import swen222.niwa.model.puzzle.Puzzle;
import swen222.niwa.model.puzzle.PuzzleBuilder;
import swen222.niwa.model.puzzle.Spell;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;

import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import static java.awt.event.KeyEvent.*;



/**
 * Scrap class for demonstrating at the lab.
 *
 * @author Marc
 */
public class EditorFrame extends JFrame implements Observer, KeyListener {

	EditorPanel panel;
	RoomRenderer renderer;
	Room room;
	EditorPlayer player;
	String stageName = "resource";
	Plant previewPlant;


	HashEntityTable<Entity> displayEntities = new HashEntityTable<>();

	boolean puzzleMode = true;
	PuzzleBuilder puzzleBuilder;
	Puzzle puzzleOutput;


	public EditorFrame() {
		super("Level Editor");
		renderer = new RoomRenderer(null, null);
		previewPlant = new Plant(Plant.Type.BASIC);

		//load();
		loadInitialMap();

		panel = new EditorPanel(renderer);
		add(panel);

		panel.setEnabled(true);
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(1280, 720));
		panel.setSize(1280, 720);
		addKeyListener(this);

		pack();
		setVisible(true); // make sure we are visible!
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Timer refresh = new Timer(1000 / 120, (e) -> { if (renderer.animationsPending()) this.repaint();});
		refresh.setRepeats(true);
		refresh.start();
	}

	/*
	Made the editor load the mountain map first for ease of use
	 */
	private void loadInitialMap(){
		stageName="resource/rooms/niceForest.xml";
		refresh();

	}

	private void load() {
		JFileChooser chooser = new JFileChooser(stageName);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"XML map files", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			stageName = chooser.getSelectedFile().getAbsolutePath();
			refresh();
		} else System.err.println("Load failed");
	}

	private void refresh() {
		displayEntities.remove(player);
		displayEntities.deleteObserver(this);
		displayEntities = new HashEntityTable<>();

		Room newRoom = Room.newFromFile(new File(stageName), 0, 0, displayEntities);
		player = new EditorPlayer(Location.at(newRoom, 0, 0));
		displayEntities.add(player);
		displayEntities.addObserver(this);

		renderer.setRoom(newRoom);
		renderer.setEntityTable(displayEntities);
		room = newRoom;
		puzzleMode = true;

		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case VK_W:
				player.move(directionRelativeToMap(Direction.NORTH));
				break;

			case VK_A:
				player.move(directionRelativeToMap(Direction.WEST));
				break;

			case VK_S:
				player.move(directionRelativeToMap(Direction.SOUTH));
				break;

			case VK_D:
				player.move(directionRelativeToMap(Direction.EAST));
				break;

			case VK_Q:
				renderer.rotateCW();
				repaint();
				break;

			case VK_E:
				renderer.rotateCCW();
				repaint();
				break;

			case VK_F:
				Spell spell = Spell.invoke(displayEntities, player.getLocation(), player.getFacing());
				renderer.drawSpell(spell);
				break;

			case VK_F5:
			case VK_R:
				refresh();
				break;

			case VK_L:
				load();
				break;

			case VK_N:
				renderer.cycleDebugCoordinates();
				repaint();
				break;

			case VK_P:				// toggle puzzle mode
				puzzleMode = !puzzleMode;
				if (puzzleMode) {
					puzzleBuilder = new PuzzleBuilder(room);
				} else {
					if (puzzleOutput != null) {
						for (Puzzle.Cell cell : puzzleOutput) {
							displayEntities.remove(cell);
						}
						puzzleOutput = null;
					}
				}
				this.panel.setMode(puzzleMode);
				break;

		}

		// PUZZLE EDITOR CONTROLS
		if (puzzleMode) switch (code) {
			case VK_OPEN_BRACKET: 		// [ - insert cell
				puzzleBuilder.addCell(player.getLocation().col, player.getLocation().row, null, false);
				previewPuzzleOutput();
				break;

			case VK_CLOSE_BRACKET: 		// ] - delete cell
				puzzleBuilder.deleteCell(player.getLocation().col, player.getLocation().row);
				previewPuzzleOutput();
				break;

			case VK_Z:					// Z - dump serial data
				try {
					ByteArrayOutputStream raw = new ByteArrayOutputStream();
					new ObjectOutputStream(raw).writeObject(puzzleBuilder);

					String encoded = new String(Base64.getEncoder().encode(raw.toByteArray()));

					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(encoded), null);
					System.out.printf("PUZZLE SERIAL COPIED TO CLIPBOARD (%s)%n%s%n",
							LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME),
							encoded
					);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			case VK_BACK_SLASH: 		// [ - create new flower
				puzzleBuilder.addCell(player.getLocation().col, player.getLocation().row, previewPlant.type, false);
				previewPuzzleOutput();
				break;
			case VK_SEMICOLON: 		// [ - change flower type to the right
				previewPlant = previewPlant.cycleType(true);
				panel.setPreviewPlant(previewPlant);
				break;
			case VK_QUOTE: 		// [ - change flower type to the left
				previewPlant = previewPlant.cycleType(false);
				panel.setPreviewPlant(previewPlant);
				break;



		}
		//repaint();
	}


	private void previewPuzzleOutput() {
		if (puzzleOutput != null) {
			for (Puzzle.Cell cell : puzzleOutput) {
				displayEntities.remove(cell);
			}
		}
		if (puzzleBuilder != null) {
			puzzleOutput = puzzleBuilder.build(room);
			for (Puzzle.Cell cell : puzzleOutput) {
				displayEntities.add(cell);
			}
		} else {
			puzzleOutput = null;
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * Returns the correct direction for the player to move, relative
	 * to the map orientation.
	 * @param d
	 * @return
	 */
	public Direction directionRelativeToMap(Direction d){

		Direction roomDirection = renderer.getFacing();

		switch(roomDirection){

			case NORTH:
				return d;

			case WEST:
				return d.turnCCW();

			case SOUTH:
				d=d.turnCW();
				return d.turnCW();

			case EAST:
				return d.turnCW();
		}
		return d;

	}

	public static void main(String[] args) {
		new EditorFrame();
	}

}
