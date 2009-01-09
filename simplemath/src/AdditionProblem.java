public class AdditionProblem extends MathProblem {

	public AdditionProblem(int top, int bottom) {
		super(top, bottom);
	}

	@Override
	public char getSymbol() {
		return '+';
	}

	@Override
	public String getAnswer() {
		return String.format(" %2d", topnum + bottomnum);
	}

}
