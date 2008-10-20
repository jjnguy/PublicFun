package ui;

import java.util.ArrayList;
import java.util.List;
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
			} else if (EXTRA_FEAURES && choice == 'e') {
				handlePolynomialInput();
			} else if (EXTRA_FEAURES && choice == 'f') {
				handleToggleAbsMode();
			} else if (choice == 't') {
				myPlotter.widenView(2);
			} else if (choice == 'r') {
				myPlotter.thinView(2);
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
			plotStraitLine();
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
			double yVal = amplitude * Math.tan(i);
			if (absMode)
				yVal = Math.abs(yVal);
			myPlotter.addPoint(i, yVal);
		}
	}

	private void plotSinLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			double yVal = amplitude * Math.sin(i);
			if (absMode)
				yVal = Math.abs(yVal);
			myPlotter.addPoint(i, yVal);
		}
	}

	private void plotCosLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			double yVal = amplitude * Math.cos(i);
			if (absMode)
				yVal = Math.abs(yVal);
			myPlotter.addPoint(i, yVal);
		}
	}

	private void plotStraitLine() {
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			double yVal = slope * i + yIntercept;
			if (absMode)
				yVal = Math.abs(yVal);
			myPlotter.addPoint(i, yVal);
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

		final String optionE = "e - plot polynomial line";
		final String optionF = "f - toggle absolute value mode (currently "
				+ (absMode ? "ON" : "OFF") + ")";

		String fullMenu = optionA + '\n' + optionB + '\n' + optionC + '\n' + optionD + '\n';
		if (EXTRA_FEAURES)
			fullMenu += optionE + '\n' + optionF + '\n';
		fullMenu += optionQ;

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

	/////////////////////////////////////////////////
	/// Begin extra code not in the homework spec //
	///////////////////////////////////////////////

	// TODO derivative mode

	public static final boolean EXTRA_FEAURES = true;

	private static final int POLYNOMIAL_GRAPH = 5;
	private boolean absMode = false;

	/**
	 * Represents a polynomial function
	 * the order of the terms is as follows:
	 * ax^0 + bx^1 + cx^2 and so on
	 * So, in other words the index of the coefficient is
	 * the power of x in each term
	 */
	private List<Double> polynomialCoefficients;

	private void handleToggleAbsMode() {
		absMode = !absMode;
		refreshGraph();
	}

	private void handlePolynomialInput() {
		while (6 == 6) {
			polynomialMenu();
			char choice = 0;
			try {
				choice = getUserInputMenuChoice();
			} catch (IllegalArgumentException e) {
				allertOfInvalidInput();
				continue;
			}
			if (choice == 'q')
				return;
			if (choice == 'a') {
				try {
					polynomialCoefficients = getCoeficients("Please enter the formula: ");
					curType = POLYNOMIAL_GRAPH;
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			} else if (choice == 'b') {
				System.out.println("Sorry, this is not implemented yet.");
			}
			refreshGraph();
		}
	}

	private List<Double> getCoeficients(String prompt) {
		List<Double> ret = new ArrayList<Double>();
		System.out.println("Use the format:  ax^0 (+/-)bx^1 (+/-)cx^3.");
		System.out.println("Note, the spacing is important");
		System.out.println("To leave out a term use a coefficient of 0.");
		System.out.println("It is legal to begin with a negtive term.");
		System.out.print(prompt);
		String input = stdin.nextLine();
		String splitBy = "x\\^\\d";
		String[] brokenDownInput = input.split(splitBy);
		try {
			for (String string : brokenDownInput) {
				ret.add(Double.parseDouble(string.trim()));
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private void plotPolynomialLine() {
		if (polynomialCoefficients == null) {
			polynomialCoefficients = new ArrayList<Double>();
		}
		for (double i = Plotter.VIEWPORT_MIN; i <= Plotter.VIEWPORT_MAX; i += gapSpacing) {
			double yVal = 0;
			for (int j = 0; j < polynomialCoefficients.size(); j++) {
				double coefficient = polynomialCoefficients.get(j);
				yVal += coefficient * (Math.pow(i, j));
			}
			if (absMode)
				yVal = Math.abs(yVal);
			myPlotter.addPoint(i, yVal);
		}
	}

	private void polynomialMenu() {
		final String optionA = "a - plot a polynomial";
		final String optionB = "b - change a term's coefficient";
		final String optionQ = "q - quit";
		final String fullMenu = optionA + '\n' + optionB + '\n' + optionQ;
		System.out.println(fullMenu);
	}
}
