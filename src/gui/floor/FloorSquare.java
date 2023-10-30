package gui.floor;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class FloorSquare extends Pane {
	private static final Image TEXTURES_FILE = new Image("texture/floor/floor.png");
	private static Map<Integer, Image> texturesMap;

	private int type;
	private int xPosition;
	private int yPosition;

	public FloorSquare(int x, int y, int type) {
		// Texture Loading
		texturesMap = new HashMap<>();
		int id = 0;
		for (int col = 1; col < 47; col++) {
			for (int row = 0; row < 16; row++) {
				PixelReader reader = TEXTURES_FILE.getPixelReader();
				WritableImage newImage = new WritableImage(reader, col * 32, row * 32, 32, 32);
				texturesMap.put(id, newImage);
				id++;
			}
		}

		// Set pane position
		setxPosition(x);
		setyPosition(y);

		// Set pane size
		setPrefWidth(32);
		setPrefHeight(32);
		setMinWidth(32);
		setMinHeight(32);

		// Set background image of the pane
		BackgroundSize bgSize = new BackgroundSize(32, 32, false, false, false, false);
		BackgroundImage bgImg = new BackgroundImage(texturesMap.get(type), null, null, null, bgSize);
		BackgroundImage[] bgImgA = { bgImg };
		setBackground(new Background(bgImgA));
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

}
