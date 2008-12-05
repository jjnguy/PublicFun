package TurtleInterpreter;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
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

		Executable next = new CodeBlock(filein, 1);
		filein.close();
		return next;
	}

	public static void main(String[] args) throws FileNotFoundException {
		LanguageParser p = new LanguageParser(new File("firstprogram.tur"));
		Executable ex = p.parseFile();
		ex.equals(null);
	}
}
