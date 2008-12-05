package turtledisplay;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import TurtleInterpreter.LanguageParser;

public class TurtleFrame extends JFrame {

	private TurtlePane pane;
	private JTextArea textPane;

	public TurtleFrame(TurtlePane p) {
		pane = p;
		JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		main.add(pane);
		textPane = new JTextArea();
		textPane.setFont(new Font("Courier New", Font.PLAIN, 12));
		textPane.setPreferredSize(pane.getPreferredSize());
		main.add(textPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(main);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) throws FileNotFoundException {
		LanguageParser parse = new LanguageParser(new File("firstprogram.tur"));
		TurtlePane p = new TurtlePane(new Dimension(500, 500), parse.parseFile());
		new TurtleFrame(p);
	}

}
