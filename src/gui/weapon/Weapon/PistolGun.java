package gui.weapon.weapon;

import gui.weapon.bullet.Bullet;
import gui.weapon.bullet.PistolGunBullet;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import logic.GameLogic;

public class PistolGun extends Weapon {

	public PistolGun(int x, int y) {
		// Call super class constructor
		super(x, y);

		// Set weapon attribute
		setFireRate(300);
		setTexture("pistol.png");
		initializeShootingSound("pistol.wav");
	}

	@Override
	public void runShoot(StackPane root) {
		Thread shootingThread = new Thread(() -> {
			setAlreadyShoot(false);
			while (isShooting()) {

				if (!getAlreadyShoot()) {
					// Play shooting sound
					playShootingSound();

					// Play shooting sound
					playShootingSound();

					// Add bullet to root pane
					Bullet bullet = new PistolGunBullet(this.xShootPosition, this.yShootPosition);

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
