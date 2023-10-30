package gui.weapon.bullet;

import gui.enemy.EnemyPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
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

public class Bullet extends Pane implements Movable {
	private double xTranslation, yTranslation;
	private double xSpeed, ySpeed;
	private int damage;
	private boolean isCollision;
	private boolean isPenetrate;
	private boolean startThread;

	public Bullet(double x, double y) {
		// Set bullet size
		setPrefWidth(32);
		setPrefHeight(32);
		setMaxWidth(32);
		setMaxHeight(32);

		// Set default background image
		setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

		// Initialize bullet attribute
		setDamage(20);
		setPenetrate(false);

		// Set bullet spawn position
		setxTranslation(GameLogic.getPlayerPane().getxTranslation());
		setyTranslation(GameLogic.getPlayerPane().getyTranslation());
		setTranslateX(getxTranslation());
		setTranslateY(getyTranslation());

		// Calculate and set speed of bullet
		double xLength = GameLogic.getPlayerPane().getCurrentXPosition() - x;
		double yLength = GameLogic.getPlayerPane().getCurrentYPosition() - y;
		double xFactor = (xLength) / Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		double yFactor = (yLength) / Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		setxSpeed((float) (10 * xFactor));
		setySpeed((float) (10 * yFactor));

		// Set default collision to false
		setCollision(false);

		// Calculate bullet rotation
		calcualteRotation(xFactor, yFactor, xLength, yLength);
	}

	@Override
	public void run() {
		setMovingThreadStart(true);

		Thread movingThread = new Thread(() -> {
			// Loop while bullet is not out of map, not collision and bullet is moving
			while (!outOfMap() && !isCollision() && isMovingThreadStart()) {
				// Calculate bullet position via speed
				setxTranslation(getxTranslation() - getxSpeed());
				setyTranslation(getyTranslation() - getySpeed());

				// Update bullet position in screen
				Platform.runLater(new Runnable() {
					public void run() {
						setTranslateX(getxTranslation());
						setTranslateY(getyTranslation());
					}
				});

				GameLogic.delay(20);
			}
		});
		movingThread.start();
		movingThread.setName("movingThread");
	}

	public void update(StackPane root) {
		Thread updateBullet = new Thread(() -> {
			boolean endThread = false;
	
			// Loop while no end signal, game started and bullet is moving
			while (!endThread && GameLogic.isStartGame() && isMovingThreadStart()) {
				// Check bullet out of map
				if (outOfMap()) {
					Platform.runLater(() -> {
						root.getChildren().remove(this);
					});
					break;
				}
	
				// Check bullet collapse with enemy
				for (Object enemy : GameLogic.getEnemyPane().toArray()) {
					if (enemy != null && GameLogic.isCollision((EnemyPane) enemy, this) && !isPenetrate()) {
						Platform.runLater(() -> {
							root.getChildren().remove(this);
							setCollision(true);
						});
						endThread = true;
						break;
					}
				}
	
				// Delay update bullet
				GameLogic.delay(20);
			}
		});
		updateBullet.start();
		updateBullet.setName("updateBullet");
	}

	private void calcualteRotation(double xFactor, double yFactor, double xLength, double yLength) {
		if (yFactor > 0.25) {
			if (xFactor < 0.9 || xFactor > -0.9) {
				setRotate(-1.0d * Math.toDegrees(xLength / Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2))));
			} else {
				setRotate(90);
			}
		} else if (yFactor < -0.25) {
			if (xFactor < 0.9 || xFactor > -0.9) {
				setRotate(
						1.0d * Math.toDegrees(xLength / Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2))) + 180);
			} else {
				setRotate(-90);
			}
		} else {
			if (xFactor > 0) {
				setRotate(-90);
			} else {
				setRotate(90);
			}
		}
	}
	
	public void adjustSpeed(int baseSpeed, double x, double y) {
		// Adjust speed
		double xLength = GameLogic.getPlayerPane().getCurrentXPosition() - x;
		double yLength = GameLogic.getPlayerPane().getCurrentYPosition() - y;
		double xFactor = (xLength) / Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		double yFactor = (yLength) / Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		setxSpeed((float) (baseSpeed * xFactor));
		setySpeed((float) (baseSpeed * yFactor));
	}

	public void setTexture(String imagePath) {
		Image texturesFile = new Image("texture/bullet/" + imagePath);
		BackgroundSize bgSize = new BackgroundSize(32, 32, false, false, false, false);
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
		this.xSpeed = speed;
	}

	@Override
	public double getySpeed() {
		return ySpeed;
	}

	@Override
	public void setySpeed(double speed) {
		this.ySpeed = speed;
	}

	@Override
	public boolean isMovingThreadStart() {
		return startThread;
	}

	@Override
	public void setMovingThreadStart(boolean startThread) {
		this.startThread = startThread;
	}

	public boolean outOfMap() {
		if (getCurrentXPosition() >= 0 && getCurrentXPosition() <= 800 && getCurrentYPosition() >= 0
				&& getCurrentYPosition() <= 800)
			return false;
		return true;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = Math.max(0, damage);
	}

	public boolean isCollision() {
		return isCollision;
	}

	public void setCollision(boolean isCollision) {
		this.isCollision = isCollision;
	}

	public boolean isPenetrate() {
		return isPenetrate;
	}

	public void setPenetrate(boolean isPenetrate) {
		this.isPenetrate = isPenetrate;
	}
}
