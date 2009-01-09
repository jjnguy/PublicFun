public class SubtractionProblem extends MathProblem {

	public SubtractionProblem(int top, int bottom) {
		super(top, bottom);
	}

	@Override
	public char getSymbol() {
		return '-';
	}

	@Override
	public String getAnswer() {
		return String.format(" %2d", topnum - bottomnum);
	}
}
