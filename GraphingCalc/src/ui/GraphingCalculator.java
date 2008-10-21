package ui;

import plotter.Plotter;
import ui.gui.MainFrame;

public class GraphingCalculator {
	public static void main(String[] args) {
		// Yup, thats it. Make it go. Once the beast gets rolling it doesn't need any more help
		// to stay on its path!
		new MainFrame(new GraphingCalculatorController(new Plotter()));
	}
}
