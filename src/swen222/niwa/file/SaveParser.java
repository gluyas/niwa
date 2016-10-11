package swen222.niwa.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;


/**
 * Creates save data in the save file.
 * @author Jack U
 *
 */
public class SaveParser {


	File inputFile = new File("resource/save/savefile.xml");

	DocumentBuilderFactory factory;
	DocumentBuilder builder;

	Document doc;
	Element rootElement;

	public SaveParser(){
		parseMap();
	}

	/**
	 * Parses the initial map and overwrites it with the new one.
	 *
	 */
	public void parseMap(){



			DocumentBuilderFactory factory =
			DocumentBuilderFactory.newInstance();
			try {
				builder = factory.newDocumentBuilder();



			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");

			//this parses in the old file
			doc = builder.parse(inputFile);

			//needs to get a root element
			rootElement = doc.getDocumentElement();


			} catch (ParserConfigurationException e) {
				System.out.println("Error in save parser");
				e.printStackTrace();
			} catch (SAXException e) {
				System.out.println("Error in save parser");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error in save parser");
				e.printStackTrace();
			}

	}


	/**
	 * Saves a given map into the save file.
	 * Formatting for the row / width is not needed because the
	 * width and height is given in the arguments.
	 * @param map
	 * @param width
	 * @param height
	 */
	public void saveMap(Room [][] map, int width, int height){

		for(int row = 0; row< height; row++){
			for(int col = 0; col < width; col++){

				Element room = doc.createElement("Room");
				String name = map[row][col].name;
				Text roomName = doc.createTextNode(name);
				room.appendChild(roomName);
				rootElement.appendChild(room);
			}
		}


	}


	/**
	 * Saves all the important information about the players.
	 * This includes their name, their location, their points and their inventory.
	 * @param players
	 */
	public void savePlayers(Player[] players){


		for(Player player: players){

			String name = player.getName();
			Location playerLoc; // how do I get this????
			int points = player.getPoints();
			ArrayList<Entity> inventory;

			//top class element for a player
			Element playerElem = doc.createElement("Player");

			//adding player name
			Element playerName = doc.createElement("PlayerName");
			Text nameText = doc.createTextNode(name);
			playerName.appendChild(nameText);
			playerElem.appendChild(playerName);

			//top class element for player location
			Element location = doc.createElement("Location");

			Element room = doc.createElement("Room");
			Element col = doc.createElement("Col");
			Element row = doc.createElement("Row");

			Element inventoryElem = doc.createElement("Inventory");


		}

	}

	public static void saveEntities(){


	}


	public void writeSave(){


		Element testElement = doc.createElement("testElement");



		//creating a divergent stream to write over old file
		DOMSource source = new DOMSource (doc);
		String path = "resource/save/savefile.xml";
		File f = new File(path);
		Result result = new StreamResult(f);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//this line overwrites the old file with the new one.
			transformer.transform(source,result);


		} catch (TransformerConfigurationException e) {
			System.out.println("Error in save parser");
			e.printStackTrace();
		} catch (TransformerException e) {
			System.out.println("Error in save parser");
			e.printStackTrace();
		}





	}


}
