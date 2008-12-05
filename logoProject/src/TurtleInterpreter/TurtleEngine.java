package TurtleInterpreter;

import executables.Executable;

public class TurtleEngine {

	private TurtleOrientation orientation;

	public TurtleEngine(TurtleOrientation beginState) {
		orientation = beginState.copy();
	}

	public void execute(Executable ex) {
		orientation = ex.execute(orientation).copy();
	}

	public int getX() {
		return orientation.x;
	}

	public int getY() {
		return orientation.y;
	}
}
