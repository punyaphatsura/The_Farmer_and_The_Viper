package gui.timer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.GameLogic;

public class GameTimerPane extends VBox {

	private static Label time;

	public GameTimerPane(String gameTimer) {
		// Initialize label text with timer
		time = new Label(gameTimer);
		time.setFont(GameLogic.font(60));
		time.setTextFill(Color.WHITE);

		// Set vbox size
		setMaxHeight(60);
		setMaxWidth(300);

		// Set alignment
		setAlignment(Pos.TOP_CENTER);

		// Set background color
		setBackground(
				new Background(new BackgroundFill(Color.web("0x000000", 0.5), new CornerRadii(10), Insets.EMPTY)));

		// Add label to vbox
		getChildren().add(time);

		// Set vbox position
		setTranslateY(-350);
		setTranslateX(-250);
	}

	// Update timer text
	public void updateGameTimer(String gameTimer) {
		time.setText(gameTimer);
	}
}
