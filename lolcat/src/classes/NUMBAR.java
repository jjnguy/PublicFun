package classes;

public class NUMBAR implements LOLVar {

	private double val;

	public NUMBAR(long val) {
		this.val = (double) val;
	}

	public NUMBAR(double val) {
		this.val = val;
	}

	@Override
	public NUMBAR castToNUMBAR() {
		return this;
	}

	@Override
	public NUMBR castToNUMBR() {
		return new NUMBR((long) val);
	}

	@Override
	public TROOF castToTROOF() {
		if (val == 0)
			return new TROOF(false);
		return new TROOF(true);
	}

	@Override
	public YARN castToYARN() {
		return new YARN(val + "");
	}

	@Override
	public boolean getTROOFVal() {
		return val == 0 ? false : true;
	}

}
