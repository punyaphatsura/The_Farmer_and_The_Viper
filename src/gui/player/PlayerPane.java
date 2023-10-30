package gui.player;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Animation;
import logic.Controllable;
import logic.Deadable;
import logic.GameLogic;
import logic.Movable;

public class PlayerPane extends Pane implements Deadable, Movable, Controllable, Animation {
	private static final Image texturesFile = new Image("texture/player/player.png");
	private static Map<Integer, Image> texturesMap;

	private double xTranslation, yTranslation;
	private double xSpeed, ySpeed;

	private boolean pressedW, pressedA, pressedS, pressedD;

	private int currentHp, startHp;

	private Rectangle statusStartHealthBar, statusCurrentHealthBar;

	private boolean startThread;

	private boolean immune;

	public PlayerPane(int x, int y) {
		// Texture Loading
		texturesMap = new HashMap<>();
		int id = 0;
		for (int col = 0; col < 4; col++) {
			PixelReader reader = texturesFile.getPixelReader();
			WritableImage newImage = new WritableImage(reader, col * 32, 0, 32, 32);
			texturesMap.put(id, newImage);
			id++;
		}

		// Set player spawn position
		setxTranslation(x);
		setyTranslation(y);

		// Set player speed
		setxSpeed(2);
		setySpeed(2);

		// Set default control state
		setPressedW(false);
		setPressedA(false);
		setPressedS(false);
		setPressedD(false);

		// Set player attribute
		setImmune(false);
		setStartHp(100);
		setCurrentHp(getStartHp());

		// Initialize HP bar
		initialHpBar(10, 50, 0, -5);

		// Set player size
		setPrefWidth(50);
		setPrefHeight(50);
		setMaxWidth(50);
		setMaxHeight(50);

		// Set default background image
		setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	@Override
	public void animate() {
		Thread thread = new Thread(() -> {
			int id = 0;
			setMovingThreadStart(true);
			while (isMovingThreadStart()) {
				int textureID = id % 4;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						BackgroundSize bgSize = new BackgroundSize(50, 50, false, false, false, false);
						BackgroundImage bgImg = new BackgroundImage(texturesMap.get(textureID), null, null, null,
								bgSize);
						BackgroundImage[] bgImgA = { bgImg };
						setBackground(new Background(bgImgA));
					}
				});

				id++;

				GameLogic.delay(100);
			}
		});
		thread.start();
		thread.setName("thread");
	}

	@Override
	public void run() {
		Thread movingThread = new Thread(() -> {
			setMovingThreadStart(true);
			while (isMovingThreadStart()) {
				// Calculate position by speed
				if (isPressedW()) {
					setyTranslation(getyTranslation() - getySpeed());
				}
				if (isPressedA()) {
					setxTranslation(getxTranslation() - getxSpeed());
				}
				if (isPressedS()) {
					setyTranslation(getyTranslation() + getySpeed());
				}
				if (isPressedD()) {
					setxTranslation(getxTranslation() + getxSpeed());
				}

				// Calculate speed via control key
				if ((isPressedW() && !isPressedA() && !isPressedS() && !isPressedD())
						|| (!isPressedW() && isPressedA() && !isPressedS() && !isPressedD())
						|| (!isPressedW() && !isPressedA() && isPressedS() && !isPressedD())
						|| (!isPressedW() && !isPressedA() && !isPressedS() && isPressedD())) {
					setySpeed(2);
					setxSpeed(2);
				} else {
					setxSpeed((float) 1.414);
					setySpeed((float) 1.414);
				}

				// Update player position on the screen
				Platform.runLater(new Runnable() {
					public void run() {
						setTranslateX(getxTranslation());
						setTranslateY(getyTranslation());
					}

				});

				GameLogic.delay(10);
			}
		});
		movingThread.start();
		movingThread.setName("movingPlayerThread");
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
		ySpeed = speed;
	}

	@Override
	public double getySpeed() {
		return ySpeed;
	}

	@Override
	public void setySpeed(double speed) {
		xSpeed = speed;
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
	public void setCurrentHp(int currentHp) {
		this.currentHp = Math.min(startHp, Math.max(0, currentHp));
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
	public void setStartHp(int startHp) {
		this.startHp = Math.max(startHp, 0);
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
	public void receiveDamage(Object damage) {
		setCurrentHp(getCurrentHp() - ((int) damage));
		Platform.runLater(() -> {
			statusCurrentHealthBar.setWidth(getWidthHpBar() * getHealthRatio());
		});
	}

	public boolean isImmune() {
		return immune;
	}

	public void setImmune(boolean immune) {
		this.immune = immune;
	}

	public void checkHeal() {
		// Heal every 5 kills stack(reset kill stack when damage taken)
		if (GameLogic.getViperKillStack() % 5 == 0 && GameLogic.getViperKillStack() != 0) {
			setCurrentHp(getCurrentHp() + 10);
			statusCurrentHealthBar.setWidth(50 * getHealthRatio());
		}
	}

	@Override
	public void keyPressHandler(KeyEvent key) {
		if (key.getCode() == KeyCode.W) {
			setPressedW(true);
		}

		if (key.getCode() == KeyCode.A) {
			setPressedA(true);
			setScaleX(-1);
		}

		if (key.getCode() == KeyCode.S) {
			setPressedS(true);
		}

		if (key.getCode() == KeyCode.D) {
			setPressedD(true);
			setScaleX(1);
		}
	}

	@Override
	public void keyReleasedHandler(KeyEvent key) {
		if (key.getCode() == KeyCode.W) {
			setPressedW(false);
		}

		if (key.getCode() == KeyCode.A) {
			setPressedA(false);
		}

		if (key.getCode() == KeyCode.S) {
			setPressedS(false);
		}

		if (key.getCode() == KeyCode.D) {
			setPressedD(false);
		}
	}

	@Override
	public boolean isPressedW() {
		return pressedW;
	}

	@Override
	public void setPressedW(boolean pressedW) {
		this.pressedW = pressedW;
	}

	@Override
	public boolean isPressedA() {
		return pressedA;
	}

	@Override
	public void setPressedA(boolean pressedA) {
		this.pressedA = pressedA;
	}

	@Override
	public boolean isPressedS() {
		return pressedS;
	}

	@Override

	public void setPressedS(boolean pressedS) {
		this.pressedS = pressedS;
	}

	@Override
	public boolean isPressedD() {
		return pressedD;
	}

	@Override
	public void setPressedD(boolean pressedD) {
		this.pressedD = pressedD;
	}
}
