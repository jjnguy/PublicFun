import java.awt.Point;
import java.awt.event.MouseEvent;

public interface Dragable extends GravityObject {

	public void grabedOnto(MouseEvent e);

	public void letGo();
	
	public boolean isHeld();

	public boolean containsPoint(Point p);
}
