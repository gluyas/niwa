package swen222.niwa.gui.graphics;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import swen222.niwa.model.world.Direction;

/**
 * A SpriteSet holds images for an entity/prop. It stores an image
 * for each facing direction; north, east, south, west.
 *
 * The class statically loads and stores sprites for the various props and enitities
 * across the game world. It parses an XML file which containsNode information about each prop/enitity
 * and their corresponding sprites.
 *
 * @author meiklehami1
 */
public class SpriteSet implements Visible, Serializable {

	private static final String SPRITE_LIST = "resource/images/spritesets.xml";
	private static final String DEFAULT_SPRITE = "resource/images/tiles/tempObject.png";

	private static final Map<String, SpriteSet> ALL = new HashMap<>();

	public final String name;										// name of this SpriteSet to assist with loading
	private transient Sprite[] sprites = new Sprite[4];		// sprite data for each direction - don't serialize

	// private constructor to ensure that all SpriteSets are statically initialised
	private SpriteSet(String name, Sprite[] sprites) {
		this.name = name;
		for (int i = 0; i < 4; i++) this.sprites[i] = sprites[i];
	}

	private SpriteSet(String name, Sprite sprite) {
		this.name = name;
		for (int i = 0; i < 4; i++) this.sprites[i] = sprite;
	}

	private SpriteSet(String name) {
		this.name = name;
	}


	/**
	 * Get the SpriteSet associated with the given String. These are outlined in a manifest file
	 * @param name the name of the SpriteSet to find
	 * @return the SpriteSet associated with that name, or null if not found
	 */
	public static SpriteSet get(String name) {
		return ALL.get(name);
	}

	@Override
	public Sprite sprite(Direction camera) {
		return sprites[camera.ordinal()];
	}

	/**
	 * Creates a copy of this SpriteSet, but facing in the provided direction,
	 * with this one presumed to be facing North.
	 * @param facing the Direction for the new SpriteSet to be facing
	 * @return a new SpriteSet instance
	 */
	public SpriteSet facing(Direction facing) {
		return new SpriteSet(name, sprites) {			// as terrible as making this anon class looks, it's actually
			@Override									// the easiest way to serialize the SpriteSet as we don't
			public Sprite sprite(Direction camera) {	// have to change the reconstitution algorithm
				return sprites[facing.relativeToPerspective(camera).ordinal()];
			}
		};
	}

	// SERIALIZATION

