package swen222.niwa.file;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.sun.istack.internal.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;
import swen222.niwa.model.world.Direction;

/**
 * SpriteLoader provides a way of loading and storing sprites for the various props and enitities
 * across the game world. It parses an XML file which contains information about each prop/enitity
 * and their corresponding sprites.
 *
 * @author meiklehami1
 */
public class SpriteLoader {

	public static final String MANIFEST = "resource/images/spritesets.xml";
	public static final String DEFAULT_SPRITE = "resource/images/tiles/tempObject.png";
	private static final Map<String, SpriteSet> SPRITESETS = new HashMap<>();

	/**
	 * Get the SpriteSet associated with the given String. These are outlined in a manifest file
	 * @param name The name of the SpriteSet
	 * @return the SpriteSet associated with that name, or null if not found
	 */
	@Nullable
	public static SpriteSet get(String name) {
		return SPRITESETS.get(name);
	}

	/**
	 * A SpriteSet holds images for an entity/prop. It stores an image
	 * for each facing direction; north, east, south, west.
	 *
	 * @author Hamish M
	 *
	 */
	public static class SpriteSet implements Visible {
		private final Sprite[] sprites;

		private SpriteSet() {
			sprites  = new Sprite[4];
		}

		private SpriteSet(Sprite[] sprites) {
			this.sprites = sprites;
		}

		public SpriteSet(Sprite sprite) {
			sprites = new Sprite[4];
			for (int i = 0; i < 4; i++) sprites[i] = sprite;
		}

		public void setSprite(Direction face, Sprite sprite){
			sprites[face.ordinal()] = sprite;
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
			return new SpriteSet(sprites) {
				@Override
				public Sprite sprite(Direction camera) {
					System.out.println((camera.ordinal()+facing.ordinal())%4);
					return sprites[facing.relativeTo(camera).ordinal()];
				}
			};
		}
	}

	// PARSING

	static {
		try {
			// initial set up
			File file = new File(MANIFEST);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();

			// get the MANIFEST node and its children
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
		if(contains(children, "faces")){
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

			// then parse the faces node; add result to SPRITESETS
			SpriteSet spriteSet = parseFaces(get(children, "faces"), localContext);
			SPRITESETS.put(ssName, spriteSet);
		}
		// else create a SpriteSet from the first Sprite element
		// and add it to SPRITESETS
		else{
			SpriteSet spriteSet = new SpriteSet(parseSprite(get(children, "sprite")));
			SPRITESETS.put(ssName, spriteSet);
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

	private static SpriteSet parseFaces(Node facesNode, Map<String, Sprite> context) {
		SpriteSet ss = new SpriteSet();
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
	 * Contains returns whether or not a NodeList contains a type of Node.
	 * @param nList The NodeList to search through.
	 * @param nodeName The Node type to find.
	 * @return true if the list contains the node, else false.
	 */
	public static boolean contains(NodeList nList, String nodeName){
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
	public static Node get(NodeList nList, String nodeName){
		for(int i = 0 ; i < nList.getLength() ;  i++){
			Node currentNode = nList.item(i);
			if(currentNode.getNodeName().equals(nodeName)){
				return currentNode;
			}
		}
		return null;
	}

	// method is here for debugging purposes
	private Map<String, SpriteSet> getMap(){
		return SPRITESETS;
	}

	public static void main(String[] args){
		SpriteLoader sl = new SpriteLoader();
		Map<String, SpriteSet> map = sl.getMap();
		for(String s : map.keySet()){
			System.out.println(s);
		}
	}
}
