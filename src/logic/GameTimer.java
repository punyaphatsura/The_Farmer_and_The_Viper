package logic;

public class GameTimer {

	private static int sec;
	private static int min;
	private static boolean timerControl;

	public GameTimer() {
		// Set timer to 0
		setMin(0);
		setSec(0);
		setTimerControl(false);
	}

	public GameTimer(int min, int sec) {
		// Set timer to given minute and second
		setMin(min);
		setSec(sec);
		setTimerControl(false);
	}

	public static void timer() {
		Thread timerThread = new Thread(() -> {
			// System.out.println("Time: " + String.format("%02d:%02d", GameTimer.getMin(), GameTimer.getSec()));
			GameLogic.delay(1000);
	
			setTimerControl(true);
	
			while (isTimerControl()) {
				setSec(getSec() + 1);
	
				if (getSec() == 60) {
					setSec(0);
					setMin(getMin() + 1);
				}
	
				// System.out.println("Time: " + String.format("%02d:%02d", GameTimer.getMin(), GameTimer.getSec()));
				
				GameLogic.delay(1000);
			}
		});
		
		timerThread.start();
	}

	public String toString() {
		return String.format("%02d:%02d", GameTimer.getMin(), GameTimer.getSec());
	}

	public static int getSec() {
		return sec;
	}

	public static void setSec(int sec) {
		GameTimer.sec = sec;
	}

	public static int getMin() {
		return min;
	}

	public static void setMin(int min) {
		GameTimer.min = min;
	}

	public static boolean isTimerControl() {
		return timerControl;
	}

	public static void setTimerControl(boolean startThread) {
		timerControl = startThread;
	}
}
