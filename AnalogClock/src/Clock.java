import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Clock implements Runnable {

	private int hour;
	private int minute;
	private int second;
	private int milis;
	
	public Clock() {
		updateTime();
	}
	
	public final void updateTime(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		hour= c.get(11);
		minute = c.get(12);
		second = c.get(13);
		milis = c.get(Calendar.MILLISECOND);
	}
	
	public String getTime(){
		return getTime("hh:mm:ss");
	}
	public String getTime(String format){
		String ret = format;
		ret = ret.replaceAll("hh", hour + "");
		ret = ret.replaceAll("mm", minute + "");
		ret = ret.replaceAll("ss", second + "");
		return ret;
	}
	public int getHours(){
		return getHours(false);
	}
	public int getHours(boolean twenty4){
		if (twenty4)return hour;
		else return hour % 12;
	}
	public int getMinutes(){
		return minute;
	}
	public int getSeconds(){
		return second;
	}
	public int getMilis(){
		return milis;
	}
	public long getUnixTime(){
		return -1;
	}
	public void run(){
		Timer updateThread = new Timer();
		updateThread.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateTime();
			}
		}, 0L, 100L);
	}
}
