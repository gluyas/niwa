package swen222.niwa.file;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.Door;
import swen222.niwa.model.entity.Rune;
import swen222.niwa.model.entity.RuneStone;
import swen222.niwa.model.entity.Seed;
import swen222.niwa.model.entity.Statue;
import swen222.niwa.model.puzzle.Puzzle;
import swen222.niwa.model.puzzle.PuzzleBuilder;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Prop;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Tile;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * Parses in a specified file and adds the tiles
 * and objects at the specified positions.
 * Provides methods that get important information about the
 * room.
 * @author Jack U
 * @author Hamish M
 *
 */
public class RoomParser {

	DocumentBuilderFactory factory;
	DocumentBuilder builder;

	File currentFile;
	Document doc;
	Element rootElement;

	public int width;
	public int height;
	private Room room;

	public RoomParser(File f){
		this.currentFile = f;
		parseFile();
		this.width = getWidth();
		this.height = getHeight();
	}

	/**
	 * Initially parses the file into the relevant fields so
	 * that it can be read. Will throw a relevant error if the parsing
	 * is not successful.
	 */
	public void parseFile(){


		try{

			DocumentBuilderFactory factory =
					DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();


			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
			ByteArrayInputStream input =  new ByteArrayInputStream(
					xmlStringBuilder.toString().getBytes("UTF-8"));
			doc = builder.parse(currentFile);
			rootElement = doc.getDocumentElement();


		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the width of the room
	 * Throws a NoSuchElementException if the width is not included in the file.
	 * @return
	 */
	public int getWidth(){

		NodeList list = rootElement.getElementsByTagName("width");
		if(list.getLength()!=1){
			throw new NoSuchElementException("There is no width from the file you are trying"
					+ " to read from");
		}
		String width = list.item(0).getTextContent();
		return Integer.parseInt(width);

	}

	/**
	 * Get the height of the room
	 * Throws a NoSuchElementException if the width is not included in the file.
	 * @return
	 */
	public int getHeight(){
		NodeList list = rootElement.getElementsByTagName("height");
		if(list.getLength()!=1){
			throw new NoSuchElementException("There is no height from the file you are trying"
					+ " to read from");
		}
		String height = list.item(0).getTextContent();
		return Integer.parseInt(height);

	}

	/**
	 * Gets the name of the room
	 * @return
	 */
	public String getName(){
		NodeList list = rootElement.getElementsByTagName("name");
		if(list.getLength()!=1){
			throw new NoSuchElementException("There is no name in this room!");
		}
		String name = list.item(0).getTextContent();
		return name;

	}

	/**
	 * Gets the spawn locations for the room.
	 * @return
	 */
	public int[][] getSpawns(){

		int[][] spawns = new int[4][2];

		int numSpawns = 0;
		NodeList list= null;

		for (int i = 0; i < 4; i++){

			switch(i){
			case 0:
				list = rootElement.getElementsByTagName("spawnNorth");
				break;
			case 1:
				list = rootElement.getElementsByTagName("spawnEast");
				break;
			case 2:
				list = rootElement.getElementsByTagName("spawnSouth");
				break;
			case 3:
				list = rootElement.getElementsByTagName("spawnWest");
				break;
			}

			if(list.getLength()!=0){
				Element el = (Element) list.item(0);
				int[] coords = getCoordsFromElement(el);
				spawns[i]=coords;
				numSpawns++;

			}
		}


		if(numSpawns==0){
			throw new Error("There must be at least one spawn location!");
		}

		return spawns;





	}


	/**
	 * Returns a 2D array of tiles that make up a room
	 * @return
	 * @throws Exception
	 */
	public Tile[][] getTiles(){



		Tile[][] tiles = new Tile[height][width];


		NodeList list = rootElement.getElementsByTagName("line");
		if(list.getLength()!=getHeight()){
			throw new IndexOutOfBoundsException("The representation of the tiles is an incorrect size");
		}



		//goes through all of the lines from top to bottom
		for(int row = 0; row<height; row++){

			String line = list.item(row).getTextContent();
			int col = 0;
			while(col<width){
				//breaks up the line into chars
				Tile t = null;


				char s = line.charAt(col*3);

				//first we need to read the height
				//note that this increases the position of col by 1
				char s2 = line.charAt((col*3)+1);


				//additionally read the 'space' after the tile.
				char s3 = line.charAt((col*3)+2);

				//this needs to be a space - if its not throw an error
				if(s3!=' '){
					throw new Error ("There should be a space here");
				}


				int blockHeight = Character.getNumericValue(s2);
				int randInt;

				//creates different tiles depending on the char
				switch(s){
				case 'g':
					randInt = new Random().nextInt(3)+1;
					t = new Tile(Tile.Type.GRASS, blockHeight, SpriteSet.get("grassBlock"+randInt), true);
					break;
				case 's':
					t = new Tile(Tile.Type.STONE, blockHeight, SpriteSet.get("stoneBlock"), true);
					break;
				case 'a':
					t = new Tile(Tile.Type.SAND, blockHeight, SpriteSet.get("sandBlock"), true);
					break;
				case 'd':
					t = new Tile(Tile.Type.DIRT, blockHeight, SpriteSet.get("dirtBlock"), true);
					break;
				case 'w':
					randInt = new Random().nextInt(2)+1;
					t = new Tile(Tile.Type.WATER, blockHeight, SpriteSet.get("waterBlock"+randInt), false);
					break;
				case 'q':
					t = new Tile(Tile.Type.WATER, blockHeight, SpriteSet.get("koi"), false);
					break;
				}
				// add the tile to the rest of the room tiles
				tiles[row][col]=t;
				col++;
			}

		}

		return tiles;
	}


	/**
	 * Gets the props from the xml, and returns a 2D array representation of them
	 * on the map.
	 * @return
	 */
	public Prop[][] getProps(){

		Prop[][] props = new Prop[height][width];

		NodeList list = rootElement.getElementsByTagName("prop");
		if(list.getLength()==0){
			return props;
		}

		//go through all the elements with the name 'prop'
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) list.item(i);

				//get the type, col, and row as strings
				String type = el.getAttribute("type");
				String stringCol = el.getAttribute("col");
				String stringRow = el.getAttribute("row");

				//convert them to appropriate types
				int col = Integer.valueOf(stringCol);
				int row = Integer.valueOf(stringRow);

				//add the prop in the 2D array
				int randInt;
				String des;
				switch(type){
				case "bamboo":
					des = "A stand of bamboo sways in the wind.";
					props[row][col] = new Prop("bamboo", SpriteSet.get("bamboo"), false, des);
					break;
				case "bigrock":
					randInt = new Random().nextInt(2)+1;
					des = "A mighty boulder stands in your path.";
					props[row][col] = new Prop("bigRock", SpriteSet.get("bigRock"+randInt), false, des);
					break;
				case "bigtree":
					randInt = new Random().nextInt(3)+1;
					des = "The graceful boughs of this tree fill you with a sense of peace.";
					props[row][col] = new Prop("bigTree", SpriteSet.get("bigTree"+randInt), false, des);
					break;
				case "bush":
					des = "What a cheery little bush!";
					props[row][col] = new Prop("bush", SpriteSet.get("bush"), false, des);
					break;
				case "fenceside":
					des = "It's just a fence.";
					props[row][col] = new Prop("fenceSide", SpriteSet.get("fenceSide"), false, des);
					break;
				case "fencefront":
					des = "It's just a fence.";
					props[row][col] = new Prop("fenceFront", SpriteSet.get("fenceFront"), false, des);
					break;
				case "grass":
					des = "A lush stand of tall grass.";
					props[row][col] = new Prop("grass", SpriteSet.get("grass"), true, des);
					break;
				case "stonepathfront":
					des = "A stone path, but where does it lead?";
					props[row][col] = new Prop("stonePathFront", SpriteSet.get("stonePathFront"), true, des);
					break;
				case "stonepathside":
					des = "A stone path, but where does it lead?";
					props[row][col] = new Prop("stonePathSide", SpriteSet.get("stonePathSide"), true, des);
					break;
				case "woodpathfront":
					des = "A wooden path to guide your way.";
					props[row][col] = new Prop("woodPathFront", SpriteSet.get("woodPathFront"), true, des);
					break;
				case "woodpathside":
					des = "A wooden path to guide your way.";
					props[row][col] = new Prop("woodPathSide", SpriteSet.get("woodPathSide"), true, des);
					break;
				case "soil":
					des = "A little patch of soil. It looks very fertile.";
					props[row][col] = new Prop("soil", SpriteSet.get("soil"), true, des);
					break;
				case "smalltree":
					randInt = new Random().nextInt(3)+1;
					des = "Like big trees, but smaller!";
					props[row][col] = new Prop("smallTree", SpriteSet.get("smallTree"+randInt), false, des);
					break;
				case "zen1":
					des = "There's something inscribed on the stone: 古池や蛙飛びこむ水のおと";
					props[row][col] = new Prop("zen1", SpriteSet.get("zen1"), false, des);
					break;
				case "zen2":
					des = "There's something inscribed on the stone: 初しぐれ猿も小蓑をほしげ也";
					props[row][col] = new Prop("zen2", SpriteSet.get("zen2"), false, des);
					break;
				}
			}
		}

