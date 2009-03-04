package cs319.gui.validators;

import cs319.gui.W2TextField;

public class FirstNameMiddleInitialValidator implements W2FieldValidator {
	@Override
	public String getError(W2TextField field) {
		if (isValid(field))
			return null;
		return "This field must contain your first name and middle initial.  <John, J>";
	}

	@Override
	public boolean isValid(W2TextField field) {
		String test = field.getText().trim();
		if (test.length() == 0)
			return false;
		if (!test.contains(",") && !test.contains(" "))
			return false;
		String[] twoParts = test.split(", *", -1);
		if (twoParts[1].trim().length() != 1)
			return false;
		return true;
	}
}
