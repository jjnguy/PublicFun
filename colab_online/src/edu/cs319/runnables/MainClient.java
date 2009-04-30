package edu.cs319.runnables;

import javax.swing.JFrame;

import edu.cs319.client.WindowClient;

/**
 * Runs the WindowClient GUI for CoLab.
 **/
public class MainClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {

		WindowClient client = new WindowClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
//		WindowClient client2 = new WindowClient();
//		client2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		client2.setVisible(true);
	}

}
