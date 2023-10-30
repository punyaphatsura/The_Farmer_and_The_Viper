package gui.weapon.weapon;

import gui.weapon.bullet.Bullet;
import gui.weapon.bullet.RifleGunBullet;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import logic.GameLogic;

public class RifleGun extends Weapon {

	public RifleGun(int x, int y) {
		// Call super class constructor
		super(x, y);

		// Set weapon attribute
		setFireRate(300);
		setTexture("rifle.png");
		initializeShootingSound("rifle.wav");
	}

	@Override
	public void runShoot(StackPane root) {
		Thread shootingThread = new Thread(() -> {
			setAlreadyShoot(false);
			while (isShooting()) {

				if (!getAlreadyShoot()) {
					// Play shooting sound
					playShootingSound();

					// Add bullet to root pane
					Bullet bullet = new RifleGunBullet(this.xShootPosition, this.yShootPosition);

					Platform.runLater(() -> {
						root.getChildren().add(bullet);
					});

					bullet.run();

					// Add update bullet thread
					bullet.update(root);

					// Delay shooting
					setAlreadyShoot(true);
					GameLogic.delay(getFireRate());
					setAlreadyShoot(false);
				}
			}

		});

		shootingThread.start();
	}
}
