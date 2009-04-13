package edu.cs319.runnables;

import javax.swing.JFrame;

import edu.cs319.client.WindowClient;

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

	}

}
