package ui;

import plotter.Plotter;

public class GraphingCalculatorUI {

	private Plotter myPlotter;

	private double gapSpacing = .1;
	private double slope = 1;
	private double yIntercept = 0;
	private double amplitude = 1;

	public GraphingCalculatorUI(Plotter plotter) {
		myPlotter = plotter;
	}

	public void handleInput() {

	}

	private void handleTrigInput() {
	}

	private void handleLineInput() {
	}

	private void handleClear() {
	}

	private void handleChangeGap() {
	}

	private void mainMenu() {
		final String optionA = "a - plot trigonemetric function";
		final String optionB = "b - plot linear function";
		final String optionC = "c - clear plot";
		final String optionD = "d - change gap between samples";
		final String optionQ = "q - quit";

		final String fullMenu = optionA + '\n' + optionB + '\n' + optionC + '\n' + optionD
				+ '\n' + optionQ;

		System.out.println(fullMenu);
	}

	private void trigMenu() {
		final String optionA = "a - plot amplitude * sin(x)";
		final String optionB = "b - plot amplitude * cos(x)";
		final String optionC = "c - plot amplitude * tan(x)";
		final String optionD = String.format("d - change amplitude (currently %1.1f)",
				amplitude);
		final String optionQ = "q - return to main menu";

		final String fullMenu = optionA + '\n' + optionB + '\n' + optionC + '\n' + optionD
				+ '\n' + optionQ;
		System.out.println(fullMenu);
	}

	private void lineMenu() {
		final String optionA = String.format("a - change slope (currently %1.1f)", slope);
		final String optionB = String.format("b - change y-intercept (currently %1.1f)",
				yIntercept);
		final String optionQ = "q - return to main menu";

		final String fullMenu = optionA + '\n' + optionB + '\n' + optionQ;
		System.out.println(fullMenu);
	}

}
