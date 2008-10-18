package ui;

import plotter.Plotter;

public class GraphingCalculator {
	public static void main(String[] args) {
		new GraphingCalculatorUI(new Plotter()).handleInput();
	}
}
