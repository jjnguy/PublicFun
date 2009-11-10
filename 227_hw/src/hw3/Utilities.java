package hw3;

import java.util.Calendar;

/**
 * A set of static utility methods for dealing with dates.
 */
public class Utilities {

	/**
	 * Get the two-letter suffix that should appear after the specified number. When appended to their corresponding
	 * numbers, these suffices produce the following: 0th, 1st, 2nd, 3rd, 4th, 5th, and so on. The suffix is completely
	 * determined by the rightmost digit of <code>n</code>, except for the cases of 11, 12, 13, all of which have the
	 * suffix "th".
	 * 
	 * @param n
	 *            The number to get the suffix for. Assumed to be positive.
	 * 
	 * @return The two-letter ordinal suffix.
	 */
	public static String getOrdinalSuffix(int n) {
		if (n == 11 || n == 12 || n == 13)
			return "th";
		char lastChar = (n + "").charAt((n + "").length() - 1);
		switch (lastChar) {
		case '1':
			return "st";
		case '2':
			return "nd";
		case '3':
			return "rd";
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case '0':
			return "th";
		default:
			throw new IllegalArgumentException("The suplied number wasn't...");
		}
	}

	/**
	 * Get the number of days in the specified month during the specified year. Februaries in leap years have 29 days.
	 * 
	 * @param year
	 *            Year in which month occurs.
	 * 
	 * @param month
	 *            Month in which to get the number of days. A value of 1 means January, 12 means December.
	 * 
	 * @return The number of days in the specified month.
	 */
	public static int getDaysInMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		return c.getActualMaximum(Calendar.DATE);
	}

	/**
	 * See if the specified year is a leap year or not. A year is a leap year if it's divisible by four -- but not if it
	 * is divisible by 100 -- unless it is divisible by 400. So, leap years include 1996 and 2000, but not 1900.
	 * 
	 * @param year
	 *            Year to check for leapness.
	 * 
	 * @return True if year is a leap year, false otherwise.
	 */
	public static boolean isLeap(int year) {
		Calendar c = Calendar.getInstance();
		c.set(year, 2, 1);
		// if February has 29 days, it is a leap year.
		return c.getActualMaximum(Calendar.DATE) == 29;
	}
}
