package cs319.gui.validators;

import cs319.gui.W2TextField;

/**
 * An interface for simplifying a way of validating different types of inputs in
 * a W2TextField.
 * 
 * @author Justin time
 * 
 */
public interface W2FieldValidator {
	/**
	 * Returns whether or not the validator is valid for the given W2TextField
	 * input
	 * 
	 * @param field
	 *            the filed to validate
	 * @return whether or not the form is valid
	 */
	public boolean isValid(W2TextField field);

	/**
	 * Gets the error of the validator
	 * 
	 * @param field
	 *            the filed to validate
	 * @return a String that describes the error of the validator, null if ther
	 *         is none
	 */
	public String getError(W2TextField field);
}
