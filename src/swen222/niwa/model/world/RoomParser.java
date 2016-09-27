package swen222.niwa.model.world;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import swen222.niwa.model.world.Tile.TileType;

import javax.xml.parsers.*;
import java.io.*;
import java.util.NoSuchElementException;

/**
 * Parses in a specified file and adds the tiles 
 * and objects at the specified positions.
 * @author Jack U
 *
 */
public class RoomParser {
	
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	
	File currentFile;
	Document doc;
	Element rootElement;
	
	int width;
	int height;
	
	public RoomParser(File f){
		this.currentFile = f;
		parseFile();
		width = getWidth();
		height = getHeight();
	}
	
	/**
	 * Initially parses the file into the relevant fields so 
	 * that it can be read
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
	 * @return
	 */
	public int getHeight(){
		 NodeList list = rootElement.getElementsByTagName("height");
		 if(list.getLength()!=1){
			 throw new NoSuchElementException("There is no width from the file you are trying"
			 		+ " to read from");
		 }
		 String width = list.item(0).getTextContent();
		 return Integer.parseInt(width);
		
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
			for(int col = 0; col<width; col++){
				//breaks up the line into chars
				Tile t = new Tile(1,TileType.GRASSTILE);
				char s = line.charAt(col);
			
				//creates different tiles depending on the char
				switch(s){
				case 'g': 
					t=new Tile(1,TileType.GRASSTILE);
					break;
				case 's':
					t=new Tile(1,TileType.STONETILE);
					break;
				case 'a':
					t=new Tile(1,TileType.SANDTILE);
					break;
				case 'd':
					t=new Tile(1,TileType.DIRTTILE);
					break;
				case 'w':
					t=new Tile(1,TileType.WATERTILE);
					break;	
				}
				
				tiles[row][col]=t;
			}
			
		}
		
		return tiles;
	}
	
	

	
}
