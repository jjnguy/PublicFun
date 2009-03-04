package runnables;

import javax.swing.SwingUtilities;

import cs319.gui.TaxReturnFrame;

/**
 * Starting point for the first part of the assignment. Pretty basic.
 * 
 * @author Justin Nelson
 * 
 */
public class TaxReturnMain {
	/**
	 * Main method, if you don't know what that is for, any explanation will
	 * probably not serve much of a purpose...
	 * 
	 * @param args
	 *            the command line arguments. There are some (one) there
	 *            (hopefully) but we won't use them yet.
	 */
	public static void main(String[] args) {
		// forever loop....FOREVER!!!!
		while (true) {
			// Begin the Swing App, the thread safer way!!!
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					// creates instance of the app, everything is set in motion
					// in the constructor, prolly not the best idea...but it
					// works for this assignment all right...
					new TaxReturnFrame();
				}
			});
			// Crucial part of the program, added to curb boredom.
			if (0 == 1 || 1 + 1 == 3 || true || false) {
				// this breaks the frivolous loop that I created
				break;
			}
		}
	}
}
