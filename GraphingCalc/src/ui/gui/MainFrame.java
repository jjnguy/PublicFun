package ui.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plotter.Plotter;
import ui.GraphingCalculatorController;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public static double DEFAULT_FREQUENCY = 1;
	public static double DEFAULT_SLOPE = 1;
	public static double DEFAULT_INTERCEPT = 0;
	public static double DEFAULT_AMPLITUDE = 1;
	public static double DEFAULT_GAP = .1;
	public static String DEFAULT_POLY_EQN = "0x^0 +0x^1 +1x^2";

	private JLabel slopeLabel;
	private JTextField slopeField;

	private JLabel interceptLabel;
	private JTextField interceptField;

	private JLabel amplitudeLabel;
	private JTextField amplitudeField;

	private JLabel freqLabel;
	private JTextField freqField;

	private JLabel equationLabel;
	private JTextField equationField;

	private JLabel gapLabel;
	private JTextField gapField;

	private JCheckBox absModeCheck;

	private JButton graphStraitLineButton;
	private JButton graphTanLineButton;
	private JButton graphSinLineButton;
	private JButton graphCosLineButton;
	private JButton graphPolynomialButton;

	private TimerTask updateValues;

	private GraphingCalculatorController controller;

	public MainFrame(GraphingCalculatorController controllerP) {
		controller = controllerP;
		updateValues = new UpdateValsTimerTask();
		createComponents();
		layoutComponents();
		addListeners();
		miscInit();
		Timer t = new Timer();
	}

	private void miscInit() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		controller.startPlotter();
		setVisible(true);
	}

	private void createComponents() {
		slopeLabel = new JLabel("Slope:");
		slopeField = new JTextField(DEFAULT_SLOPE + "");

		interceptLabel = new JLabel("Y-Intercept:");
		interceptField = new JTextField(DEFAULT_INTERCEPT + "");

		amplitudeLabel = new JLabel("Amplitude:");
		amplitudeField = new JTextField(DEFAULT_AMPLITUDE + "");

		freqLabel = new JLabel("Frequency:");
		freqField = new JTextField(DEFAULT_FREQUENCY + "");

		equationLabel = new JLabel("Polynomial Equation:");
		equationField = new JTextField(DEFAULT_POLY_EQN);

		gapLabel = new JLabel("Gap:");
		gapField = new JTextField(DEFAULT_GAP + "");

		absModeCheck = new JCheckBox("Absolute Value Mode");

		graphStraitLineButton = new JButton("Graph Straight Line:");
		graphSinLineButton = new JButton("Graph Sine Curve");
		graphCosLineButton = new JButton("Graph Cosine Curve");
		graphTanLineButton = new JButton("Graph Tangent Curve");
		graphPolynomialButton = new JButton("Graph Polynomial Equation");
	}

	private void layoutComponents() {
		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridy = 0;
		gc.gridx = 0;
		mainPane.add(slopeLabel, gc);
		gc.gridx++;
		mainPane.add(slopeField, gc);
		gc.gridx--;
		gc.gridy++;
		mainPane.add(interceptLabel, gc);
		gc.gridx++;
		mainPane.add(interceptField, gc);
		gc.gridx++;
		gc.gridy--;
		mainPane.add(amplitudeLabel, gc);
		gc.gridx++;
		mainPane.add(amplitudeField, gc);
		gc.gridx--;
		gc.gridy++;
		mainPane.add(freqLabel, gc);
		gc.gridx++;
		mainPane.add(freqField, gc);
		gc.gridx -= 3;
		gc.gridy++;
		mainPane.add(gapLabel, gc);
		gc.gridx++;
		mainPane.add(gapField, gc);
		gc.gridx++;
		mainPane.add(equationLabel, gc);
		gc.gridx++;
		mainPane.add(equationField, gc);
		gc.gridy++;
		gc.gridx -= 3;
		mainPane.add(graphStraitLineButton, gc);
		gc.gridx++;
		mainPane.add(graphSinLineButton, gc);
		gc.gridx++;
		mainPane.add(graphCosLineButton, gc);
		gc.gridx++;
		mainPane.add(graphTanLineButton, gc);
		gc.gridx++;
		mainPane.add(graphPolynomialButton, gc);
		gc.gridy = 0;
		gc.gridx = 4;
		mainPane.add(absModeCheck,gc);
		add(mainPane);
	}

	private void addListeners() {
		graphStraitLineButton.addActionListener(straightListener);
		graphSinLineButton.addActionListener(sinListener);
		graphCosLineButton.addActionListener(cosListener);
		graphTanLineButton.addActionListener(tanListener);
		graphPolynomialButton.addActionListener(polyListener);
		absModeCheck.addChangeListener(absChange);
	}

	private void updateAllValues() {
		try {
			controller.changeGap(Double.parseDouble(gapField.getText()));
			controller.updateAmplitude(Double.parseDouble(amplitudeField.getText()));
			controller.updateFreq(Double.parseDouble(freqField.getText()));
			controller.updateSlope(Double.parseDouble(slopeField.getText()));
			controller.updateYIntercept(Double.parseDouble(interceptField.getText()));
			controller.updateCoeficients(equationField.getText());
			controller.toggleAbsMode(absModeCheck.isSelected());
		} catch (NumberFormatException e) {

		}
	}

	private void updateAndRefresh() {
		updateAllValues();
		controller.refreshGraph();
	}

	private ActionListener polyListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.updateGraphType(GraphingCalculatorController.POLYNOMIAL_GRAPH);
			updateAndRefresh();
		}
	};
	private ActionListener tanListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.updateGraphType(GraphingCalculatorController.TAN_GRAPH);
			updateAndRefresh();
		}
	};
	private ActionListener cosListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.updateGraphType(GraphingCalculatorController.COS_GRAPH);
			updateAndRefresh();
		}
	};
	private ActionListener sinListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.updateGraphType(GraphingCalculatorController.SIN_GRAPH);
			updateAndRefresh();
		}
	};
	private ActionListener straightListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.updateGraphType(GraphingCalculatorController.LINE_GRAPH);
			updateAndRefresh();
		}
	};
	private ChangeListener absChange = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			updateAndRefresh();
		}
	};
	private ChangeListener updateAndRefresh = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			updateAndRefresh();
		}
	};

	class UpdateValsTimerTask extends TimerTask {
		@Override
		public void run() {
			updateAndRefresh();
		}
	}
}
