package edu.cs319.client.customcomponents;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTextArea;

public class WorkingPane extends JTextArea {
	
	@Override
	public void setEditable(boolean b) {
		super.setEditable(b);
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isEditable()) {
			setBackground(Color.LIGHT_GRAY);
		} else {
			setBackground(Color.WHITE);
		}
	}
}
