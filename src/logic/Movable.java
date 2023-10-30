package logic;

public interface Movable {
	public void run();

	public double getxTranslation();

	public void setxTranslation(double xTranslation);

	public double getyTranslation();

	public void setyTranslation(double yTranslation);

	public double getCurrentXPosition();

	public double getCurrentYPosition();

	public double getxSpeed();

	public void setxSpeed(double speed);

	public double getySpeed();

	public void setySpeed(double speed);

	boolean isMovingThreadStart();

	void setMovingThreadStart(boolean startThread);
}
