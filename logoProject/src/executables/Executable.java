package executables;
import java.util.Iterator;
import java.util.List;

import TurtleInterpreter.TurtleOrientation;


public interface Executable {
		
	// TODO make executable iterable so that we can loop through all the 
	// executables in an executable and apply the changes to a 
	// tutle orientation
	public TurtleOrientation execute(TurtleOrientation originalOrientation);
	
	public boolean hasNextStatement();
	
	public List<TurtleOrientation> executeFully(TurtleOrientation originalOrientation);
}
