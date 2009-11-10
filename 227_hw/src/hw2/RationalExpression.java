package hw2;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RationalExpression implements Comparable<RationalExpression>{

	private String stringRep;
	
	public RationalExpression(String exprn) {
		stringRep = exprn;
	}

	public Rational evaluate(){
		String rational = "\\s*(\\[\\s*-?\\d+\\s*/\\s*-?\\d+\\s*\\])\\s*";
		String operator = "(\\s*[+-*/]\\s*)";
		String exprnRegex = rational + operator + rational + operator + rational;
		Matcher m = Pattern.compile(exprnRegex).matcher(stringRep);
		Rational rational1 = new Rational(m.group(1).trim());
		char op1 = m.group(2).trim().charAt(0);
		Rational rational2 = new Rational(m.group(3).trim());
		char op2 = m.group(4).trim().charAt(0);
		Rational rational3 = new Rational(m.group(5).trim());
		if (op2 == '*' || op2 == '/') {
			return evaluate(rational1, (evaluate(rational2, rational3, op2)), op1);
		} else {
			return evaluate(evaluate(rational1, rational2, op1), rational3, op2);
		}
	}

	private static Rational evaluate(Rational one, Rational two, char opperator) {
		switch (opperator) {
		case '+':
			return one.add(two);
		case '-':
			return one.subtract(two);
		case '*':
			return one.multiply(two);
		case '/':
			return one.divide(two);
		default:
			throw new RuntimeException();
		}
	}

	@Override
	public int compareTo(RationalExpression o) {
		return this.evaluate().compareTo(o.evaluate());
	}

	public int compare(RationalExpression o) {
		return this.compareTo(o);
	}
}