	// the sprites array is not serialized, so we need to pull it from the list
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.sprites = get(this.name).sprites;
	}

	// STATIC PARSING

	private void setSprite(Direction face, Sprite sprite){
		sprites[face.ordinal()] = sprite;
	}

	static {
		loadSprites();
	}

	public static void loadSprites() {
		if (!ALL.isEmpty()) return;
		try {
			// initial set up
			File file = new File(SPRITE_LIST);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();

			// get the SPRITE_LIST node and its children
			NodeList manifestNode = doc.getElementsByTagName("manifest");
			NodeList nodeList = manifestNode.item(0).getChildNodes();

			// iterate over each node and start parsing
			for (int i = 0; i < nodeList.getLength() ; i++) {
				Node currentNode = nodeList.item(i);
				String nodeName = currentNode.getNodeName();

				switch (nodeName){
					case "spriteset":
						parseSpriteSet(currentNode, new HashMap<>());
						break;
					case "group":
						parseGroup(currentNode, new HashMap<>());
						break;
				}
			}


		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private static void parseGroup(Node groupNode, Map<String, Sprite> context){
		Map<String, Sprite> localContext = new HashMap<>(context);
		NodeList children =  groupNode.getChildNodes();

		// iterate over each child
		for(int i = 0 ; i < children.getLength(); i++){
			Node currentNode = children.item(i);

			// make sure we're not casting a text node to an Element
			if(currentNode.getNodeType() == Node.ELEMENT_NODE){
				Element child = (Element) currentNode;

				// decide what to do with each child
				switch (child.getNodeName()){
					case "sprite":
						localContext.put(child.getAttribute("id"), parseSprite(currentNode));
						break;
					case "group":
						parseGroup(currentNode, localContext);
						break;
					case "spriteset":
						parseSpriteSet(currentNode, localContext);
						break;
				}
			}
		}
	}

	private static void parseSpriteSet(Node spriteSetNode, Map<String, Sprite> context){
		Map<String, Sprite> localContext = new HashMap<>(context);
		String ssName = ((Element) spriteSetNode).getAttribute("name");
		NodeList children = spriteSetNode.getChildNodes();

		// check if the SpriteSet has a faces node
		if(containsNode(children, "faces")){
			// iterate over children and parse sprites into localContext
			for(int i = 0 ; i < children.getLength(); i++){
				Node currentNode = children.item(i);

				// make sure we're not casting a text node to an Element
				if(currentNode.getNodeType() == Node.ELEMENT_NODE){
					Element child = (Element) currentNode;

					if(child.getNodeName().equals("sprite")){
						localContext.put(child.getAttribute("id"), parseSprite(currentNode));
					}
				}
			}

			// then parse the faces node; add result to ALL
			SpriteSet spriteSet = parseFaces(new SpriteSet(ssName), getNode(children, "faces"), localContext);
			ALL.put(ssName, spriteSet);
		}
		// else create a SpriteSet from the first Sprite element
		// and add it to ALL
		else{
			SpriteSet spriteSet = new SpriteSet(ssName, parseSprite(getNode(children, "sprite")));
			ALL.put(ssName, spriteSet);
		}
	}

	private static Sprite parseSprite(Node spriteNode){
		Element spriteElement = (Element)spriteNode;
		try {
			// each sprite will always have an image path, if its the path does not exist
			// then assign it a default image path
			File imgPath = new File(spriteElement.getAttribute("img"));
			Image img;
			if(imgPath.exists()){
				img = ImageIO.read(imgPath);
			}else{
				img = ImageIO.read(new File(DEFAULT_SPRITE));
			}

			// now store the other potential attributes; ax, ay, width
			String axAttr = spriteElement.getAttribute("ax");
			String ayAttr = spriteElement.getAttribute("ay");
			String widthAttr =  spriteElement.getAttribute("width");

			// now check which attributes have been specified, if they are
			// empty then assign them default values.
			double ax, ay, width;
			if(axAttr.isEmpty()){ ax = 0.5;}
			else{ ax = Double.parseDouble(axAttr);}

			if(ayAttr.isEmpty()){ ay = 0.25;}
			else{ ay = Double.parseDouble(ayAttr);}

			if(widthAttr.isEmpty()){ width = 1;}
			else{ width = Double.parseDouble(widthAttr);}

			// now create the Sprite
			return new Sprite(img, ax, ay, width);
		} catch (IOException e) {
			System.out.println(spriteElement.getAttribute("img"));
			e.printStackTrace();
		}


		return null;
	}

	private static SpriteSet parseFaces(SpriteSet ss, Node facesNode, Map<String, Sprite> context) {
		Element facesElement = (Element) facesNode;

		// north face
		String northFace = facesElement.getAttribute("n");
		Sprite northSprite = context.get(northFace);
		ss.setSprite(Direction.NORTH, northSprite);

		// east face
		String eastFace = facesElement.getAttribute("e");
		Sprite eastSprite = context.get(eastFace);
		ss.setSprite(Direction.EAST, eastSprite);

		// south face
		String southFace = facesElement.getAttribute("s");
		Sprite southSprite = context.get(southFace);
		ss.setSprite(Direction.SOUTH, southSprite);

		// west face
		String westFace = facesElement.getAttribute("w");
		Sprite westSprite = context.get(westFace);
		ss.setSprite(Direction.WEST, westSprite);

		return ss;
	}

	/**
	 * Contains returns whether or not a NodeList containsNode a type of Node.
	 * @param nList The NodeList to search through.
	 * @param nodeName The Node type to find.
	 * @return true if the list containsNode the node, else false.
	 */
	private static boolean containsNode(NodeList nList, String nodeName){
		for(int i = 0 ; i < nList.getLength() ;  i++){
			Node currentNode = nList.item(i);
			if(currentNode.getNodeName().equals(nodeName)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the first appearance of a specified Node type in a NodeList.
	 * @param nList The NodeList to search through.
	 * @param nodeName The Node type to get.
	 * @return A Node object if the node is found, else null.
	 */
	private static Node getNode(NodeList nList, String nodeName){
		for(int i = 0 ; i < nList.getLength() ;  i++){
			Node currentNode = nList.item(i);
			if(currentNode.getNodeName().equals(nodeName)){
				return currentNode;
			}
		}
		return null;
	}

	public static void main(String[] args){
		for(String s : ALL.keySet()){
			System.out.println(s);
		}
	}
}