package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphingCalculatorUI {
	private GraphingCalculatorController controller;
	private static final boolean EXTRA_FEATURES = true;
	private Scanner stdin;

	public GraphingCalculatorUI(GraphingCalculatorController cont) {
		controller = cont;
		stdin = new Scanner(System.in);
		controller.startPlotter();
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
				controller.stopPlotter();
				return;
			}
			if (choice == 'a') {
				handleTrigInput();
			} else if (choice == 'b') {
				controller.updateGraphType(GraphingCalculatorController.LINE_GRAPH);
				controller.refreshGraph();
				handleLineInput();
			} else if (choice == 'c') {
				controller.clearGraph();
			} else if (choice == 'd') {
				handleChangeGap();
			} else if (EXTRA_FEATURES && choice == 'e') {
				handlePolynomialInput();
			} else if (EXTRA_FEATURES && choice == 'f') {
				handleToggleAbsMode();
			} else {
				allertOfInvalidInput();
			}
			controller.refreshGraph();
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
				controller.updateGraphType(GraphingCalculatorController.SIN_GRAPH);
			} else if (choice == 'b') {
				controller.updateGraphType(GraphingCalculatorController.COS_GRAPH);
			} else if (choice == 'c') {
				controller.updateGraphType(GraphingCalculatorController.TAN_GRAPH);
			} else if (choice == 'd') {
				double newAmplitude;
				try {
					newAmplitude = getUserInputNumber("Please enter the new amplitude: ");
					controller.updateAmplitude(newAmplitude);
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			} else {
				allertOfInvalidInput();
			}
			controller.refreshGraph();
		}
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
					controller.updateSlope(newSlope);
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			} else if (choice == 'b') {
				double newIntercept;
				try {
					newIntercept = getUserInputNumber("Enter the new y-intercept please: ");
					controller.updateYIntercept(newIntercept);
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			}
			controller.refreshGraph();
		}
	}

	private void handleChangeGap() {
		double newGap;
		try {
			newGap = getUserInputNumber("Enter the new gap spacing: ");
			if (newGap == 0)
				return;
			controller.changeGap(Math.abs(newGap));
		} catch (IllegalArgumentException e) {
			allertOfInvalidInput();
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
				+ (controller.getAbsMode() ? "ON" : "OFF") + ")";

		String fullMenu = optionA + '\n' + optionB + '\n' + optionC + '\n' + optionD + '\n';
		if (EXTRA_FEATURES)
			fullMenu += optionE + '\n' + optionF + '\n';
		fullMenu += optionQ;

		System.out.println(fullMenu);
	}

	private void trigMenu() {
		final String optionA = "a - plot amplitude * sin(x)";
		final String optionB = "b - plot amplitude * cos(x)";
		final String optionC = "c - plot amplitude * tan(x)";
		final String optionD = String.format("d - change amplitude (currently %1.1f)",
				controller.getAmplitude());
		final String optionQ = "q - return to main menu";

		final String fullMenu = optionA + '\n' + optionB + '\n' + optionC + '\n' + optionD
				+ '\n' + optionQ;
		System.out.println(fullMenu);
	}

	private void lineMenu() {
		final String optionA = String.format("a - change slope (currently %1.1f)", controller
				.getSlope());
		final String optionB = String.format("b - change y-intercept (currently %1.1f)",
				controller.getYIntercept());
		final String optionQ = "q - return to main menu";

		final String fullMenu = optionA + '\n' + optionB + '\n' + optionQ;
		System.out.println(fullMenu);
	}

	private void handleToggleAbsMode() {
		controller.toggleAbsMode();
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
					controller.updateCoeficients(getCoeficients("Please enter the formula: "));
					controller.updateGraphType(GraphingCalculatorController.POLYNOMIAL_GRAPH);
				} catch (IllegalArgumentException e) {
					allertOfInvalidInput();
				}
			} else if (choice == 'b') {
				System.out.println("Sorry, this is not implemented yet.");
			}
			controller.refreshGraph();
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

	private void polynomialMenu() {
		final String optionA = "a - plot a polynomial";
		final String optionB = "b - change a term's coefficient";
		final String optionQ = "q - quit";
		final String fullMenu = optionA + '\n' + optionB + '\n' + optionQ;
		System.out.println(fullMenu);
	}
}
