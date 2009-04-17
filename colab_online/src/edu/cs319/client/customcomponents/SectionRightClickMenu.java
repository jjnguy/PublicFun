package edu.cs319.client.customcomponents;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class SectionRightClickMenu extends JMenu {

	private JMenuItem aquireLockItem;
	private JMenuItem releaseLockItem;
	private JMenuItem newSubSectionItem;
	
	public SectionRightClickMenu(){
		super();
		aquireLockItem = new JMenuItem("Aquire Lock");
		releaseLockItem = new JMenuItem("Release Lock");
		newSubSectionItem = new JMenuItem("Add New SubSection");
	}
	
}
