package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import logic.GameLogic;

public class TitleState extends VBox {
	// Constructor method
	public TitleState(StackPane root, Stage primaryStage) {

		// Initialize game title label
		Label title = new Label();
		title.setText("The Farmer and\nThe Viper");
		title.setTextFill(Color.WHITE);
		title.setFont(GameLogic.font(80));
		title.setTranslateY(-80);
		title.setTextAlignment(TextAlignment.CENTER);

		// Initialize high score label
		Label highScore = new Label();
		highScore.setText(GameLogic.getHighScore());
		highScore.setTextFill(Color.WHITE);
		highScore.setFont(GameLogic.font(30));
		highScore.setTranslateY(300);
		highScore.setTextAlignment(TextAlignment.CENTER);

		// Initialize start button
		Label start = new Label();
		start.setText("Start");
		start.setTextFill(Color.WHITE);
		start.setFont(GameLogic.font(60));
		start.setOnMouseClicked(event -> {
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
		exit.setOnMouseClicked(event -> {
			Platform.exit();
		});

		// Set alignment
		setAlignment(Pos.CENTER);

		// Set background
		setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		// Add everything to scene
		getChildren().addAll(title, highScore, start, exit);

		// Set scene size
		setPrefHeight(800);
		setPrefWidth(800);

		GameLogic.playBackgroundMusic("startbg.wav");
	}
}
