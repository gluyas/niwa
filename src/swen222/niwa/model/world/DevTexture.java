package swen222.niwa.model.world;

import java.awt.Image;
import java.awt.List;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.world.Tile.Texture;

public class DevTexture implements Texture {




		static Image bamboo;
		static Image bigRock;
		static Image bigTree;
		static Image bush;
		static Image dirtBlock;
		static Image fence;
		static Image flower;
		static Image grassBlock1;
		static Image grassBlock2;
		static Image grassBlock3;
		static Image grass;
		static Image path1;
		static Image path2;
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
		static Image smallTree;
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
				bamboo = ImageIO.read(new File("images/bamboo.png"));
				bigRock = ImageIO.read(new File("images/bigRock.png"));
				bush = ImageIO.read(new File("images/bush.png"));
				dirtBlock = ImageIO.read(new File("images/dirtBlock.png"));
				fence = ImageIO.read(new File("images/fence.png"));
				flower = ImageIO.read(new File("images/flower.png"));
				grassBlock1= ImageIO.read(new File("images/grassBlock1.png"));
				grassBlock2 = ImageIO.read(new File("images/grassBlock2.png"));
				grassBlock3 = ImageIO.read(new File("images/grassBlock3.png"));
				path1 = ImageIO.read(new File("images/path1.png"));
				path2 = ImageIO.read(new File("images/path2.png"));
				playerBlackFront = ImageIO.read(new File("images/playerBlackFront.png"));
				playerBlackSide = ImageIO.read(new File("images/playerBlackSide.png"));
				playerBlueFront = ImageIO.read(new File("images/playerBlueFrontpng"));
				playerBlueSide = ImageIO.read(new File("images/playerBlueSide.png"));
				playerGrayFront = ImageIO.read(new File("images/playerGrayFront.png"));
				playerGraySide = ImageIO.read(new File("images/playerGraySide.png"));
				playerGreenFront = ImageIO.read(new File("images/playerGreenFront.png"));
				playerGreenSide = ImageIO.read(new File("images/playerGreenSide.png"));
				playerWhiteFront = ImageIO.read(new File("images/playerWhiteFront.png"));
				playerWhiteSide = ImageIO.read(new File("images/playerWhiteSide.png"));
				sandBlock = ImageIO.read(new File("images/sandBlock.png"));
				seed = ImageIO.read(new File("images/seed.png"));
				smallRock = ImageIO.read(new File("images/smallRock.png"));
				smallTree = ImageIO.read(new File("images/smallTree.png"));
				soil = ImageIO.read(new File("images/soil.png"));
				stoneBlock = ImageIO.read(new File("images/stoneBlock.png"));
				templateBlock = ImageIO.read(new File("images/templateBlock.png"));
				templateObject = ImageIO.read(new File("images/templateObject.png"));
				waterBlock1 = ImageIO.read(new File("images/waterBlock1.png"));
				waterBlock2 = ImageIO.read(new File("images/waterBlock2.png"));
				waterBlock3 = ImageIO.read(new File("images/waterBlock3.png"));
				zen1 = ImageIO.read(new File("images/zen1.png"));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		final Image img;

		public DevTexture(Image img) {
			this.img = img;
		}

		public Sprite sprite(int height){
			return new Sprite(img,0,0);

		}


}
