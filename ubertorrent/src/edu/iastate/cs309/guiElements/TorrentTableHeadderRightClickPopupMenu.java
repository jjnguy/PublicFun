package edu.iastate.cs309.guiElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;

import edu.iastate.cs309.util.Util;

/**
 * This menu facilitates adding and removing columns of data from the table
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class TorrentTableHeadderRightClickPopupMenu extends JPopupMenu
{

	private List<JCheckBox> listOfOptions;
	private TorrentTableModel model;

	/**
	 * @param model
	 *            the current model for the table
	 * 
	 */
	public TorrentTableHeadderRightClickPopupMenu(TorrentTableModel model)
	{
		listOfOptions = new ArrayList<JCheckBox>(model.shownColNames.size());
		this.model = model;
		for (Iterator<String> iter = model.possibleColNames.iterator(); iter.hasNext();)
		{
			String nextName = iter.next();
			JCheckBox toAdd = new JCheckBox(nextName);
			toAdd.addActionListener(clickedOnAnItem);
			listOfOptions.add(toAdd);
			if (model.shownColNames.contains(nextName))
			{
				toAdd.setSelected(true);
			}
			add(toAdd);
		}
	}

	//TODO removing shit and adding it again screws with the orders
	private ActionListener clickedOnAnItem = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JCheckBox cBox = (JCheckBox) e.getSource();
			if (!cBox.isSelected())
				model.hideColumn(cBox.getText());
			else
				model.showColumn(cBox.getText());
			if (Util.DEBUG)
				System.out.println(cBox.getText());
		}
	};
}
