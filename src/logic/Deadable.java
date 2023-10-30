package logic;

public interface Deadable {
	public int getCurrentHp();

	public void setCurrentHp(int hp);

	public boolean isDead();

	public int getStartHp();

	public void setStartHp(int hp);

	public float getHealthRatio();
	
	public int getWidthHpBar();

	void initialHpBar(int h, int w, int x, int y);

	public void receiveDamage(Object o);

	boolean isImmune();

	void setImmune(boolean immune);
}
