package genericComponents;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Util {

	private Util() {
	}

	public static String os() {
		return System.getProperty("os.name");
	}

	public static void moveToMiddle(JFrame comp) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		comp.setLocation((screenSize.width / 2) - comp.getWidth() / 2,
				(screenSize.height / 2) - comp.getHeight() / 2);
	}

	public static void moveToMiddle(JDialog comp) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		comp.setLocation((screenSize.width / 2) - comp.getWidth() / 2,
				(screenSize.height / 2) - comp.getHeight() / 2);
	}

	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
