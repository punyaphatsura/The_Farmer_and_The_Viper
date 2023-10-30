package logic;

import javafx.scene.input.KeyEvent;

public interface Controllable {
	public void keyPressHandler(KeyEvent key);

	public void keyReleasedHandler(KeyEvent key);

	public boolean isPressedW();

	public void setPressedW(boolean pressedW);

	public boolean isPressedA();

	public void setPressedA(boolean pressedA);

	public boolean isPressedS();

	public void setPressedS(boolean pressedS);

	public boolean isPressedD();

	public void setPressedD(boolean pressedD);
}
