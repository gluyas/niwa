package swen222.niwa.model.world;

import java.awt.Image;
import java.awt.List;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.world.Tile.Texture;

public class DevTexture implements Texture {

		static Image bamboo;
		static Image bigRock1;
		static Image bigRock2;
		static Image bigTree1;
		static Image bigTree2;
		static Image bigTree3;
		static Image bush;
		static Image dirtBlock;
		static Image fenceSide;
		static Image fenceFront;
		static Image flower;
		static Image grassBlock1;
		static Image grassBlock2;
		static Image grassBlock3;
		static Image grass;
		static Image path1Side;
		static Image path1Front;
		static Image path2Side;
		static Image path2Front;
		static Image playerBlackFront;
		static Image playerBlackSide;
		static Image playerBlueFront;
		static Image playerBlueSide;
		static Image playerGrayFront;
		static Image playerGraySide;
		static Image playerGreenFront;
		static Image playerGreenSide;
		static Image playerWhiteFront;
		static Image playerWhiteSide;
		static Image sandBlock;
		static Image seed;
		static Image smallRock;
		static Image smallTree1;
		static Image smallTree2;
		static Image smallTree3;
		static Image soil;
		static Image stoneBlock;
		static Image templateBlock;
		static Image templateObject;
		static Image waterBlock1;
		static Image waterBlock2;
		static Image waterBlock3;
		static Image zen1;



		static {
			try {
				bamboo = ImageIO.read(new File("resource/images/bamboo.png"));
				bigRock1 = ImageIO.read(new File("resource/images/bigRock1.png"));
				bigRock2 = ImageIO.read(new File("resource/images/bigRock2.png"));
				bigTree1 = ImageIO.read(new File("resource/images/bigTree1.png"));
				bigTree2 = ImageIO.read(new File("resource/images/bigTree2.png"));
				bigTree3 = ImageIO.read(new File("resource/images/bigTree3.png"));
				bush = ImageIO.read(new File("resource/images/bush.png"));
				dirtBlock = ImageIO.read(new File("resource/images/dirt-Block.png"));
				fenceFront = ImageIO.read(new File("resource/images/fence-front.png"));
				fenceSide = ImageIO.read(new File("resource/images/fence-side.png"));
				flower = ImageIO.read(new File("resource/images/flower.png"));
				grassBlock1= ImageIO.read(new File("resource/images/grass-Block1.png"));
				grassBlock2 = ImageIO.read(new File("resource/images/grass-Block2.png"));
				grassBlock3 = ImageIO.read(new File("resource/images/grass-Block3.png"));
				path1Side = ImageIO.read(new File("resource/images/path1-side.png"));
				path1Front = ImageIO.read(new File("resource/images/path1-front.png"));
				path2Side = ImageIO.read(new File("resource/images/path2-side.png"));
				path2Front = ImageIO.read(new File("resource/images/path2-front.png"));
				playerBlackFront = ImageIO.read(new File("resource/images/player-black-front.png"));
				playerBlackSide = ImageIO.read(new File("resource/images/player-black-side.png"));
				playerBlueFront = ImageIO.read(new File("resource/images/player-blue-front.png"));
				playerBlueSide = ImageIO.read(new File("resource/images/player-blue-side.png"));
				playerGrayFront = ImageIO.read(new File("resource/images/player-gray-front.png"));
				playerGraySide = ImageIO.read(new File("resource/images/player-gray-side.png"));
				playerGreenFront = ImageIO.read(new File("resource/images/player-green-front.png"));
				playerGreenSide = ImageIO.read(new File("resource/images/player-green-side.png"));
				playerWhiteFront = ImageIO.read(new File("resource/images/player-white-front.png"));
				playerWhiteSide = ImageIO.read(new File("resource/images/player-white-side.png"));
				sandBlock = ImageIO.read(new File("resource/images/sand-Block.png"));
				seed = ImageIO.read(new File("resource/images/seed.png"));
				smallRock = ImageIO.read(new File("resource/images/smallRock.png"));
				smallTree1 = ImageIO.read(new File("resource/images/smalltree1.png"));
				smallTree2 = ImageIO.read(new File("resource/images/smalltree2.png"));
				smallTree3 = ImageIO.read(new File("resource/images/smalltree3.png"));
				soil = ImageIO.read(new File("resource/images/soil.png"));
				stoneBlock = ImageIO.read(new File("resource/images/stone-Block.png"));
				templateBlock = ImageIO.read(new File("resource/images/template-block.png"));
				templateObject = ImageIO.read(new File("resource/images/template-object.png"));
				waterBlock1 = ImageIO.read(new File("resource/images/water-Block1.png"));
				waterBlock2 = ImageIO.read(new File("resource/images/water-Block2.png"));
				waterBlock3 = ImageIO.read(new File("resource/images/water-Block3.png"));
				zen1 = ImageIO.read(new File("resource/images/zen1.png"));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		final Sprite img;

		public DevTexture(Image img) {
			this.img = new Sprite(img, 0.5, 0.25);
		}

		public DevTexture(Image img, double aX, double aY) {
			this.img = new Sprite(img, aX, aY);
		}

		public Sprite sprite(int height){
			return img;
		}


}
