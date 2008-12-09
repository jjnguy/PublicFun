package executables;
import java.util.List;

import TurtleInterpreter.TurtleOrientation;


public interface Executable {
	
	public TurtleOrientation execute(TurtleOrientation originalOrientation);
	
	public boolean hasNextStatement();
	
	public List<TurtleOrientation> executeFully(TurtleOrientation originalOrientation);
	
	public void resetExecution();
}
