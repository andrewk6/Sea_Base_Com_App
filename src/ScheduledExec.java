import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExec {
	private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private Runnable myTask;

	private int hour, min, sec;
	public ScheduledExec(Runnable myTask, int hour, int min, int sec) {
		this.myTask = myTask;
		this.hour = hour;
		this.min = min;
		this.sec = sec;

	}
	
	public void stopExecution() {
		executorService.shutdownNow();
	}

	public void startExecution() {
		long delay = computeNextDelay();
		System.out.println(delay);
		executorService.schedule(myTask, delay, TimeUnit.SECONDS);
	}

	private long computeNextDelay() {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow.withHour(hour).withMinute(min).withSecond(sec);
		if (zonedNow.compareTo(zonedNextTarget) > 0)
			zonedNextTarget = zonedNextTarget.plusDays(1);

		Duration duration = Duration.between(zonedNow, zonedNextTarget);
		return duration.getSeconds();
	}
	
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
		stopExecution();
		startExecution();
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
		stopExecution();
		startExecution();
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
		stopExecution();
		startExecution();
	}

	public void setMyTask(Runnable myTask) {
		this.myTask = myTask;
	}
}