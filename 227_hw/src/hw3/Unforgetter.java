package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;

/**
 * An application for getting reminders of annually recurring events like birthdays, anniversaries, and tax days.
 */
public class Unforgetter {
	/**
	 * The application driver. An EventManager is created using the events from the file chosen by the user. Then, a TUI
	 * is run to let the user query the events.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		int selection = chooser.showOpenDialog(null);
		if (selection == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			f = new File("C:\\Documents and Settings\\jnelson\\workspace\\JacobsHomework\\src\\hw3\\dates1.txt");
			try {
				EventManager manager = new EventManager(f);
				UnforgetterTUI tui = new UnforgetterTUI(manager);
				tui.listen();
			} catch (FileNotFoundException e) {
				System.err.println("The file " + f.getName() + " couldn't be read.");
			}
		}
	}
}
