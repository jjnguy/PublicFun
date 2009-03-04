package cs319.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * This class provides verification for a checkbox. It lets a user know that it
 * has not yet been given attention too.
 * 
 * @author Justin Timberlake
 * 
 */
@SuppressWarnings("serial")
public class W2CheckBox extends JCheckBox implements W2Component {

	/**
	 * A boolean value that holds the verification state of the W2CheckBox. That
	 * was prolly clear before the comment though.
	 */
	private boolean hasBeenValidated;

	/**
	 * Constructs a W2CHeckBox with the given text.
	 * 
	 * @param text
	 *            the text to display next to the checkbox
	 */
	public W2CheckBox(String text) {
		super(text);
		setBackground(W2TextField.NO_VALUE_COLOR);
		addActionListener(checkedAction);
		hasBeenValidated = false;
	}

	/**
	 * Very similar to the boolean value from above. This one is only readable
	 * though.
	 */
	@Override
	public boolean hasBeenVerified() {
		return hasBeenValidated;
	}

	/**
	 * An action listener that alerts the check box that it has been given
	 * attention
	 */
	private ActionListener checkedAction = new ActionListener() {
		/**
		 * This happens if an action is performed.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			setBackground(W2TextField.VALID_COLOR);
			hasBeenValidated = true;
		}
	};
}
