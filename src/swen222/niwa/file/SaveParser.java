package swen222.niwa.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

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
	
	public void parseMap(){
		
		try{

			DocumentBuilderFactory factory =
			DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();


			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
		
			//this parses in the old file
			doc = builder.parse(inputFile);
	
			//needs to get a root element
			rootElement = doc.getDocumentElement();
			
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
			
			} catch (ParserConfigurationException | SAXException | IOException e) {
				System.out.println("Error in save parser");
				e.printStackTrace();
			}
	}
	
	
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
	
	public static void savePlayerLocs(){
		
		
	}
	
	public static void saveEntities(){
		
		
	}
	

}
