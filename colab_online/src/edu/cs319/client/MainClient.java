package edu.cs319.client;

import javax.swing.JFrame;

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
		WindowLogIn logIn = new WindowLogIn();
		logIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logIn.setVisible(true);
	}

}
