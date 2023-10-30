package gui.weapon.weapon;

import gui.weapon.bullet.Bullet;
import gui.weapon.bullet.FlameThrowerGunBullet;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import logic.GameLogic;

public class ElderflameGun extends Weapon {

	public ElderflameGun(int x, int y) {
		// Call super class constructor
		super(x, y);

		// Set weapon attribute
		setFireRate(150);
		setTexture("elderflame.png");
		initializeShootingSound("fire.wav");
	}

	@Override
	public void startShoot() {
		setShooting(true);
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
					Bullet bullet = new FlameThrowerGunBullet(this.xShootPosition, this.yShootPosition);
					bullet.setDamage(100);

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