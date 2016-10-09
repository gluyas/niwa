package swen222.niwa.model.world;

import java.awt.Image;
import java.awt.List;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.gui.Sprite;
/**
 * This class was used during development. RIP in pasta; may the flying spaghetti monster have mercy on your soul.
 */
public class DevTexture {

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
		static Image zen2;



		static {
			try {
				bamboo = ImageIO.read(new File("resource/images/props/bamboo.png"));
				bigRock1 = ImageIO.read(new File("resource/images/props/bigrock1.png"));
				bigRock2 = ImageIO.read(new File("resource/images/props/bigrock2.png"));
				bigTree1 = ImageIO.read(new File("resource/images/props/bigtree1.png"));
				bigTree2 = ImageIO.read(new File("resource/images/props/bigtree2.png"));
				bigTree3 = ImageIO.read(new File("resource/images/props/bigtree3.png"));
				bush = ImageIO.read(new File("resource/images/props/bush.png"));
				dirtBlock = ImageIO.read(new File("resource/images/tiles/dirt-block.png"));
				fenceFront = ImageIO.read(new File("resource/images/props/fence-front.png"));
				fenceSide = ImageIO.read(new File("resource/images/props/fence-side.png"));
				flower = ImageIO.read(new File("resource/images/entities/flowerblue1.png"));
				grassBlock1= ImageIO.read(new File("resource/images/tiles/grass-block1.png"));
				grassBlock2 = ImageIO.read(new File("resource/images/tiles/grass-block2.png"));
				grassBlock3 = ImageIO.read(new File("resource/images/tiles/grass-block3.png"));
				grass = ImageIO.read(new File("resource/images/props/grass.png"));
				path1Side = ImageIO.read(new File("resource/images/props/path1-side.png"));
				path1Front = ImageIO.read(new File("resource/images/props/path1-front.png"));
				path2Side = ImageIO.read(new File("resource/images/props/path2-side.png"));
				path2Front = ImageIO.read(new File("resource/images/props/path2-front.png"));
				playerBlackFront = ImageIO.read(new File("resource/images/players/player-black-front.png"));
				playerBlackSide = ImageIO.read(new File("resource/images/players/player-black-side.png"));
				playerBlueFront = ImageIO.read(new File("resource/images/players/player-blue-front.png"));
				playerBlueSide = ImageIO.read(new File("resource/images/players/player-blue-side.png"));
				playerGrayFront = ImageIO.read(new File("resource/images/players/player-gray-front.png"));
				playerGraySide = ImageIO.read(new File("resource/images/players/player-gray-side.png"));
				playerGreenFront = ImageIO.read(new File("resource/images/players/player-green-front.png"));
				playerGreenSide = ImageIO.read(new File("resource/images/players/player-green-side.png"));
				playerWhiteFront = ImageIO.read(new File("resource/images/players/player-white-front.png"));
				playerWhiteSide = ImageIO.read(new File("resource/images/players/player-white-side.png"));
				sandBlock = ImageIO.read(new File("resource/images/tiles/sand-block.png"));
				seed = ImageIO.read(new File("resource/images/entities/seed.png"));
				smallTree1 = ImageIO.read(new File("resource/images/props/smalltree1.png"));
				smallTree2 = ImageIO.read(new File("resource/images/props/smalltree2.png"));
				smallTree3 = ImageIO.read(new File("resource/images/props/smalltree3.png"));
				soil = ImageIO.read(new File("resource/images/entities/soil.png"));
				stoneBlock = ImageIO.read(new File("resource/images/tiles/stone-block.png"));
				templateBlock = ImageIO.read(new File("resource/images/tiles/template-block.png"));
				templateObject = ImageIO.read(new File("resource/images/tiles/template-object.png"));
				waterBlock1 = ImageIO.read(new File("resource/images/tiles/water-block1.png"));
				waterBlock2 = ImageIO.read(new File("resource/images/tiles/water-block2.png"));
				waterBlock3 = ImageIO.read(new File("resource/images/tiles/water-block3.png"));
				zen1 = ImageIO.read(new File("resource/images/props/zen1.png"));
				zen2 = ImageIO.read(new File("resource/images/props/zen2.png"));

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
