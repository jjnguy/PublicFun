package ui;

import java.util.ArrayList;
import java.util.List;

import plotter.Plotter;

public class GraphingCalculatorController {

	public static final int LINE_GRAPH = 0;
	public static final int TAN_GRAPH = 1;
	public static final int COS_GRAPH = 2;
	public static final int SIN_GRAPH = 3;
	public static final int NO_GRAPH = 4;
	public static final int POLYNOMIAL_GRAPH = 5;

	private Plotter myPlotter;

	private boolean absMode = false;
	private double gapSpacing = .1;
	private double slope = 1;
	private double yIntercept = 0;
	private double amplitude = 1;

	/**
	 * Represents a polynomial function
	 * the order of the terms is as follows:
	 * ax^0 + bx^1 + cx^2 and so on
	 * So, in other words the index of the coefficient is
	 * the power of x in each term
	 */
	private List<Double> polynomialCoefficients;

	private int curType = NO_GRAPH;

	public GraphingCalculatorController(Plotter plotter) {
		myPlotter = plotter;
	}

	public void startPlotter() {
		myPlotter.startPlotter();
	}

	public void stopPlotter() {
		myPlotter.stopPlotter();
	}

	public void zoomIn(int factor) {
		myPlotter.zoomIn(factor);
	}

	public void zoomOut(int factor) {
		myPlotter.zoomOut(factor);
	}

	public void widenView(int factor) {
		myPlotter.widenView(factor);
	}

	public void thinView(int factor) {
		myPlotter.thinView(factor);
	}

	public void widenHeight(int factor) {
		myPlotter.growViewHeight(factor);
	}

	public void shrinkHeight(int factor) {
		myPlotter.shrinkViewHeight(factor);
	}

	public void updateAmplitude(double newAmplitude) {
		amplitude = newAmplitude;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void updateSlope(double newSlope) {
		slope = newSlope;
	}

	public void updateYIntercept(double newIntercept) {
		yIntercept = newIntercept;
	}

	public double getYIntercept() {
		return yIntercept;
	}

	public double getSlope() {
		return slope;
	}

	public boolean getAbsMode() {
		return absMode;
	}

	public void updateGraphType(int graphType) {
		curType = graphType;
	}

	public void updateCoeficients(String funct) {
		if (polynomialCoefficients == null) {
			polynomialCoefficients = new ArrayList<Double>();
		}
		polynomialCoefficients.clear();
		String splitBy = "x\\^\\d";
		String[] brokenDownInput = funct.split(splitBy);
		for (String string : brokenDownInput) {
			polynomialCoefficients.add(Double.parseDouble(string.trim()));
		}
	}

	public void refreshGraph() {
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

	public void clearGraph() {
		myPlotter.clear();
		curType = NO_GRAPH;
		refreshGraph();
	}

	public void changeGap(double newGap) {
		if (newGap == 0)
			return;
		gapSpacing = Math.abs(newGap);
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

	public void toggleAbsMode() {
		absMode = !absMode;
		refreshGraph();
	}

	public void toggleAbsMode(boolean selected) {
		absMode = selected;
	}
}
