package ui;

import java.util.Scanner;

import plotter.Plotter;

public class GraphingCalculatorUI {

	private static final int LINE_GRAPH = 0;
	private static final int TAN_GRAPH = 1;
	private static final int COS_GRAPH = 2;
	private static final int SIN_GRAPH = 3;
	private static final int NO_GRAPH = 4;

	private Plotter myPlotter;

	private double gapSpacing = .1;
	private double slope = 1;
	private double yIntercept = 0;
	private double amplitude = 1;

	private int curType = NO_GRAPH;

	private Scanner stdin;

	public GraphingCalculatorUI(Plotter plotter) {
		myPlotter = plotter;
		stdin = new Scanner(System.in);
		myPlotter.startPlotter();
	}

	public void handleInput() {
		System.out.println("Main Menu:");
		while (2 + 2 == 4) {
			mainMenu();
			char choice = 0;
			try {
				choice = getUserInputMenuChoice();
			} catch (IllegalArgumentException e) {
				allertOfInvalidInput();
				continue;
			}
			if (choice == 'q') {
				System.out.println("C ya!");
				myPlotter.stopPlotter();
				return;
			}
			if (choice == 'a') {
				handleTrigInput();
			} else if (choice == 'b') {
				curType = LINE_GRAPH;
				refreshGraph();
				handleLineInput();
			} else if (choice == 'c') {
				handleClear();
			} else if (choice == 'd') {
				handleChangeGap();
			} else {
				allertOfInvalidInput();
			}
			refreshGraph();
		}
	}

	private void allertOfInvalidInput() {
		System.out.println("Invalid input:  Press ENTER");
		stdin.nextLine();
	}

	private void handleTrigInput() {
		while (1 + 1 == 2) {
			trigMenu();
			char choice = 0;
			try {
				choice = getUserInputMenuChoice();
			} catch (IllegalArgumentException e) {
				allertOfInvalidInput();
				continue;
			}
			if (choice == 'q') {
				return;
			}
			if (choice == 'a') {
				curType = SIN_GRAPH;
			} else if (choice == 'b') {
				curType = COS_GRAPH;
			} else if (choice == 'c') {
				curType = TAN_GRAPH;
			} else if (choice == 'd') {
				double newAmplitude;
				try {
					newAmplitude = getUserInputNumber("Please enter the new amplitude: ");
					amplitude = newAmplitude;
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			} else {
				allertOfInvalidInput();
			}
			refreshGraph();
		}
	}

	private void refreshGraph() {
		myPlotter.clear();
		if (curType == NO_GRAPH)
			return;
		if (curType == COS_GRAPH)
			plotCosLine();
		if (curType == SIN_GRAPH)
			plotSinLine();
		if (curType == TAN_GRAPH)
			plotTanLine();
		if (curType == LINE_GRAPH)
			plotLine();
		// non-homework option
		if (curType == POLYNOMIAL_GRAPH)
			plotPolynomialLine();
	}

	private void handleLineInput() {
		while (3 / 2 == 1) {
			lineMenu();
			char choice = 0;
			try {
				choice = getUserInputMenuChoice();
			} catch (IllegalArgumentException e) {
				allertOfInvalidInput();
				continue;
			}
			if (choice == 'q') {
				return;
			}
			if (choice == 'a') {
				double newSlope;
				try {
					newSlope = getUserInputNumber("Enter the new slope please: ");
					slope = newSlope;
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			} else if (choice == 'b') {
				double newIntercept;
				try {
					newIntercept = getUserInputNumber("Enter the new y-intercept please: ");
					yIntercept = newIntercept;
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			}
			refreshGraph();
		}
	}

	private void handleClear() {
		myPlotter.clear();
		curType = NO_GRAPH;
	}

	private void handleChangeGap() {
		double newGap;
		try {
			newGap = getUserInputNumber("Enter the new gap spacing: ");
			if (newGap == 0)
				return;
			gapSpacing = Math.abs(newGap);
		} catch (IllegalArgumentException e) {
			allertOfInvalidInput();
		}
	}

	private void plotTanLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			myPlotter.addPoint(i, amplitude * Math.tan(i));
		}
	}

	private void plotSinLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			myPlotter.addPoint(i, amplitude * Math.sin(i));
		}
	}

	private void plotCosLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			myPlotter.addPoint(i, amplitude * Math.cos(i));
		}
	}

	private void plotLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			myPlotter.addPoint(i, slope * i + yIntercept);
		}
	}

	private char getUserInputMenuChoice() {
		System.out.print(">>");
		String input = stdin.nextLine().trim().toLowerCase();
		if (input.length() != 1)
			throw new IllegalArgumentException();
		return input.charAt(0);
	}

	private double getUserInputNumber(String prompt) {
		System.out.print(prompt);
		String input = stdin.nextLine().trim();
		double number = 0;
		try {
			number = Double.parseDouble(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		return number;
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

	public static final boolean HOMEWORK_MODE = false;

	// Begin extra code not in the homework spec

	private static final int POLYNOMIAL_GRAPH = 5;

	private void plotPolynomialLine() {
		
	}

	private void polynomialMenu() {
		final String optionA = "a - plot a polynomial";
		final String optionB = "b - change a term's coefficient";
		final String optionQ = "q - quit";
		final String fullMenu = optionA + '\n' + optionB + '\n' + optionQ;
		System.out.println(fullMenu);
	}
}
