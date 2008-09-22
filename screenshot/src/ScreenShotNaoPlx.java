import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class ScreenShotNaoPlx {

	public static void main(String[] args) throws HeadlessException, IOException,
			AWTException {
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TakeSS(), 0, 2000);
	}

	public static BufferedImage getScreenShot() throws HeadlessException, AWTException {
		return getScreenShot(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	private static BufferedImage getScreenShot(Rectangle bounds) throws AWTException {
		Robot c3po = new Robot();
		return c3po.createScreenCapture(bounds);
	}
}

class TakeSS extends TimerTask {
	int i = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		File f = new File("C:/"
				+ Util.getTimeStringFromMiliseconds(System.currentTimeMillis()));
		System.out.println(f.mkdirs());
		
		try {
			ImageIO.write(ScreenShotNaoPlx.getScreenShot(), "png", new File("C:/"
					+ Util.getTimeStringFromMiliseconds(System.currentTimeMillis()) + "/" + "img" + i
					+ ".png"));
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i++;
	}

}