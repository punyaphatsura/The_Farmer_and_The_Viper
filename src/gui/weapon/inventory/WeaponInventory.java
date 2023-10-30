package gui.weapon.inventory;

import java.util.ArrayList;
import java.util.Arrays;

import gui.weapon.weapon.FlameThrowerGun;
import gui.weapon.weapon.LaserGun;
import gui.weapon.weapon.MachineGun;
import gui.weapon.weapon.PistolGun;
import gui.weapon.weapon.RifleGun;
import gui.weapon.weapon.ShotGun;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import logic.GameLogic;
import logic.GameTimer;

public class WeaponInventory extends GridPane {
	private final ArrayList<String> GUN_TEXTURES = new ArrayList<String>(
			Arrays.asList("pistol", "rifle", "shotgun", "machinegun", "lasergun", "flamethrower"));

	private ArrayList<WeaponInventorySquare> weaponInventorySquares;

	public WeaponInventory() {
		// Initialize weaponInventorySquares
		weaponInventorySquares = new ArrayList<>();
		
		// Add weaponInventorySquare to grid
		for (int i = 0; i < GUN_TEXTURES.size(); i++) {
			WeaponInventorySquare weaponInventorySquare = new WeaponInventorySquare(i + 1, GUN_TEXTURES.get(i));
			weaponInventorySquare.setOpacity(0.5);
			add(weaponInventorySquare, i, 0);
			weaponInventorySquares.add(weaponInventorySquare);
		}

		// Set GridPane position
		setTranslateY(720);
		setTranslateX(203);

		// Set Gap between square
		setHgap(2);
	}

	public void update() {
		Thread updateInventoryThread = new Thread(() -> {
			while (GameLogic.isStartGame()) {
				for (WeaponInventorySquare weaponInventorySquare : weaponInventorySquares) {
					if (weaponInventorySquare.getName().equals("pistol") && GameTimer.getMin() >= 0) {
						weaponInventorySquare.setOpacity(1);
					} else if (weaponInventorySquare.getName().equals("rifle") && GameTimer.getMin() >= 1) {
						weaponInventorySquare.setOpacity(1);
					} else if (weaponInventorySquare.getName().equals("shotgun") && GameTimer.getMin() >= 1) {
						weaponInventorySquare.setOpacity(1);
					} else if (weaponInventorySquare.getName().equals("machinegun") && GameTimer.getMin() >= 3) {
						weaponInventorySquare.setOpacity(1);
					} else if (weaponInventorySquare.getName().equals("lasergun") && GameTimer.getMin() >= 3) {
						weaponInventorySquare.setOpacity(1);
					} else if (weaponInventorySquare.getName().equals("flamethrower") && GameTimer.getMin() >= 3) {
						weaponInventorySquare.setOpacity(1);
					} else {
						weaponInventorySquare.setOpacity(0.5);
					}

					if (weaponInventorySquare.getName() == "pistol" && GameLogic.getWeapon() instanceof PistolGun) {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					} else if (weaponInventorySquare.getName() == "rifle"
							&& GameLogic.getWeapon() instanceof RifleGun) {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					} else if (weaponInventorySquare.getName() == "shotgun"
							&& GameLogic.getWeapon() instanceof ShotGun) {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					} else if (weaponInventorySquare.getName() == "machinegun"
							&& GameLogic.getWeapon() instanceof MachineGun) {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					} else if (weaponInventorySquare.getName() == "lasergun"
							&& GameLogic.getWeapon() instanceof LaserGun) {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					} else if (weaponInventorySquare.getName() == "flamethrower"
							&& GameLogic.getWeapon() instanceof FlameThrowerGun) {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					} else {
						weaponInventorySquare.setBorder(new Border(new BorderStroke(Color.YELLOW,
								BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
					}
				}

				GameLogic.delay(100);
			}

		});

		updateInventoryThread.start();
	}
}
