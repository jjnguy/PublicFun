package edu.cs319.client.customcomponents;

import javax.swing.JList;

import edu.cs319.dataobjects.DocumentSubSection;

public class SubSectionList extends JList {

	private JFlexibleListModel<DocumentSubSection> model;
	
	public SubSectionList(){
		model = new JFlexibleListModel<DocumentSubSection>();
	}
	
}

