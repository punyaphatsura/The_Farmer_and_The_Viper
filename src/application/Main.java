package application;

import javax.sound.sampled.AudioSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logic.GameLogic;

public class Main extends Application {
	private StackPane root;

	public StackPane getRoot() {
		return root;
	}

	public void setRoot(StackPane root) {
		this.root = root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Initialize root
		root = new StackPane();

		// Initialize background music player
		GameLogic.setPlayer(AudioSystem.getClip());

		// Set root size
		root.setPrefHeight(800);
		root.setPrefWidth(800);

		// Disable resize
		primaryStage.setResizable(false);

		// Initialize all scene
		GameLogic.setPlayScene(new Scene(root));
		GameLogic.setTitleScene(new Scene(new TitleState(root, primaryStage)));

		// Show stage to the screen
		primaryStage.setScene(GameLogic.getTitleScene());
		primaryStage.setTitle("The Farmer And Ther Viper");
		primaryStage.getIcons().add(new Image("texture/icon.png"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() {
		// Stop background music
		GameLogic.stopBackgroundMusic();

		// Stop all thread
		GameLogic.gameStop(root);
	}
}