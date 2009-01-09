package interpreter;
import java.util.*;

/*
 * Lolcode parser, (c) 2007 Mathew Hall Currently implements: variable assignment, I HAS A
 * looping: IM IN YR LOOP | now supports nesting and non fixed KTHXBYEs printing: VISIBLE
 * break: KTHXBYE comments: BTW relies on ICanHasParser to turn a program into tokens.
 */
public class Lolcode {

	public String program[] = new String[4096];
	public int PC = 0;

	private boolean halt = false;
	private HashMap<String, String> variables = new HashMap<String, String>();
	private Stack<Integer> callStack = new Stack<Integer>();
	private Stack<Integer> returnStack = new Stack<Integer>();
	private int cycles = 0;

	public Lolcode(String[] p) {
		program = p;
	}

	public void run() {
		PC = 0;
		while (!halt) {
			// System.out.println("Processing string: " + program[PC]);
			String cur = program[PC];
			if (cur.compareTo("HAI") == 0 && PC != 0) {
				halt = true;
				System.out.println("Kitty sez: U NO SAI HAI! :(");
			}
			if (cur.compareTo("CAN_HAS_STDIO?") == 0) {

				// do nothing, later add instantiation code for a few objects
				// eg class for stdio, class for db...
				// array of null references, item thats CAN_HASd is instantiated in
				// array and used.
			}
			if (cur.compareTo("I_HAS_A") == 0) {
				variables.put(program[PC + 1], "NOTHING D:");
			}
			if (cur.compareTo("IM_IN_YR_LOOP") == 0) {
				returnStack.push(PC); // +1);
			}
			if (cur.compareTo("IM_OUTTA_YR_LOOP") == 0) {
				PC = returnStack.peek(); // IM OUTTA YR LOOP will actually loop
			}
			if (cur.compareTo("VISIBLE") == 0) {
				// are we printing a string literal or a variable?
				if (program[PC + 1].charAt(0) == '"') {
					int i = 0;
					do {
						// print the next word in the program until one that ends with " is
						// printed
						i++;
						System.out.print(program[PC + i].replace("\"", "") + " ");
					} while (program[PC + i].charAt(program[PC + i].length() - 1) != '"');
					System.out.print("\n");
				} else {
					// otherwise fetch the appropriate variable - add failure code here
					System.out.println(variables.get(program[PC + 1]));
				}
			}
			if (cur.compareTo("KTHXBYE") == 0) {
				if (returnStack.size() != 0) {
					returnStack.pop(); // pop the deepest loop return off
					while (program[++PC].compareTo("IM_OUTTA_YR_LOOP") == 0)
						;
					// the PC will be at the closest loop end now, so increment again.
					// PC++; //skip past the loop statement
					PC--; // PC gets incremented at the end of our cycle, so predecrement it.
				} else {
					halt = true;
					System.out.println("Kitty sez: BAI THX 4 CODIN ME ^_^");
				}

			}
			cycles++;
			PC++;
		}
		stackDump();
	}

	public void stackDump() {
		System.out.println("Return stack:");
		System.out.println(returnStack);
	}

	public void printStats() {
		System.out.println("CPU Status:");
		System.out.println(cycles + " cycles run, PC at " + PC + "(" + program[PC] + ")");
		stackDump();
	}

	public void reset() {
		PC = 0;
	}

	public static void main(String[] args) {
		String st = "HAI\n" + "CAN HAS STDIO?\n" + "I HAS A VAR\n" + "IM IN YR LOOP\n"
				+ "VISIBLE \"SANDWICH\"\n" + "IM IN YR LOOP\n" + "VISIBLE VAR\n" + "KTHXBYE\n"
				+ "IM OUTTA YR LOOP\n" +

				"VISIBLE \"sandwich\"\n" + "VISIBLE \"face\"\n" + "IM OUTTA YR LOOP\n"
				+ "KTHXBYE";
		ICanHasParser p = new ICanHasParser(st);

		Lolcode lc = new Lolcode(p.parse());

		lc.run();
	}
}