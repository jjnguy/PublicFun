package cs319.gui.validators;

import cs319.gui.W2TextField;

public class LastNameValidator implements W2FieldValidator {

	@Override
	public String getError(W2TextField field) {
		if (isValid(field))
			return null;
		return "This field must contain a Last Name.";
	}

	@Override
	public boolean isValid(W2TextField field) {
		String text = field.getText();
		if (text.length() == 0)
			return false;
		if (!text.matches("[\\w&&\\D]+"))
			return false;

		return true;
	}

}
