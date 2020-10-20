package application;

public class Timer implements Runnable {

	private static Thread killThread;

	public Timer(Thread input) {
		killThread = input;
	}

	private static void time() {

		PointAwardingSystem points = new PointAwardingSystem();

		final int mili = 500;
		final int sec = 60;
		final int oneMinute = (mili * sec);

		System.out.println("Start 3 minute timer");
		sleep(oneMinute * 2);

		System.out.println("One minute remaining...");
		sleep(oneMinute);

		System.out.println("Time's up!");

		killThread.stop();

		points.printFinalTotal();

	}

	private static void sleep(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		time();
	}

}
