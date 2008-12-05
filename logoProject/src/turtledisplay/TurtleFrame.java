package turtledisplay;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import TurtleInterpreter.LanguageParser;

public class TurtleFrame extends JFrame {
	
	private TurtlePane pane;
	
	public TurtleFrame(TurtlePane p){
		pane = p;
		add(p);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		LanguageParser parse = new LanguageParser(new File("firstprogram.tur"));
		TurtlePane p = new TurtlePane(new Dimension(400,400), parse.parseFile());
		new TurtleFrame(p);
	}
	
}
