import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ChatPanelMenu extends JMenuBar {
	public ChatPanelMenu() {
		JMenu file = new JMenu("File");
		JMenuItem connect = new JMenuItem("Connect");
		JMenuItem host = new JMenuItem("Host");
		file.add(connect);
		file.add(host);
		add(file);
	}
}
