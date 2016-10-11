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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.util.HashEntityTable;
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

			removeAll(rootElement);



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
				String name = map[row][col].getName();
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
	public void savePlayers(PlayerEntity[] players){


		for(int i = 0; i <players.length; i++){

			PlayerEntity player = players[i];

			String name = player.getName();
			Location playerLoc = player.getLocation();
			int points = player.getPoints();
			ArrayList<ObjectEntity> inventory = player.getInventory();

			//top class element for a player
			Element playerElem = doc.createElement("Player");

			savePlayerName(playerElem, name);
			savePlayerLocation(playerElem, playerLoc);
			savePlayerPoints(playerElem,points);
			savePlayerInv(playerElem,inventory);

			rootElement.appendChild(playerElem);
		}

	}


	public void savePlayerName(Element e, String name){

		// -------------PLAYERNAME------------------//

		Element playerName = doc.createElement("PlayerName");
		Text nameText = doc.createTextNode(name);

		playerName.appendChild(nameText);
		e.appendChild(playerName);

	}

	public void savePlayerLocation(Element e, Location playerLoc){

		// -------------LOCATION-----------------//
		System.out.println("Saving player location");
		//top class element for player location
		Element location = doc.createElement("Location");

		//get location information
		Element room = doc.createElement("Room");
		Text roomText = doc.createTextNode(playerLoc.room.name);
		room.appendChild(roomText);

		Element col = doc.createElement("Col");
		Text colText = doc.createTextNode(Integer.toString(playerLoc.col));
		col.appendChild(colText);

		Element row = doc.createElement("Row");
		Text rowText = doc.createTextNode(Integer.toString(playerLoc.row));
		row.appendChild(rowText);

		//append to location top element
		location.appendChild(room);
		location.appendChild(col);
		location.appendChild(row);
		e.appendChild(location);
	}


	public void savePlayerPoints(Element e, int points){

		// -------------POINTS-----------------//
		System.out.println("Saving player points");

		Element playerPoints = doc.createElement("PlayerPoints");
		Text pointsText = doc.createTextNode(Integer.toString(points));
		e.appendChild(playerPoints);

	}

	public void savePlayerInv(Element e, ArrayList<ObjectEntity> inv){

		// -------------INVENTORY---------------//
		System.out.println("Saving player inventory");

		Element inventoryElem = doc.createElement("Inventory");

		for(ObjectEntity entity: inv){
			Element item = doc.createElement("Item");
			Text itemName = doc.createTextNode(e.toString());

			item.appendChild(itemName);
			inventoryElem.appendChild(item);
		}


		e.appendChild(inventoryElem);

	}





	public void saveEntities(HashEntityTable<?> entities){


		Element entityElem = doc.createElement("Entities");


		for(Entity entity:entities){

			Location entityLoc = entity.getLocation();
			Element location = doc.createElement("Location");

			//Element entityName = doc.createElement("Type");
			//get location information
			Element room = doc.createElement("Room");
			Text roomText = doc.createTextNode(entityLoc.room.name);
			room.appendChild(roomText);

			Element col = doc.createElement("Col");
			Text colText = doc.createTextNode(Integer.toString(entityLoc.col));
			col.appendChild(colText);

			Element row = doc.createElement("Row");
			Text rowText = doc.createTextNode(Integer.toString(entityLoc.row));
			row.appendChild(rowText);

			//append to location top element
			location.appendChild(room);
			location.appendChild(col);
			location.appendChild(row);

			entityElem.appendChild(location);
			rootElement.appendChild(entityElem);



		}


	}


	public void writeSave(){


		//Element testElement = doc.createElement("testElement2");
		//Text testText = doc.createTextNode("testText");

		//testElement.appendChild(testText);
		//rootElement.appendChild(testElement);




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

	public static void removeAll(Node node) {
	    while (node.hasChildNodes())
	        node.removeChild(node.getFirstChild());
	}


}



