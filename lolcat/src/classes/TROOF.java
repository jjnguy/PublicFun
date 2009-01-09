package classes;

public class TROOF implements LOLVar {

	private boolean val;

	public TROOF(boolean b) {
		val = b;
	}

	@Override
	public NUMBAR castToNUMBAR() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NUMBR castToNUMBR() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TROOF castToTROOF() {
		return this;
	}

	@Override
	public YARN castToYARN() {
		if (val)
			return new YARN("WIN");
		return new YARN("FAIL");
	}

	@Override
	public boolean getTROOFVal() {
		return val;
	}
}
