package gui.weapon.bullet;

public class LaserGunBullet extends Bullet {

	public LaserGunBullet(double x, double y) {
		// Call super class constructor
		super(x, y);

		// Set bullet attribute
		setDamage(40);
		setPenetrate(true);

		// Adjust speed
		adjustSpeed(50, x, y);
		
		// Set bullet texture
		setTexture("laser.png");
	}
}
