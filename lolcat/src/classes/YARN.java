package classes;

public class YARN implements LOLVar {

	private String val;

	public YARN(String string) {
		val = string;
	}

	@Override
	public NUMBAR castToNUMBAR() {
		try {
			return new NUMBAR(Double.parseDouble(val));
		} catch (NumberFormatException e) {
			// TODO is this supposed to throw an error?
			throw new ClassCastException("Could not cast YARN to NUMBAR.");
		}
	}

	@Override
	public NUMBR castToNUMBR() {
		try {
			return new NUMBR(Integer.parseInt(val));
		} catch (NumberFormatException e) {
			// TODO is this supposed to throw an error?
			throw new ClassCastException("Could not cast YARN to NUMBAR.");
		}
	}

	@Override
	public TROOF castToTROOF() {
		if (val.length() == 0)
			return new TROOF(false);
		return new TROOF(true);
	}

	@Override
	public YARN castToYARN() {
		return this;
	}

	@Override
	public boolean getTROOFVal() {
		return val.length() == 0 ? false : true;
	}

}
