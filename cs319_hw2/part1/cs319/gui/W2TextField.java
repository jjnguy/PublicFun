package cs319.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import cs319.gui.validators.W2FieldValidator;

/**
 * Class used for validating input in a W2 Form.
 * 
 * @author Justin Nelson
 * 
 */
@SuppressWarnings("serial")
public class W2TextField extends JPanel implements W2Component {

	/**
	 * Color for the background of the text field if valid
	 */
	public static final Color INVALID_COLOR = new Color(255, 119, 119);
	/**
	 * Color for the background of the text field if not valid
	 */
	public static final Color VALID_COLOR = new Color(112, 255, 91);
	/**
	 * Color for the background of the text field if not yet set
	 */
	public static final Color NO_VALUE_COLOR = new Color(245, 255, 60);

	/**
	 * The orientation of the text in relation to the label
	 */
	public static final int HORIZONTAL = -5;
	/**
	 * The orientation of the text in relation to the label
	 */
	public static final int VERTICAL = -7;

	/**
	 * The validator belonging to this W2TextField
	 */
	private W2FieldValidator validator;
	/**
	 * Member describing some state of the object...I forgot which one now...
	 */
	private boolean hasHadFocus;
	/**
	 * The text that should be validated
	 */
	private JTextField theText;
	/**
	 * The label of the text
	 */
	private JLabel theLabel;
	/**
	 * An int describing the layout orientation of the component
	 */
	private int layout;

	/**
	 * Constructs an empty W2TextField with a given validator and layout and
	 * label
	 * 
	 * @param label
	 *            the label for the text
	 * @param validator
	 *            the validator to verify the text
	 * @param layout
	 *            the orientation of the component
	 */
	public W2TextField(String label, W2FieldValidator validator, int layout) {
		if (layout == HORIZONTAL) {
			setLayout(new GridLayout(1, 2));
		} else if (layout == VERTICAL) {
			setLayout(new GridLayout(2, 1));
		} else throw new IllegalArgumentException("The orientation must be horizontal or vertical.");

		this.layout = layout;

		theText = new JTextField(15);
		theLabel = new JLabel(label);
		add(theLabel);
		add(theText);
		hasHadFocus = false;
		this.validator = validator;
		this.theText.addFocusListener(focusListener);
		this.theText.addKeyListener(typeListener);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		theText.setEditable(enabled);
		theLabel.setEnabled(enabled);
	}

	public String getText() {
		return theText.getText();
	}

	public void setText(String text) {
		theText.setText(text);
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		if (this.layout == W2TextField.VERTICAL) {
			super.setPreferredSize(new Dimension(preferredSize.width + 4, preferredSize.height * 2));
		} else if (this.layout == W2TextField.HORIZONTAL) {
			super.setPreferredSize(new Dimension(preferredSize.width * 2, preferredSize.height));
		}
		theText.setPreferredSize(preferredSize);
	}

	@Override
	public synchronized void addKeyListener(KeyListener l) {
		theText.addKeyListener(l);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setToolTipText(validator.getError(this));
		theLabel.setToolTipText(validator.getError(this));
		theText.setToolTipText(validator.getError(this));
		if (!isEnabled()) {
			this.theText.setBackground(Color.LIGHT_GRAY);
		} else if (!validator.isValid(this) && !hasHadFocus) {
			this.theText.setBackground(NO_VALUE_COLOR);
		} else if (!validator.isValid(this)) {
			this.theText.setBackground(INVALID_COLOR);
		} else {
			this.theText.setBackground(VALID_COLOR);
		}
	}

	private KeyListener typeListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			repaint();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			repaint();
		}
	};

	private FocusListener focusListener = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			theText.setSelectionEnd(theText.getText().length());
			theText.setSelectionStart(0);
		}

		@Override
		public void focusLost(FocusEvent e) {
			hasHadFocus = true;
			repaint();
		}
	};

	@Override
	public boolean hasBeenVerified() {
		return validator.isValid(this);
	}

	public void addDocumentListener(DocumentListener taxableIntrestChangeL) {
		theText.getDocument().addDocumentListener(taxableIntrestChangeL);
	}
}
