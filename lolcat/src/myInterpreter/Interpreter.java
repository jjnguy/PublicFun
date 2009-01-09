package myInterpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import classes.LOLVar;

public class Interpreter {
	private static final String TOKENIZER_FILE_LOC = "parsefiles/tokenfile";

	private List<String> keys;
	private List<String> tokens;

	private File tokenizedFile;

	public Interpreter(File toInterpret) {
		loadKeysAndTokens();
		File tokenizedFile = null;
		try {
			tokenizedFile = fixLines(getTokenizedFile(toInterpret));
		} catch (FileNotFoundException e) {
			System.err.println("OOPSIES, I LOST TEH FIL!!");
			return;
		}
		this.tokenizedFile = tokenizedFile;
	}

	public void execute() {
		int curPointer = 0;
		List<String> lines = null;
		Map<String, LOLVar> globalVariables = new HashMap<String, LOLVar>();
		LOLVar IT;
		try {
			lines = getStrippedLinesFromFile(tokenizedFile);
		} catch (FileNotFoundException e) {
			System.err.println("OOPSIES, I LOST TEH FIL!!");
			return;
		}
		if (!lines.get(curPointer).equals("HAI")) {
			System.err.println("OOPSIE, PROGRM MISSIN 'HAI'");
			return;
		}
		curPointer++;

	}

	private static List<String> getStrippedLinesFromFile(File f) throws FileNotFoundException {
		boolean foundHAI = false;
		Scanner fin = new Scanner(f);
		List<String> ret = new ArrayList<String>();
		while (fin.hasNextLine()) {
			String line = fin.nextLine().trim();
			if (line.length() == 0 || line.startsWith("BTW")) {
				continue;
			}
			if (line.contains("CAN_HAS"))
				continue;
			if (line.contains("BTW")) {
				line = line.substring(0, line.indexOf("BTW"));
			}
			if (!foundHAI && line.equals("HAI"))
				foundHAI = true;
			if (foundHAI)
				ret.add(line);
		}
		printListToConsole(ret);
		return ret;
	}

	private void loadKeysAndTokens() {
		keys = new ArrayList<String>();
		tokens = new ArrayList<String>();
		File keyFile = new File(TOKENIZER_FILE_LOC);
		Scanner fIn = null;
		try {
			fIn = new Scanner(keyFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (fIn.hasNextLine()) {
			String[] pair = fIn.nextLine().trim().split("=");
			keys.add(pair[0]);
			tokens.add(pair[1]);
		}
		fIn.close();
	}

	private static String loadFileToString(File f) throws FileNotFoundException {
		Scanner fileIn = null;
		fileIn = new Scanner(f);
		StringBuilder builder = new StringBuilder();
		while (fileIn.hasNextLine()) {
			builder.append(fileIn.nextLine().trim() + "\n");
		}
		return builder.toString();
	}

	private File fixLines(File tokenizedFile) throws FileNotFoundException {
		String entireFile = loadFileToString(tokenizedFile);
		// entireFile = entireFile.replaceAll("(\\.\\.\\.\\n)", "");
		// entireFile = entireFile.replaceAll(",", "\\n");
		return tokenizedFile;
	}

	public File getTokenizedFile(File originalFile) throws FileNotFoundException {
		String entireFile = loadFileToString(originalFile);
		for (int i = 0; i < keys.size(); i++) {
			entireFile = entireFile.replaceAll(keys.get(i), tokens.get(i));
		}
		File ret = new File("." + originalFile.getName() + ".tokenized");
		PrintStream out = new PrintStream(ret);
		out.println(entireFile);
		out.close();
		return ret;
	}

	public static void printListToConsole(List<?> l) {
		for (Object object : l) {
			System.out.println(object);
		}
	}

	public static void printFileToConsole(File f) {
		Scanner fin = null;
		try {
			fin = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("OOPSIES, I LOST TEH FIL!!!!");
			return;
		}
		while (fin.hasNextLine()) {
			System.out.println(fin.nextLine());
		}
		fin.close();
	}

	public static void main(String[] args) {
		String filename = "examples/helloworld.lcd";
		Interpreter i = new Interpreter(new File(filename));
		i.execute();
	}
}
