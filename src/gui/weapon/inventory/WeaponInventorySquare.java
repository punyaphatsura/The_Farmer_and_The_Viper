package gui.weapon.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.GameLogic;

public class WeaponInventorySquare extends Pane {
	private String name;

	public WeaponInventorySquare(int id, String name) {
		// Set Name
		setName(name);

		// Set pane size
		setPrefWidth(64);
		setPrefHeight(32);
		setMinWidth(64);
		setMinHeight(32);

		// Set background image of the pane
		Image texturesFile = new Image("texture/weapon/" + name + ".png");
		BackgroundSize bgSize = new BackgroundSize(64, 32, false, false, false, false);
		BackgroundImage bgImg = new BackgroundImage(texturesFile, null, null, null, bgSize);
		BackgroundFill[] bgFill = { new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY) };
		BackgroundImage[] bgImgA = { bgImg };

		setBackground(new Background(bgFill, bgImgA));

		Label label = new Label();
		label.setText(String.valueOf(id));
		label.setTextFill(Color.WHITE);
		label.setFont(GameLogic.font(40));
		label.setPadding(new Insets(40));
		label.setTranslateY(-42);

		getChildren().add(label);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
