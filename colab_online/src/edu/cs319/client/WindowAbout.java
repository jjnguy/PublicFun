package edu.cs319.client;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class WindowAbout extends JDialog {

	public WindowAbout(JFrame parent) {
		super(parent, "About CoLab");
		setLocation(new Point(parent.getLocation().x + 50, parent.getLocation().y + 50));
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.setSize(300, 220);
		this.setResizable(false);
		setUpAppearance();
	}

	private void setUpAppearance() {
		JTextArea text = new JTextArea();
		text.setSize(280, 200);
		text.setEditable(false);
		text.setText("CoLab was brought to you by the letter C and \nby The Squirrels for " +
				"their Computer Science 319 \nSemester Project. \n\nPlease Enjoy.");
		text.append("\n\nIan Dallas\nAmelia Gee\nJustin Nelson\nWayne Rowcliffe");
		Insets borderInsets = new Insets(5, 5, 5, 5);
		text.setBorder(new EmptyBorder(borderInsets));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.add(text, BorderLayout.CENTER);
		add(mainPanel);
	}

	public static void showAboutWindow(JFrame parent) {
		WindowAbout about = new WindowAbout(parent);
		about.setVisible(true);
	}
}
