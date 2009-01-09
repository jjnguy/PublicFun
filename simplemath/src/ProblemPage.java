import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProblemPage {

	private List<MathProblem> problems;

	public ProblemPage() {
		Random r = new Random();
		problems = new ArrayList<MathProblem>(100);
		List<MathProblem> allProbs = ProblemPage.getAllAdditionProblems(0, 12);
		int numProbs = allProbs.size();
		for (int i = 0; i < numProbs; i++) {
			int next = r.nextInt(allProbs.size());
			problems.add(allProbs.get(next));
			allProbs.remove(next);
		}
	}

	public String print(boolean answer) {
		String ret = "";
		int cols = 11;
		for (int j = 0; j < 99; j += cols) {
			for (int i = 0; i < cols; i++) {
				MathProblem p = problems.get(i + j);
				ret += p.topRow();
				ret += "    ";
			}
			ret += "\n";
			for (int i = 0; i < cols; i++) {
				MathProblem p = problems.get(i + j);
				ret += p.bottomRow();
				ret += "    ";
			}
			ret += "\n";
			for (int i = 0; i < cols; i++) {
				MathProblem p = problems.get(i + j);
				ret += p.bar();
				ret += "    ";
			}
			ret += "\n";
			if (answer) {
				for (int i = 0; i < cols; i++) {
					MathProblem p = problems.get(i + j);
					ret += p.getAnswer();
					ret += "    ";
				}
			} else {
				ret += "\n";
			}
			ret += "\n\n";
		}
		return ret;
	}

	public static List<MathProblem> getAllAdditionProblems(int low, int high) {
		List<MathProblem> ret = new ArrayList<MathProblem>((high - low) * (high - low));
		for (int n = low; n <= high; n++) {
			for (int i = low; i <= high; i++) {
				ret.add(new AdditionProblem(n, i));
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		ProblemPage p = new ProblemPage();
		System.out.println(p.print(true));
	}
}
