package genericComponents;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class TimeCounter extends CounterPanel {

	public static final int UP_MODE = 1;
	public static final int DOWN_MODE = -1;
	private int mode;
	private Timer timer;

	public TimeCounter(int mode) {
		super();
		this.mode = mode;
		timer = new Timer();
	}

	public void start() {
		timer.scheduleAtFixedRate(new IncrementTimer(), 1000, 1000);
	}

	public void stop() {
		timer.cancel();
		timer = new Timer();
	}

	public void reset() {
		setValue(0);
	}

	class IncrementTimer extends TimerTask {
		@Override
		public void run() {
			increment(mode);
		}
	}
}
