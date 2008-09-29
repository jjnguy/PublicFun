import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class FourierMenuBar extends JMenuBar {

	private MainGraphFrame parent;
	
	public FourierMenuBar(MainGraphFrame parentComp){
		super();
		parent = parentComp;
		JMenu file = new JMenu("File");
			JMenuItem openFile = new JMenuItem("Open");
				openFile.addActionListener(parent);
				openFile.setActionCommand("openFile");
			JMenuItem exit = new JMenuItem("Exit");
				exit.addActionListener(parent);
				exit.setActionCommand("exit");			
		file.add(openFile);
		file.add(exit);
		add(file);
		
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
