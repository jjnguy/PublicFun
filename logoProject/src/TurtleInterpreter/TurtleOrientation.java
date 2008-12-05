package TurtleInterpreter;
public class TurtleOrientation {
	public int angle;
	public int x;
	public int y;

	public TurtleOrientation copy() {
		TurtleOrientation ret = new TurtleOrientation();
		ret.angle = angle;
		ret.x = x;
		ret.y = y;
		return ret;
	}
}
