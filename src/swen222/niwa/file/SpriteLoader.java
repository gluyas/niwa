package swen222.niwa.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    public static final String manifest = "resource/sprites/spritesets.xml";

    private static final Map<String, SpriteSet> SPRITESETS = new HashMap<>();

    static {
        try {
            // initial set up
            File file = new File(manifest);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            // get the manifest node and its children
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

    private static void parseSpriteSet(Node spriteSetNode, Map<String, Sprite> context){
        Map<String, Sprite> localContext = new HashMap<>(context);

        // check if it has a facing element
        // if not then just create a SS from the first Sprite element
        // otherwise iterate over sprites and add them to localContext
        // then parse faces; add result to spritesets under the name attribute
    }

    private static Sprite parseSprite(Node spriteNode){
        //make new SpriteSet from the node
        //set fields based on faces element's attributes
        // or if sprite node has no id element then set it to all faces
        return null;
    }
    
    private static SpriteSet parseFaces(Node facesNode, Map<String, Sprite> context) {
        //create new SS
        //assign sprite indicies by the faces' attributes
        return null;
    }


    static class SpriteSet implements Visible {
        private Sprite[] sprites = new Sprite[4];

        public SpriteSet() {

        }

        public SpriteSet(Sprite sprite) {
            for (int i = 0; i < 4; i++) sprites[i] = sprite;
        }

        @Override
        public Sprite sprite(Direction facing) {
            return sprites[facing.ordinal()];
        }
    }

    public static void main(String[] args){
        new SpriteLoader();
    }
}
