package cs319.gui.validators;

import cs319.gui.W2TextField;

public class NumberFieldValidator implements W2FieldValidator {

	@Override
	public String getError(W2TextField field) {
		if (!isValid(field)) {
			return "This field must contain a number";
		}
		return null;
	}

	@Override
	public boolean isValid(W2TextField field) {
		String text = field.getText();
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
