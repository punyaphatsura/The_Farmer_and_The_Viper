package gui.weapon.bullet;

public class PistolGunBullet extends Bullet {

	public PistolGunBullet(double x, double y) {
		// Call super class constructor
		super(x, y);

		// Set bullet attribute
		setDamage(20);
		setPenetrate(false);

		// Set bullet texture
		setTexture("pistol.png");
	}
}
