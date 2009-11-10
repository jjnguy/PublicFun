package hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AccessibleNumber {

	private static String[] MAGNITUDE_WORDS = { "thousand", "million", "billion" };
	private static String[] DIGIT_WORDS = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
			"nine" };
	private static String[] TENS_WORDS = { "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty",
			"ninety" };
	private static String[] TEENS_WORDS = { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
			"seventeen", "eighteen", "nineteen" };

	private int num;
	private String wrdRep;
	private ShortAudio soundRep;

	public AccessibleNumber(int num) {
		this.num = num;
	}

	public String getText() {
		if (wrdRep != null)
			return wrdRep;
		int numRemaining = num;
		String bildr = "";
		int magCount = -1;
		while (numRemaining > 0) {
			int lastThree = numRemaining % 1000;
			numRemaining /= 1000;
			String hundredStr = getHundredWords(lastThree).trim();
			bildr = hundredStr + (magCount >= 0 && !hundredStr.isEmpty() ? " " + MAGNITUDE_WORDS[magCount] + " " : " ")
					+ bildr;
			magCount++;
		}

		wrdRep = bildr.replaceAll("\\s+", " ");
		return wrdRep;
	}

	private static String getHundredWords(int num) {
		if (num > 999 || num < 0)
			throw new IllegalArgumentException("Cannot get hundred word of a number not in the range 0 <= n <= 999");
		String ret = "";
		if (num > 99) {
			ret += DIGIT_WORDS[num / 100] + " hundred ";
			num %= 100;
		}
		if (num < 20 && num > 9) {
			ret += TEENS_WORDS[num % 10];
		} else if (num < 10 && num > 0) {
			ret += DIGIT_WORDS[num];
		} else if (num != 0) {
			ret += TENS_WORDS[num / 10 - 1];
			if (num % 10 != 0) {
				ret += " " + DIGIT_WORDS[num % 10];
			}
		}
		return ret;
	}

	public ShortAudio getAudio(String soundFiles) throws IOException {
		File files = new File(soundFiles);
		if (!files.exists())
			throw new FileNotFoundException();
		String[] theText = getText().split(" ");
		ShortAudio ret = new ShortAudio(soundFiles + "/" + theText[0] + ".wav");
		for (int i = 1; i < theText.length; i++){
			ret.append(new ShortAudio(soundFiles + "/" + theText[i] + ".wav"));
		}
		return ret;
	}

	@Override
	public String toString() {
		return getText();
	}

}
