package gui.score;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.GameLogic;

public class ScorePane extends VBox {

	private Label score;

	public ScorePane() {
		// Initialize label text from score and multiplier
		score = new Label(String.valueOf(GameLogic.getScore()) + " " + GameLogic.getMultiply() + "X");
		score.setFont(GameLogic.font(60));
		score.setTextFill(Color.WHITE);

		// Set vbox size
		setMaxHeight(60);
		setMaxWidth(300);
		
		// Set alignment
		setAlignment(Pos.TOP_CENTER);

		// Set background color
		setBackground(
				new Background(new BackgroundFill(Color.web("0x000000", 0.5), new CornerRadii(10), Insets.EMPTY)));

		// Add label to vbox
		getChildren().add(score);

		// Set vbox position
		setTranslateY(-350);
		setTranslateX(250);
	}

	public void updateScore() {
		// Update text
		score.setText(String.valueOf(GameLogic.getScore()) + " " + GameLogic.getMultiply() + "X");

		// Update color
		if (GameLogic.getMultiply() == 1) {
			score.setTextFill(Color.WHITE);
		} else if (GameLogic.getMultiply() == 2) {
			score.setTextFill(Color.YELLOW);
		} else if (GameLogic.getMultiply() == 3) {
			score.setTextFill(Color.ORANGE);
		} else if (GameLogic.getMultiply() < 10) {
			score.setTextFill(Color.RED);
		} else if (GameLogic.getMultiply() < 20) {
			score.setTextFill(Color.MAGENTA);
		} else if (GameLogic.getMultiply() == 20) {
			score.setTextFill(Color.LIGHTBLUE);
		}
	}

	public void run() {
		Thread score = new Thread(() -> {
			// Loop update score while game is started
			while (GameLogic.isStartGame()) {
				Platform.runLater(() -> {
					updateScore();
				});

				GameLogic.delay(100);
			}
		});
		score.start();
	}
}
