package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import logic.GameLogic;

public class WonState extends VBox {
	// Constructor method
	public WonState(StackPane root, Stage primaryStage) {

		// Initialize congratulations label
		Label title = new Label();
		title.setText("Congratulations\nYou Win");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setTextFill(Color.GREEN);
		title.setFont(GameLogic.font(100));
		title.setTranslateY(-40);

		// Initialize trophy image
		ImageView trophy = new ImageView(new Image("/texture/endgame/trophy.jpg"));
		trophy.setPreserveRatio(true);
		trophy.setFitWidth(160);
		trophy.setTranslateY(-20);

		// Initialized final score
		Label finalScore = new Label();
		finalScore.setText("Your Score : " + GameLogic.getScore());
		finalScore.setTextFill(Color.YELLOW);
		finalScore.setFont(GameLogic.font(70));
		finalScore.setTranslateY(10);

		// Initialized retry button
		Label retry = new Label();
		retry.setText("Play Again");
		retry.setTextFill(Color.WHITE);
		retry.setFont(GameLogic.font(60));
		retry.setTranslateY(30);
		retry.setOnMouseClicked(event -> {
			GameLogic.gameStart(primaryStage, GameLogic.getPlayScene(), root);
			primaryStage.setScene(GameLogic.getPlayScene());
			GameLogic.playBackgroundMusic("playbg.wav");
		});

		// Initialize exit button
		Label exit = new Label();
		exit.setText("Exit");
		exit.setTextFill(Color.GRAY);
		exit.setFont(GameLogic.font(40));
		exit.setPadding(new Insets(40));
		exit.setTranslateY(30);
		exit.setOnMouseClicked(event -> {
			Platform.exit();
		});

		// Write new high score
		boolean newHighScore = GameLogic.setHighScore();

		// Initialize high score label
		Label highScore = new Label();

		// if high score's beaten, high score change color
		if (newHighScore) {
			highScore.setTextFill(Color.LIMEGREEN);
			highScore.setText("New " + GameLogic.getHighScore());
		} else {
			highScore.setTextFill(Color.WHITE);
			highScore.setText(GameLogic.getHighScore());
		}

		highScore.setFont(GameLogic.font(30));
		highScore.setTranslateY(50);
		highScore.setTextAlignment(TextAlignment.CENTER);

		// Set alignment
		setAlignment(Pos.CENTER);

		// Set background
		setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		// Add everything to scene
		getChildren().addAll(title, trophy, finalScore, retry, exit, highScore);

		// Set scene size
		setPrefHeight(800);
		setPrefWidth(800);
	}
}
