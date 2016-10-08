package swen222.niwa.file;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Prop;
import swen222.niwa.model.world.Tile;
import swen222.niwa.model.world.Tile.TileType;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Parses in a specified file and adds the tiles
 * and objects at the specified positions.
 * Provides methods that get important information about the
 * room.
 * @author Jack U
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
				Tile t = new Tile(1,TileType.GRASSTILE);


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

				//creates different tiles depending on the char
				switch(s){
					case 'g':
						t=new Tile(blockHeight,TileType.GRASSTILE);
						break;
					case 's':
						t=new Tile(blockHeight,TileType.STONETILE);
						break;
					case 'a':
						t=new Tile(blockHeight,TileType.SANDTILE);
						break;
					case 'd':
						t=new Tile(blockHeight,TileType.DIRTTILE);
						break;
					case 'w':
						t=new Tile(blockHeight,TileType.WATERTILE);
						break;
					case 'q':
						t=new Tile(blockHeight,TileType.KOITILE);
						break;
				}


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
                Prop.PropType propType = stringToEnumProp(type);
                int col = Integer.valueOf(stringCol);
                int row = Integer.valueOf(stringRow);

                //add the prop in the 2D array
                props[row][col]=new Prop(propType);


            }
		}

		return props;

		}


	/**
	 * Gets the entities from the xml, and returns a 2D array representation of them
	 * on the map. *NOTE* In construction - Commented out for the moment
	 * @return
	 */


	/*
	public Entity[][] getEntities(){

		Entity[][] entities = new Entity[height][width];

		NodeList list = rootElement.getElementsByTagName("entity");
		if(list.getLength()==0){
			return entities;
		}

		//go through all the elements with the name 'entity'
		for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) list.item(i);

                //get the type, col, and row as strings
                String type = el.getAttribute("type");
                String stringCol = el.getAttribute("col");
                String stringRow = el.getAttribute("row");

                int col = Integer.valueOf(stringCol);
                int row = Integer.valueOf(stringRow);

                //convert them to appropriate types
                switch(type){


                Prop.PropType propType = stringToEnum(type);


                //add the prop in the 2D array
                props[row][col]=new Prop(propType);


            }
		}

		return props;

		}
		*/



	public Prop.PropType stringToEnumProp(String type){

		String capsString = type.toUpperCase();
		return Enum.valueOf(Prop.PropType.class, capsString);

	}


	public Prop.PropType stringToEnumEntity(String type){

		String capsString = type.toUpperCase();
		return Enum.valueOf(Prop.PropType.class, capsString);

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


}
