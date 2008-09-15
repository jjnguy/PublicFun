import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JTable;

public class HighScoreList {

	private String[] easyNames;
	private int[] easyTimes;
	private String[] intermediateNames;
	private int[] intermediateTimes;
	private String[] expertNames;
	private int[] expertTimes;

	public HighScoreList(File highScoreList) {
		easyNames = new String[10];
		easyTimes = new int[10];
		intermediateNames = new String[10];
		intermediateTimes = new int[10];
		expertNames = new String[10];
		expertTimes = new int[10];

		Scanner fIn = null;
		try {
			fIn = new Scanner(highScoreList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		line = fIn.nextLine(); // Throw away the lable
		for (int i = 0; i < 10; i++) {
			line = fIn.nextLine();
			easyNames[i] = line.substring(line.indexOf('[') + 1, line
					.indexOf(']'));
			easyTimes[i] = Integer.parseInt(line.substring(
					line.indexOf(']') + 1).trim());
		}
		line = fIn.nextLine(); // Throw away the lable
		for (int i = 0; i < 10; i++) {
			line = fIn.nextLine();
			intermediateNames[i] = line.substring(line.indexOf('[') + 1, line
					.indexOf(']'));
			intermediateTimes[i] = Integer.parseInt(line.substring(
					line.indexOf(']') + 1).trim());
		}
		line = fIn.nextLine(); // Throw away the lable
		for (int i = 0; i < 10; i++) {
			line = fIn.nextLine();
			expertNames[i] = line.substring(line.indexOf('[') + 1, line
					.indexOf(']'));
			expertTimes[i] = Integer.parseInt(line.substring(
					line.indexOf(']') + 1).trim());
		}
	}

	public void writeToFile(File file) {
		PrintStream out = null;
		try {
			out = new PrintStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		out.println("EASY");
		for (int i = 0; i < 10; i++) {
			line = i + 1 + "[" + easyNames[i] + "]" + " " + easyTimes[i];
			out.println(line);
		}
		out.println("INTERMEDIATE");
		for (int i = 0; i < 10; i++) {
			line = i + 1 + "[" + intermediateNames[i] + "]" + " "
					+ intermediateTimes[i];
			out.println(line);
		}
		out.println("EXPERT");
		for (int i = 0; i < 10; i++) {
			line = i + 1 + "[" + expertNames[i] + "]" + " " + expertTimes[i];
			out.println(line);
		}
	}

	/**
	 * 
	 * @param name
	 * @param score
	 * @param d
	 * @return true if a score was updated, false otherwise
	 */
	public boolean addHighScore(String name, int score, Difficulty d) {
		int replacePos = -1;
		String[] replaceNameArray;
		int[] replaceScoreArray;
		if (d.equals(Difficulty.EASY)) {
			replaceNameArray = easyNames;
			replaceScoreArray = easyTimes;
		} else if (d.equals(Difficulty.INTERMEDIATE)) {
			replaceNameArray = intermediateNames;
			replaceScoreArray = intermediateTimes;
		} else if (d.equals(Difficulty.EXPERT)) {
			replaceNameArray = expertNames;
			replaceScoreArray = expertTimes;
		} else {
			return false;
		}
		for (int i = 0; i < replaceScoreArray.length; i++) {
			if (replaceScoreArray[i] > score) {
				replacePos = i;
				break;
			}
		}
		if (replacePos == -1)
			return false;

		for (int i = replaceNameArray.length - 1; i > replacePos; i--) {
			replaceScoreArray[i] = replaceScoreArray[i - 1];
			replaceNameArray[i] = replaceNameArray[i - 1];
		}

		replaceNameArray[replacePos] = name;
		replaceScoreArray[replacePos] = score;

		return true;
	}

	public boolean isHighScore(int score, Difficulty d) {
		if (d.equals(Difficulty.EASY)) {
			return score < easyTimes[9];
		} else if (d.equals(Difficulty.INTERMEDIATE)) {
			return score < intermediateTimes[9];
		} else if (d.equals(Difficulty.EXPERT)) {
			return score < expertTimes[9];
		}
		return false;
	}

	public String getTable() {
		StringBuilder ret = new StringBuilder();
		ret.append("EASY" + '\n');
		for (int i = 0; i < easyTimes.length; i++) {
			String line = String.format("%2d %30s \t%3d", i + 1, easyNames[i],
					easyTimes[i]);
			ret.append(line + '\n');
		}
		ret.append("INTERMEDIATE" + '\n');
		for (int i = 0; i < intermediateTimes.length; i++) {
			String line = String.format("%2d %30s \t%3d", i + 1,
					intermediateNames[i], intermediateTimes[i]);
			ret.append(line + '\n');
		}
		ret.append("EXPERT" + '\n');
		for (int i = 0; i < expertTimes.length; i++) {
			String line = String.format("%2d %30s \t%3d", i + 1, expertNames[i],
					expertTimes[i]);
			ret.append(line + '\n');
		}
		return ret.toString();
	}
}
