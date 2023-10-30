package gui.enemy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import gui.weapon.bullet.Bullet;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Deadable;
import logic.GameLogic;
import logic.Movable;
import logic.Animation;

public class EnemyPane extends Pane implements Movable, Deadable, Animation {
	private static final Image[] TEXTURES_FILES = { new Image("texture/enemy/viper01.png"),
			new Image("texture/enemy/viper02.png"), new Image("texture/enemy/viper03.png") };

	private static ArrayList<Map<Integer, Image>> walkingTexturesMaps;
	private static ArrayList<Map<Integer, Image>> attackTexturesMaps;
	private static ArrayList<Map<Integer, Image>> deathTexturesMaps;

	private int textureNumber;

	private double xTranslation, yTranslation;
	private double xSpeed, ySpeed, xStartSpeed, yStartSpeed;

	private int score;

	private int startHp, currentHp, bite;

	private boolean startThread;

	private Rectangle statusStartHealthBar, statusCurrentHealthBar;

	protected static final int WALK_STATE = 0;
	protected static final int ATTACK_STATE = 1;
	protected static final int DEAD_STATE = 2;

	protected int state; // 0: chasing ,1: attack ,2: death

	protected boolean respawn = false;

	private boolean immune = false;

	// Constructor method
	public EnemyPane(int x, int y) {
		// Initialize texture enemy
		walkingTexturesMaps = new ArrayList<Map<Integer, Image>>(
				Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>()));
		attackTexturesMaps = new ArrayList<Map<Integer, Image>>(
				Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>()));
		deathTexturesMaps = new ArrayList<Map<Integer, Image>>(
				Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>()));

		// Random enemy texture
		Random rand = new Random();
		textureNumber = rand.nextInt(3);

		// Initialize enemy attribute
		setStartHp(50);
		setCurrentHp(getStartHp());
		setBite(10);
		setScore(10);

		// Texture Loading
		for (int id = 0; id < 3; id++) {
			PixelReader reader = TEXTURES_FILES[id].getPixelReader();
			int walkingID = 0;
			for (int col = 0; col < 8; col++) {
				WritableImage newImage = new WritableImage(reader, col * 32, 32, 32, 32);
				walkingTexturesMaps.get(id).put(walkingID, newImage);
				walkingID++;
			}
			int attackID = 0;
			for (int col = 0; col < 6; col++) {
				WritableImage newImage = new WritableImage(reader, col * 32, 64, 32, 32);
				attackTexturesMaps.get(id).put(attackID, newImage);
				attackID++;
			}
			int deathID = 0;
			for (int col = 0; col < 6; col++) {
				WritableImage newImage = new WritableImage(reader, col * 32, 128, 32, 32);
				deathTexturesMaps.get(id).put(deathID, newImage);
				deathID++;
			}
		}

		// Set enemy spawn position
		setxTranslation(x);
		setyTranslation(y);

		// Set enemy speed
		setxStartSpeed(0.8);
		setyStartSpeed(0.8);
		setxSpeed(getxStartSpeed());
		setySpeed(getyStartSpeed());

		// Initialize HP bar
		initialHpBar(5, 25, 5, -5);

		// Set enemy size
		setPrefWidth(32);
		setPrefHeight(32);
		setMaxWidth(32);
		setMaxHeight(32);

		// Set default background color
		setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	protected void flipAnimation() {
		if (getxTranslation() >= GameLogic.getPlayerPane().getxTranslation()) {
			// Flip snake to right
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					setScaleX(-1);
				}
			});
		} else if (getxTranslation() < GameLogic.getPlayerPane().getxTranslation()) {
			// Flip snake to left
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					setScaleX(1);
				}
			});
		}
	}

	private int playAnimation(int animationState, int id, ArrayList<Map<Integer, Image>> texturesMaps,
			int animationFrame) {
		// Reset id when transition between state
		if (state != animationState) {
			id = 0;
			state = animationState;
		}

		int textureID = id % animationFrame;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				BackgroundSize bgSize = new BackgroundSize(32, 32, false, false, false, false);
				BackgroundImage bgImg = new BackgroundImage(texturesMaps.get(textureNumber).get(textureID), null, null,
						null, bgSize);
				BackgroundImage[] bgImgA = { bgImg };
				setBackground(new Background(bgImgA));
			}
		});

		return id;
	}

	public void animate() {
		Thread animate = new Thread(() -> {
			int id = 0;

			// While enemy is moving
			while (isMovingThreadStart()) {
				// Flip enemy to the right direction
				flipAnimation();

				if (!GameLogic.canEnemyBite(this) && !isDead()) {
					// Walking to the player animation
					id = playAnimation(WALK_STATE, id, walkingTexturesMaps, 8);
				} else if (GameLogic.canEnemyBite(this) && !isDead()) {
					// Attack the player animation
					id = playAnimation(ATTACK_STATE, id, attackTexturesMaps, 6);
				} else if (isDead()) {
					// Death animation
					id = playAnimation(DEAD_STATE, id, deathTexturesMaps, 6);

					// If animation end respawn the enemy
					if (id % 6 == 5) {
						respawn = true;
						break;
					}
				}

				id++;

				GameLogic.delay(30);
			}
		});
		animate.start();
		animate.setName("animate");
	}

	@Override
	public void run() {
		setMovingThreadStart(true);
		Thread movingThread = new Thread(() -> {
			// Loop while
			while (isMovingThreadStart() && !isDead()) {

				// Calculate x and y speed for walking to player
				if (getxTranslation() != GameLogic.getPlayerPane().getxTranslation()
						&& getyTranslation() != GameLogic.getPlayerPane().getyTranslation()) {
					setxSpeed(getxStartSpeed());
					setySpeed(getyStartSpeed());
				} else {
					setxSpeed((float) 0.707 * getxStartSpeed());
					setySpeed((float) 0.707 * getyStartSpeed());
				}

				// Calculate enemy position via speed
				if (getxTranslation() < GameLogic.getPlayerPane().getxTranslation()) {
					setxTranslation(getxTranslation() + getxSpeed());
				} else if (getxTranslation() > GameLogic.getPlayerPane().getxTranslation()) {
					setxTranslation(getxTranslation() - getxSpeed());
				}

				if (getyTranslation() < GameLogic.getPlayerPane().getyTranslation()) {
					setyTranslation(getyTranslation() + getySpeed());
				} else if (getyTranslation() > GameLogic.getPlayerPane().getyTranslation()) {
					setyTranslation(getyTranslation() - getySpeed());
				}

				// Update enemy position in screen
				Platform.runLater(new Runnable() {
					public void run() {
						setTranslateX(getxTranslation());
						setTranslateY(getyTranslation());
					}
				});

				// Delay the moving
				GameLogic.delay(10);

				// Delay for play dead animation
				if (isDead()) {
					GameLogic.delay(500);
				}

			}
		});

		movingThread.start();
		movingThread.setName("movingViperThread");
	}

	@Override
	public double getxTranslation() {
		return xTranslation;
	}

	@Override
	public void setxTranslation(double xTranslation) {
		this.xTranslation = xTranslation < -304 ? -304 : xTranslation > 304 ? 304 : xTranslation;
	}

	@Override
	public double getyTranslation() {
		return yTranslation;
	}

	@Override
	public void setyTranslation(double yTranslation) {
		this.yTranslation = yTranslation < -304 ? -304 : yTranslation > 304 ? 304 : yTranslation;
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

	public double getxStartSpeed() {
		return xStartSpeed;
	}

	public void setxStartSpeed(double xStartSpeed) {
		this.xStartSpeed = xStartSpeed;
	}

	public double getyStartSpeed() {
		return yStartSpeed;
	}

	public void setyStartSpeed(double yStartSpeed) {
		this.yStartSpeed = yStartSpeed;
	}

	@Override
	public boolean isMovingThreadStart() {
		return startThread;
	}

	@Override
	public void setMovingThreadStart(boolean startThread) {
		this.startThread = startThread;
	}

	@Override
	public int getCurrentHp() {
		return currentHp;
	}

	@Override
	public void setCurrentHp(int hp) {
		currentHp = Math.max(0, hp);
	}

	@Override
	public boolean isDead() {
		return currentHp == 0;
	}

	@Override
	public int getStartHp() {
		return startHp;
	}

	@Override
	public void setStartHp(int hp) {
		startHp = Math.max(0, hp);
	}

	@Override
	public float getHealthRatio() {
		return (float) (getCurrentHp()) / (float) (getStartHp());
	}

	@Override
	public int getWidthHpBar() {
		return (int) statusStartHealthBar.getWidth();
	}

	@Override
	public void initialHpBar(int h, int w, int x, int y) {
		statusStartHealthBar = new Rectangle(w, h, Color.RED);
		statusStartHealthBar.setTranslateX(x);
		statusStartHealthBar.setTranslateY(y);
		getChildren().add(statusStartHealthBar);

		statusCurrentHealthBar = new Rectangle(w, h, Color.GREEN);
		statusCurrentHealthBar.setTranslateX(x);
		statusCurrentHealthBar.setTranslateY(y);
		getChildren().add(statusCurrentHealthBar);
	}

	@Override
	public void receiveDamage(Object bullet) {
		if (!isImmune()) {
			setCurrentHp(getCurrentHp() - ((Bullet) bullet).getDamage());

			Thread delayThread = new Thread(() -> {
				Platform.runLater(() -> {
					statusCurrentHealthBar.setWidth(getWidthHpBar() * getHealthRatio());
				});
				try {
					if (((Bullet) bullet).isPenetrate()) {
						setImmune(true);
						Thread.sleep(20);
						setImmune(false);
					}

				} catch (InterruptedException e) {
					System.out.println("Immune delay error");
				}
			});
			delayThread.start();
		}
	}

	@Override
	public boolean isImmune() {
		return immune;
	}

	@Override
	public void setImmune(boolean immune) {
		this.immune = immune;
	}

	public boolean needRespawn() {
		return respawn;
	}

	public int getBite() {
		return bite;
	}

	public void setBite(int bite) {
		this.bite = bite;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
