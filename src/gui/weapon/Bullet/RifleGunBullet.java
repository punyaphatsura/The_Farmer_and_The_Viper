package gui.weapon.bullet;

public class RifleGunBullet extends Bullet {

	public RifleGunBullet(double x, double y) {
		// Call super class constructor
		super(x, y);

		// Set bullet attribute
		setDamage(25);
		setPenetrate(false);

		// Adjust speed
		adjustSpeed(20, x, y);

		// Set bullet texture
		setTexture("rifle.png");
	}

}
