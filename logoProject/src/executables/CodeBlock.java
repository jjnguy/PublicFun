package executables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import TurtleInterpreter.TurtleOrientation;

public class CodeBlock implements Executable {

	private List<Executable> commands;
	private int exectueCount;
	private boolean hasNext;
	private int curPos;

	public CodeBlock(List<Executable> commands, int repeatCount) {
		this.commands = commands;
		exectueCount = repeatCount;
		curPos = 0;
		hasNext = commands.size() > 0 && repeatCount > 0;
	}

	/**
	 * The Scanner is in the state where it has just read a repeat; or start;
	 * 
	 * @param s
	 */
	public CodeBlock(Scanner s, int repeatAmmnt) {
		exectueCount = repeatAmmnt;
		commands = new ArrayList<Executable>();
		String line = "";
		while (true) {
			line = s.nextLine().trim();
			if (line.trim().equals("end;"))
				break;
			if (line.length() == 0)
				continue;
			if (line.startsWith("//"))
				continue;
			if (line.contains("//"))
				line = line.substring(0, line.indexOf("//")).trim();
			if (line.startsWith("repeat")) {
				line = line.split(" +")[1].trim();
				int repeatCount = Integer.parseInt(line.substring(0, line.length() - 1));
				commands.add(new CodeBlock(s, repeatCount));
			} else {
				commands.add(new Statement(line));
			}
		}
		curPos = 0;
		hasNext = commands.size() > 0 && repeatAmmnt > 0;
	}

	@Override
	public TurtleOrientation execute(TurtleOrientation originalOrientation) {
		TurtleOrientation currentOr = originalOrientation.copy();
		Executable ex = commands.get(curPos % commands.size());
		if (ex.hasNextStatement()) {
			currentOr = ex.execute(originalOrientation);
		} else {
			curPos++;
			ex = commands.get(curPos % commands.size());
			// need to reset in case we have passed by here already
			ex.resetExecution();
			System.out.println(curPos + " " + commands.size() + " " + exectueCount);
			if (curPos >= commands.size() * exectueCount)
				hasNext = false;
			else
				currentOr = ex.execute(originalOrientation);
		}
		return currentOr;
	}

	@Override
	public boolean hasNextStatement() {
		return hasNext;
	}

	@Override
	public List<TurtleOrientation> executeFully(TurtleOrientation originalOrientation) {
		TurtleOrientation currentOr = originalOrientation.copy();
		List<TurtleOrientation> ret = new ArrayList<TurtleOrientation>();
		for (Executable ex : commands) {
			ret.addAll(ex.executeFully(currentOr));
			currentOr = ret.get(ret.size() - 1);
		}
		return ret;
	}

	@Override
	public void resetExecution() {
		curPos = 0;
		hasNext = true;
	}
}
