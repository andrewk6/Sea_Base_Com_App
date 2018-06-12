public class Runner{
	public static void main(String[]args) {
		ScheduledExec test = new ScheduledExec(new Runnable() {
			public void run() {
				System.out.println("Hello");
			}
		}, 20, 11, 0);
		test.startExecution();
	}
}