package gui.weapon.weapon;

import gui.weapon.bullet.Bullet;
import gui.weapon.bullet.ShotGunBullet;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import logic.GameLogic;

public class ShotGun extends Weapon {

	public ShotGun(int x, int y) {
		// Call super class constructor
		super(x, y);

		// Set weapon attribute
		setFireRate(500);
		setTexture("shotgun.png");
		initializeShootingSound("shotgun.wav");
	}

	@Override
	public void runShoot(StackPane root) {
		Thread shootingThread = new Thread(() -> {
			setAlreadyShoot(false);
			while (isShooting()) {

				if (!getAlreadyShoot()) {
					// Play shooting sound
					playShootingSound();
					
          // Calculate bullet shooting position
					double[] calculatedPostion = calculateBulletPosition();
					
					// Add bullet to root pane
					Bullet bullet1 = new ShotGunBullet(GameLogic.getPlayerPane().getCurrentXPosition() + calculatedPostion[0], GameLogic.getPlayerPane().getCurrentYPosition() - calculatedPostion[3]);
					Bullet bullet2 = new ShotGunBullet(GameLogic.getPlayerPane().getCurrentXPosition() + calculatedPostion[1], GameLogic.getPlayerPane().getCurrentYPosition() - calculatedPostion[4]);
					Bullet bullet3 = new ShotGunBullet(GameLogic.getPlayerPane().getCurrentXPosition() + calculatedPostion[2], GameLogic.getPlayerPane().getCurrentYPosition() - calculatedPostion[5]);
					
					Platform.runLater(() -> {
						root.getChildren().addAll(bullet1, bullet2, bullet3);
					});

					// Bullet start moving
					bullet1.run();
					bullet2.run();
					bullet3.run();

					// Add update bullet thread
					bullet1.update(root);
					bullet2.update(root);
					bullet3.update(root);

					// Delay shooting
					setAlreadyShoot(true);
					GameLogic.delay(getFireRate());
					setAlreadyShoot(false);
				}
			}

		});

		shootingThread.start();
	}
	
	private double[] calculateBulletPosition() {
		// Calculate 3 bullet shooting position
		double xLength0 = this.xShootPosition - GameLogic.getPlayerPane().getCurrentXPosition();
		double yLength0 = GameLogic.getPlayerPane().getCurrentYPosition() - this.yShootPosition;
		
		double cosT0 = xLength0 / Math.sqrt(Math.pow(xLength0, 2) + Math.pow(yLength0, 2));
		
		double T0 = yLength0 >= 0 ? Math.acos(cosT0) : -1 * Math.acos(cosT0);
		
		double T1 = T0 + 0.1;
		double T2 = T0 - 0.1;
		
		double cosT1 = Math.cos(T1);
		double cosT2 = Math.cos(T2);
		
		double sinT1 = Math.sin(T1);
		double sinT2 = Math.sin(T2);
		
		double xLength1 = cosT1 * Math.sqrt(Math.pow(xLength0, 2) + Math.pow(yLength0, 2));
		double xLength2 = cosT2 * Math.sqrt(Math.pow(xLength0, 2) + Math.pow(yLength0, 2));
		
		double yLength1 = sinT1 * Math.sqrt(Math.pow(xLength0, 2) + Math.pow(yLength0, 2));
		double yLength2 = sinT2 * Math.sqrt(Math.pow(xLength0, 2) + Math.pow(yLength0, 2));
		
		double[] result = { xLength0, xLength1, xLength2, yLength0, yLength1, yLength2 };
		
		return result;
	}
}
