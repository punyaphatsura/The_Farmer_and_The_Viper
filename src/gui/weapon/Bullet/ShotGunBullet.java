package gui.weapon.bullet;

public class ShotGunBullet extends Bullet {

	public ShotGunBullet(double x, double y) {
		// Call super class constructor
		super(x, y);

		// Set bullet attribute
		setDamage(20);
		setPenetrate(false);

		// Set bullet texture
		setTexture("shotgun.png");
	}

}
