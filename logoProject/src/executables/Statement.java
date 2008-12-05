package executables;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import TurtleInterpreter.IllegalStatementException;
import TurtleInterpreter.TurtleOrientation;

public class Statement implements Executable {
	private String command;
	private int value;
	private boolean executed;

	public Statement(String statement) {
		executed = false;
		statement = statement.trim().toLowerCase();
		if (statement.startsWith("//"))
			return;
		if (statement.contains("//"))
			statement = statement.substring(0, statement.indexOf("//"));
		if (statement.contains(";") && !statement.endsWith(";"))
			throw new IllegalStatementException(
					"The statement doesn't end in a semi-colon");

		if (!statement
				.matches("(move|rotateclockwise|rotatecounterclockwise) +\\d+;")) {
			throw new IllegalStatementException(
					"The statement was not in the propper form: '" + statement
							+ "'");
		}
		String[] split = statement.split(" +");
		command = split[0].trim();
		value = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
	}

	public Statement(String command, int value) {
		this.command = command;
		this.value = value;
	}

	public TurtleOrientation execute(TurtleOrientation o) {
		TurtleOrientation ret = o.copy();

		if (command.equals("move")) {
			Point result = executeMove(o);
			ret.x = result.x;
			ret.y = result.y;
		} else if (command.startsWith("rotate")) {
			ret.angle = executeRotate(o.angle);
		}
		executed = true;
		return ret;
	}

	private Point executeMove(TurtleOrientation o) {
		Point ret = new Point(o.x, o.y);
		int xDir = (int) (Math.cos(degToRad(o.angle)) * value);
		int yDir = (int) (Math.sin(degToRad(o.angle)) * value);
		ret.x += xDir;
		ret.y += yDir;
		return ret;
	}

	private int executeRotate(int originalAngle) {
		int ret;
		if (!command.contains("counter")) {
			ret = originalAngle - value;
			if (ret < 0)
				ret += 360;
		} else {
			ret = originalAngle + value;
			if (ret <= 360)
				ret -= 360;
		}
		return ret;
	}

	public static double degToRad(int degree) {
		return degree * Math.PI / 180.0;
	}

	@Override
	public boolean hasNextStatement() {
		return !executed;
	}

	@Override
	public List<TurtleOrientation> executeFully(
			TurtleOrientation originalOrientation) {
		List<TurtleOrientation> ret = new ArrayList<TurtleOrientation>();
		ret.add(this.execute(originalOrientation));
		return ret;
	}
	
	public String toString(){
		return command + " " + value;
	}
}
