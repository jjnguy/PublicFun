package guiElements;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.CheckPrime;

/**
 * @author Justin
 * 
 */
public class PrimePanel extends JPanel implements ActionListener, KeyListener {

	/**
	 * 
	 */
	JTextField input;

	/**
	 * 
	 */
	JTextArea output;

	/**
	 * 
	 */
	JButton click;

	/**
	 * 
	 */
	protected PrimePanel() {
		super(new GridLayout());

		input = new JTextField();
		input.addKeyListener(this);
		input.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

		click = new JButton("Click to calculate");
		click.addActionListener(this);
		click.setActionCommand("calculate");
		click.setFocusable(false);
		click.addKeyListener(this);

		output = new JTextArea();
		output.setEditable(false);
		output.addKeyListener(this);
		output.setFocusable(false);

		add(input);
		add(click);
		add(output);

	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("calculate")) setBoxes();

	}

	/**
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_RIGHT) setBoxes();

	}

	/**
	 * 
	 */
	private void setBoxes() {
		String curNum;
		if (input.getText().matches("\\d+")) try {
			curNum = input.getText();
			if (CheckPrime.isPrime(new BigInteger(curNum))) output.setText(input.getText() + " is prime.");
			else output.setText(CheckPrime.getListOfFactors(new BigInteger(curNum + "")).toString());
		} catch (NumberFormatException nfe) {
			input.setText("");
			output.setText("Stick below " + Integer.MAX_VALUE + ", thanks.");
		}
		else {
			output.setText("Please enter only integers");
			input.setText("");
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {

	}
}
