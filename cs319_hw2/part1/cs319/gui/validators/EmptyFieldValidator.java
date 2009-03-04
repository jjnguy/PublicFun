package cs319.gui.validators;

import cs319.gui.W2TextField;

public class EmptyFieldValidator implements W2FieldValidator {

	@Override
	public boolean isValid(W2TextField field) {
		return field.getText().trim().length() != 0;
	}

	@Override
	public String getError(W2TextField field) {
		if (!isValid(field))
			return "This field must not be blank.";
		return null;
	}

}
