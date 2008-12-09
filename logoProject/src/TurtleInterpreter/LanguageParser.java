package TurtleInterpreter;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import executables.CodeBlock;
import executables.Executable;
import executables.Statement;

import turtledisplay.TurtlePane;

public class LanguageParser {

	private Scanner filein;

	public LanguageParser(File file) throws FileNotFoundException {
		filein = new Scanner(file);
	}

	public Executable parseFile() {
		String line = "";

		while (!line.equals("start;")) {
			if (!filein.hasNextLine())
				throw new IllegalStatementException("The file contained no start point");
			line = filein.nextLine();
		}

		Executable next = new CodeBlock(filein, 1, new HashMap<String, CodeBlock>());
		filein.close();
		return next;
	}
}
