package genericComponents;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CounterPanel extends JPanel {

	private boolean clickable;

	private int number;

	private JLabel hundreds, tens, ones;

	public CounterPanel(int startValue, Color color, boolean clickableP) {
		if (startValue > 999 || startValue < -99)
			throw new IllegalArgumentException("The panel supports -99 to 999");

		clickable = clickableP;

		setFont(new Font("Ariel", Font.BOLD, 20));

		this.hundreds = new JLabel();
		this.tens = new JLabel();
		this.ones = new JLabel();
		this.hundreds.setForeground(color);
		this.hundreds.setFont(new Font("Ariel", Font.BOLD, 20));
		this.tens.setForeground(color);
		this.tens.setFont(new Font("Ariel", Font.BOLD, 20));
		this.ones.setForeground(color);
		this.ones.setFont(new Font("Ariel", Font.BOLD, 20));

		setValue(startValue);

		addMouseListener(ml);

		add(hundreds);
		add(tens);
		add(ones);
	}

	public void setValue(int number) {
		this.number = number;
		// TODO implement support below 0
		if (number >= 0) {
			int hundreds = number / 100;
			this.hundreds.setText(hundreds + "");
			int tens = (number - hundreds * 100) / 10;
			this.tens.setText(tens + "");
			int ones = number - (hundreds * 100 + tens * 10);
			this.ones.setText(ones + "");
		}
	}

	public int getValue() {
		return number;
	}

	public CounterPanel(int startValue) {
		this(startValue, Color.BLACK, false);
	}

	public CounterPanel(Color color) {
		this(0, color, false);
	}

	public CounterPanel() {
		this(0, Color.BLACK, false);
	}

	public int increment(int ammnt) {
		number += ammnt;
		setValue(number);
		return number;
	}

	public int increment() {
		return increment(1);
	}

	public int decrement(int ammnt) {
		number -= ammnt;
		setValue(number);
		return number;
	}

	public int decrement() {
		return decrement(1);
	}

	private MouseListener ml = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!clickable)
				return;
			if (e.getButton() == MouseEvent.BUTTON1)
				increment();
			else
				decrement();
		}
	};
}
