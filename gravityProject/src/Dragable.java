import java.awt.event.MouseEvent;

public interface Dragable {
	
	public void grabedOnto(MouseEvent e);

	public void letGo();
}
