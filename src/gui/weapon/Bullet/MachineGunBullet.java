package gui.weapon.bullet;

public class MachineGunBullet extends Bullet {

	public MachineGunBullet(double x, double y) {
		// Call super class constructor
		super(x, y);

		// Set bullet attribute
		setDamage(5);
		setPenetrate(false);

		// Set bullet texture
		setTexture("machinegun.png");
	}

}
