public abstract class MathProblem {

	protected int topnum, bottomnum;

	public MathProblem(int top, int bottom) {
		if (top > 19 || bottom > 19) {
			//throw new IllegalArgumentException();
		}
		topnum = top;
		bottomnum = bottom;
	}

	public String topRow() {
		return String.format(" %2d", topnum);
	}

	public String bottomRow() {
		return String.format("%s%2d", getSymbol() + "", bottomnum);
	}

	public String bar() {
		return "---";
	}

	public String draw() {
		String ret = "";
		ret += topRow();
		ret += "\n";
		ret += bottomRow();
		ret += "\n";
		ret += bar();
		return ret;
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof MathProblem)) {
			return false;
		}
		MathProblem otherM = (MathProblem) other;
		return otherM.bottomnum == bottomnum && otherM.topnum == topnum;
	}

	public String toString() {
		return topnum + " " + bottomnum + " " + getSymbol();
	}

	public abstract char getSymbol();

	public abstract String getAnswer();
}