		return props;

	}


	/**
	 * Gets the entities from the xml for this room, and returns a 2D string array
	 * of entities so that they can be converted into entities in the
	 * roomparser. If there are no entities, returns an empty list.
	 * @return
	 */

	public EntityTable<Entity> getEntities(){
		EntityTable<Entity> entities = new HashEntityTable<Entity>();

		NodeList list = rootElement.getElementsByTagName("entity");

		ArrayList<Statue> statues = new ArrayList<>();
		//go through all the elements with the name 'entity'
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) list.item(i);

				//get the type, col, and row as strings
				String type = el.getAttribute("type");
				String stringCol = el.getAttribute("col");
				String stringRow = el.getAttribute("row");
				String direction = el.getAttribute("dir");

				// get the facing direction
				Direction facing = Direction.fromString(direction);

				int col = Integer.valueOf(stringCol);
				int row = Integer.valueOf(stringRow);
				// we assume all the statues are listed before the door

				//add the string in the 2D array
				switch(type){
				case "door":
					entities.add(new Door(Location.at(room, col, row), statues, facing));
					break;
				case "rune1":
					entities.add(new Rune(Location.at(room, col, row), "circle", SpriteSet.get("rune1")));
					break;
				case "rune2":
					entities.add(new Rune(Location.at(room, col, row), "cross", SpriteSet.get("rune2")));
					break;
				case "rune3":
					entities.add(new Rune(Location.at(room, col, row), "lightning", SpriteSet.get("rune3")));
					break;
				case "runestone1":
					entities.add(new RuneStone(Location.at(room, col, row), "circle", facing));
					break;
				case "runestone2":
					entities.add(new RuneStone(Location.at(room, col, row), "cross", facing));
					break;
				case "runestone3":
					entities.add(new RuneStone(Location.at(room, col, row), "lightning", facing));
					break;
				case "seed":
					entities.add(new Seed(Location.at(room, col, row), SpriteSet.get("seed")));
					break;
				case "statue":
					Statue statue = new Statue(Location.at(room, col, row));
					statues.add(statue);
					entities.add(statue);
					break;
				}
			}
		}

		// PUZZLE OBJECTS - read base64 serial data

		for (PuzzleBuilder builder : getPuzzleBuilders()) {
			for (Puzzle.Cell cell : builder.build(room)) {
				entities.add(cell);
			}
		}

		return entities;
	}

	public PuzzleBuilder[] getPuzzleBuilders() {
		NodeList list = doc.getElementsByTagName("puzzle");

		if (list.getLength() > 0) {

			PuzzleBuilder[] builders = new PuzzleBuilder[list.getLength()];

			for (int i = 0; i < list.getLength(); i++) {
				try {
					byte[] puzzleData = Base64.getDecoder().decode(list.item(i).getTextContent());
					ObjectInputStream obj = new ObjectInputStream(new ByteArrayInputStream(puzzleData));

					builders[i] = (PuzzleBuilder) obj.readObject();

				} catch (IOException | ClassCastException e) {
					continue;
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			return builders;
		} else {
			return new PuzzleBuilder[0];
		}
	}

	private int [] getCoordsFromElement(Element el){

		int [] coords= new int [2];

		//get the type, col, and row as strings
		String stringCol = el.getAttribute("col");
		String stringRow = el.getAttribute("row");

		coords[0] = Integer.valueOf(stringCol);
		coords[1] =  Integer.valueOf(stringRow);

		return coords;


	}



	public void setLocationRoom(Room room) {
		this.room = room;

	}


}
