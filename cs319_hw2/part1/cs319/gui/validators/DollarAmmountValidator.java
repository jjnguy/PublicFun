package cs319.gui.validators;

import cs319.gui.W2TextField;

public class DollarAmmountValidator implements W2FieldValidator {

	@Override
	public String getError(W2TextField field) {
		if (!isValid(field)) {
			return "This field must be formatted as money. <$0.00>";
		}
		return null;
	}

	@Override
	public boolean isValid(W2TextField field) {
		String text = field.getText();
		if (!text.startsWith("$"))
			return false;
		text = text.substring(1);
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
