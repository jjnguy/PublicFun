package classes;

public class NUMBR implements LOLVar {

	private long val;

	public NUMBR(long val) {
		this.val = val;
	}

	@Override
	public NUMBAR castToNUMBAR() {
		return new NUMBAR(val);
	}

	@Override
	public NUMBR castToNUMBR() {
		return this;
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
