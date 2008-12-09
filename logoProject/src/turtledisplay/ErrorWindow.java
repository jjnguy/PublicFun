package turtledisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorWindow extends JFrame {
	private JTextArea errorDisplay;
	private JButton clear;

	public ErrorWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JPanel main = new JPanel();
		errorDisplay = new JTextArea();
		errorDisplay.setEditable(false);
		JScrollPane scroll = new JScrollPane(errorDisplay);
		clear = new JButton("Clear");
		clear.addActionListener(clearAction);
		main.add(scroll);
		main.add(clear);
		add(main);
		pack();
	}

	public void writeException(Exception e) {
		errorDisplay.append(e.getMessage() + "\n");
		pack();
	}

	private ActionListener clearAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			errorDisplay.setText("");
		}
	};
}
