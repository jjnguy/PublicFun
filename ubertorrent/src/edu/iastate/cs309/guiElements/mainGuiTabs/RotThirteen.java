package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RotThirteen extends UberTab
{
	private JPanel mainPane;
	private JTextArea text;
	private JButton rotIt;
	private String originalAlphabetUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String originalAlphabetLower = "abcdefghijklmnopqrstuvwxyz";
	private String shiftAlphabetUp;
	private String shiftAlphabetLower;

	public RotThirteen(int shiftAmnt)
	{
		text = new JTextArea("<Enter Text Here>");
		rotIt = new JButton("rot");

		rotIt.addActionListener(rot);
		GridBagConstraints gc = new GridBagConstraints();
		mainPane = new JPanel(new GridBagLayout());
		gc.weightx = gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		mainPane.add(text, gc);
		gc.weightx = gc.weighty = .0;
		gc.fill = GridBagConstraints.VERTICAL;
		mainPane.add(rotIt, gc);
		setLayout(new BorderLayout());
		add(mainPane, BorderLayout.CENTER);

		shiftAlphabetUp = originalAlphabetUpper.substring(shiftAmnt) + originalAlphabetUpper.substring(0, shiftAmnt);
		shiftAlphabetLower = originalAlphabetLower.substring(shiftAmnt) + originalAlphabetLower.substring(0, shiftAmnt);
	}

	public char rotAChar(char inP)
	{
		if (!Character.isLetter(inP))
			return inP;
		if (Character.isUpperCase(inP))
			return shiftAlphabetUp.charAt(originalAlphabetUpper.indexOf(inP));
		return shiftAlphabetLower.charAt(originalAlphabetLower.indexOf(inP));
	}

	private ActionListener rot = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String txt = text.getText();
			String newTxt = "";
			for (int i = 0; i < txt.length(); i++)
			{
				newTxt += rotAChar(txt.charAt(i));
			}
			text.setText(newTxt);
		}
	};

	@Override
	public Icon getTabIcon()
	{
		return null;
	}

	@Override
	public String getTabName()
	{
		return "Rot 13 Tab";
	}

	@Override
	public String getTabToolTipText()
	{
		return "Provides an easy interface for rot 13'ing code";
	}
}
