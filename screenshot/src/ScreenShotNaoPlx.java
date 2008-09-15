import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenShotNaoPlx {

	public static void main(String[] args) throws HeadlessException, IOException,
			AWTException {
		ImageIO.write(getScreenShot(), "png", new File("C:/hi.png"));
	}

	private static BufferedImage getScreenShot() throws HeadlessException, AWTException {
		return getScreenShot(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	private static BufferedImage getScreenShot(Rectangle bounds) throws AWTException {
		Robot c3po = new Robot();
		return c3po.createScreenCapture(bounds);
	}
}
