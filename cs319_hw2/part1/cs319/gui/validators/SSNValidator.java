package cs319.gui.validators;

import cs319.gui.W2TextField;

public class SSNValidator implements W2FieldValidator {

	@Override
	public String getError(W2TextField field) {
		if (isValid(field))
			return null;
		return "This field must contain a valid SSN. <XXX-XX-XXXX>";
	}

	@Override
	public boolean isValid(W2TextField field) {
		String text = field.getText().trim();
		if (text.length() != 11)
			return false;
		String[] parts = text.split("-", -1);
		if (parts.length != 3)
			return false;
		try {
			Integer.parseInt(parts[0]);
			Integer.parseInt(parts[2]);
			Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
