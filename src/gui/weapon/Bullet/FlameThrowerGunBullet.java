package gui.weapon.bullet;

public class FlameThrowerGunBullet extends Bullet {

	public FlameThrowerGunBullet(double x, double y) {
		// Call super class constructor
		super(x, y);

		// Set bullet attribute
		setDamage(2);
		setPenetrate(true);

		// Adjust speed
		adjustSpeed(10, x, y);

		// Set bullet texture
		setTexture("fire.png");

		// Fire don't rotate
		setRotate(0);
	}
}
