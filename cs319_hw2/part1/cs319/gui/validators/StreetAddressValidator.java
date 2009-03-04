package cs319.gui.validators;

import cs319.gui.W2TextField;

public class StreetAddressValidator implements W2FieldValidator {

	@Override
	public String getError(W2TextField field) {
		if (isValid(field))
			return null;
		return "This field must contain a valid address.  No PO Box numbers.";
	}

	@Override
	public boolean isValid(W2TextField field) {
		String text = field.getText().trim();
		if (text.length() == 0)
			return false;
		if (!text.contains(" "))
			return false;
		String houseNumPart = text.substring(0, text.indexOf(" "));
		try {
			Integer.parseInt(houseNumPart);
		} catch (NumberFormatException nfe) {
			return false;
		}
		String afterHouseNum = text.substring(text.indexOf(" ") + 1).trim();
		if (afterHouseNum.length() == 0)
			return false;
		return true;
	}
}
