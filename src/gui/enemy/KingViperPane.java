package gui.enemy;

import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.paint.Color;
import logic.GameLogic;

public class KingViperPane extends EnemyPane {

	private static final Image TEXTURES_FILE = new Image("texture/enemy/kingviper.png");

	private static Map<Integer, Image> walkingTexturesMap;
	private static Map<Integer, Image> attackTexturesMap;
	private static Map<Integer, Image> deathTexturesMap;

	// Constructor method
	public KingViperPane(int x, int y) {
		// Call super class constructor
		super(x, y);
		
		// Initialize texture maps
		walkingTexturesMap = new HashMap<>();
		attackTexturesMap = new HashMap<>();
		deathTexturesMap = new HashMap<>();

		// Initialize enemy attribute
		setStartHp(10000);
		setCurrentHp(getStartHp());
		setBite(90);
		setScore(2000);

		// Texture Loading
		PixelReader reader = TEXTURES_FILE.getPixelReader();
		int walkingID = 0;
		for (int col = 0; col < 5; col++) {
			WritableImage newImage = new WritableImage(reader, col * 160, 0, 160, 160);
			walkingTexturesMap.put(walkingID, newImage);
			walkingID++;
		}
		int attackID = 0;
		for (int col = 0; col < 3; col++) {
			WritableImage newImage = new WritableImage(reader, col * 160, 160, 160, 160);
			attackTexturesMap.put(attackID, newImage);
			attackID++;
		}
		int deathID = 0;
		for (int col = 0; col < 4; col++) {
			WritableImage newImage = new WritableImage(reader, col * 160, 320, 160, 160);
			deathTexturesMap.put(deathID, newImage);
			deathID++;
		}

		// Set enemy spawn position
		setxTranslation(x);
		setyTranslation(y);

		// Set enemy speed
		setxStartSpeed(0.25);
		setyStartSpeed(0.25);
		setxSpeed(getxStartSpeed());
		setySpeed(getyStartSpeed());

		// Initialize HP bar
		initialHpBar(15, 150, 5, -15);

		// Set enemy size
		setPrefWidth(160);
		setPrefHeight(160);
		setMaxWidth(160);
		setMaxHeight(160);

		// Set default background image
		setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private int playAnimation(int animationState, int id, Map<Integer, Image> texturesMap, int animationFrame) {
		// Reset id when transition between state
		if (state != animationState) {
			id = 0;
			state = animationState;
		}

		int textureID = id % animationFrame;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				BackgroundSize bgSize = new BackgroundSize(160, 160, false, false, false, false);
				BackgroundImage bgImg = new BackgroundImage(texturesMap.get(textureID), null, null, null, bgSize);
				BackgroundImage[] bgImgA = { bgImg };
				setBackground(new Background(bgImgA));
			}
		});

		return id;
	}

	@Override
	public void animate() {
		Thread thread = new Thread(() -> {
			int id = 0;
			setMovingThreadStart(true);
			while (isMovingThreadStart()) {
				// Flip enemy to the right direction
				flipAnimation();

				if (!GameLogic.canEnemyBite(this) && !isDead()) {
					// Walking to the player animation
					id = playAnimation(WALK_STATE, id, walkingTexturesMap, 5);
				} else if (GameLogic.canEnemyBite(this) && !isDead()) {
					// Attack the player animation
					id = playAnimation(ATTACK_STATE, id, attackTexturesMap, 3);
				} else if (isDead()) {
					// Death animation
					id = playAnimation(DEAD_STATE, id, deathTexturesMap, 4);

					// If animation end re-spawn the enemy
					if (id % 6 == 5) {
						respawn = true;
						break;
					}
				}

				id++;

				GameLogic.delay(60);
			}
		});
		thread.start();
	}
}
