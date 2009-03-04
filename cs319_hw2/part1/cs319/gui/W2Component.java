package cs319.gui;

/**
 * Fun little interface to add validation to component values
 * 
 * @author Justin Timberlake
 * 
 */
public interface W2Component {
	/**
	 * 
	 * @return whether or not the value/state of the form is a valid one for a
	 *         W2 form
	 */
	public boolean hasBeenVerified();
}
