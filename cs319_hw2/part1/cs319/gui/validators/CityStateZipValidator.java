package cs319.gui.validators;

import cs319.gui.W2TextField;

public class CityStateZipValidator implements W2FieldValidator {

	@Override
	public String getError(W2TextField field) {
		if (isValid(field))
			return null;
		return "This field must contain City, State, Zipcode.";
	}

	@Override
	public boolean isValid(W2TextField field) {
		String text = field.getText().trim();
		if (text.length() < 13)
			return false;
		String[] parts = text.split(", *", -1);
		if (parts.length != 3)
			return false;
		if (parts[0].length() < 3)
			return false;
		if (parts[1].length() < 2)
			return false;
		if (parts[2].trim().length() != 5)
			return false;
		try {
			Integer.parseInt(parts[2].trim());
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
}
