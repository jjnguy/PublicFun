package guiElements;

import javax.swing.JFrame;

/**
 * @author Justin
 *
 */
public class PrimeFrame extends JFrame  {

	/**
	 * 
	 */
	PrimePanel mainPanel;

	/**
	 * 
	 */
	public PrimeFrame() {
		super("Prime Tester by Justin Nelson v1.2");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainPanel = new PrimePanel();
		add(mainPanel);

		setSize(600, 73);
		setResizable(false);

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrimeFrame hi = new PrimeFrame();
		hi.setVisible(true);
	}

}
