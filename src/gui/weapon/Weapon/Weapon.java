package gui.weapon.weapon;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import application.Main;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logic.GameLogic;
import logic.Movable;

public abstract class Weapon extends Pane implements Movable {

	private double xTranslation, yTranslation;
	private double xSpeed, ySpeed;
	protected double xShootPosition, yShootPosition;

	private boolean alreadyShoot = false;

	private boolean startThread;

	protected boolean shooting = false;

	private int fireRate;

	private String shootingFileName;

	public Weapon(int x, int y) {
		// Set weapon initial position
		setxTranslation(x);
		setyTranslation(y);
		setTranslateX(GameLogic.getPlayerPane().getxTranslation() + 32);
		setTranslateY(GameLogic.getPlayerPane().getyTranslation());
		
		// Set weapon initial flip
		setScaleX(1);

		// Set weapon size
		setPrefWidth(64);
		setPrefHeight(32);
		setMaxWidth(64);
		setMaxHeight(32);

		// Set default fire rate
		setFireRate(500);
		
		// Set default background color
		setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	@Override
	public void run() {
		setMovingThreadStart(true);

		Thread movingThread = new Thread(() -> {
			// Loop while player is moving and weapon is moving
			while (GameLogic.getPlayerPane().isMovingThreadStart() && isMovingThreadStart()) {
				// Update weapon position on the screen
				Platform.runLater(new Runnable() {
					public void run() {
						if (GameLogic.getPlayerPane().isPressedA()) {
							setScaleX(-1);
							setTranslateX(GameLogic.getPlayerPane().getxTranslation() - 32);
						} else if (GameLogic.getPlayerPane().isPressedD()) {
							setScaleX(1);
							setTranslateX(GameLogic.getPlayerPane().getxTranslation() + 32);
						}

						setTranslateY(GameLogic.getPlayerPane().getyTranslation());
					}
				});

				GameLogic.delay(10);
			}
		});
		movingThread.start();
	}

	public void startShoot() {
		setShooting(true);
	};

	public abstract void runShoot(StackPane root);

	public void endShoot() {
		setShooting(false);
	}

	public void setTexture(String imagePath) {
		Image texturesFile = new Image("texture/weapon/" + imagePath);
		BackgroundSize bgSize = new BackgroundSize(64, 32, false, false, false, false);
		BackgroundImage bgImg = new BackgroundImage(texturesFile, null, null, null, bgSize);
		BackgroundImage[] bgImgA = { bgImg };
		this.setBackground(new Background(bgImgA));
	}

	@Override
	public double getxTranslation() {
		return xTranslation;
	}

	@Override
	public void setxTranslation(double xTranslation) {
		this.xTranslation = xTranslation;
	}

	@Override
	public double getyTranslation() {
		return yTranslation;
	}

	@Override
	public void setyTranslation(double yTranslation) {
		this.yTranslation = yTranslation;
	}

	@Override
	public double getCurrentXPosition() {
		return 400 + getxTranslation();
	}

	@Override
	public double getCurrentYPosition() {
		return 400 + getyTranslation();
	}

	@Override
	public double getxSpeed() {
		return xSpeed;
	}

	@Override
	public void setxSpeed(double speed) {
		xSpeed = speed;
	}

	@Override
	public double getySpeed() {
		return ySpeed;
	}

	@Override
	public void setySpeed(double speed) {
		ySpeed = speed;
	}

	@Override
	public boolean isMovingThreadStart() {
		return startThread;
	}

	@Override
	public void setMovingThreadStart(boolean startThread) {
		this.startThread = startThread;
	}

	public void setShootPosition(MouseEvent event) {
		xShootPosition = event.getSceneX();
		yShootPosition = event.getSceneY();
	}

	public void initializeShootingSound(String path) {
		shootingFileName = "/music/weapon/" + path;
	}

	public void playShootingSound() {
		try {
			InputStream shootingFileStream =  Main.class.getResourceAsStream(shootingFileName);
			InputStream bufferedShootingFile = new BufferedInputStream(shootingFileStream);
			AudioInputStream shootingFile = AudioSystem.getAudioInputStream(bufferedShootingFile);
			Clip shootSoundPlayer = AudioSystem.getClip();
			shootSoundPlayer.open(shootingFile);
			shootSoundPlayer.start();
		} catch (LineUnavailableException e) {
			System.out.println("Cannot generate audio player");
		} catch (MalformedURLException e) {
			System.out.println("Invalid audio path");
		} catch (IOException e) {
			System.out.println("Error");
		} catch (UnsupportedAudioFileException e) {
			System.out.println("Unsuppoort audio file type");
		}
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public boolean isShooting() {
		return shooting;
	}

	public void setAlreadyShoot(boolean alreadyShoot) {
		this.alreadyShoot = alreadyShoot;
	}

	public boolean getAlreadyShoot() {
		return alreadyShoot;
	}

	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}

	public int getFireRate() {
		return fireRate;
	}
}
