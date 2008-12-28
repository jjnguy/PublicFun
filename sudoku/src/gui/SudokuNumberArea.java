package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SudokuNumberArea extends JTextField {
	public SudokuNumberArea() {
		super();
		setPreferredSize(new Dimension(30, 30));
		setFont(new Font("Sans", Font.BOLD, 24));
	}

	public SudokuNumberArea(int number) {
		this();
		setText(number + "");
		lock();
	}

	public void lock() {
		setEditable(false);
		setForeground(Color.BLACK);
		setBackground(new Color(238, 238, 238));
	}
}
