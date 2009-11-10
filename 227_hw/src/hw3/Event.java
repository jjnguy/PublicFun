package hw3;

import java.util.Calendar;

/**
 * An event which after occurring on a given year, month, and day, is continually observed in subsequent years on the
 * same month and day.
 */
public class Event {

	private int year, month, day;
	private String desc;

	/**
	 * Create an event which first occurred on the specified year, month, and day.
	 * 
	 * @param year
	 *            Year of first occurrence.
	 * 
	 * @param month
	 *            Month of first occurrence.
	 * 
	 * @param day
	 *            Day of first occurrence.
	 * 
	 * @param description
	 *            Textual description of the event. May contain <code>###</code> fields.
	 */
	public Event(int year, int month, int day, String description) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.desc = description;
	}

	/**
	 * Get the description of the event with a slight twist. The first occurrence of <code>###</code> in the description
	 * is replaced by a numeric descriptor indicating how many times the event has occurred at the time the event is
	 * observed in the specified year. If the specified year is equivalent to the event's starting year, 0th is
	 * substituted. If the specified year is one year after the event's starting year, 1st is substituted. And so on. If
	 * no <code>###</code> exists, then the description is returned as is.
	 * 
	 * @param year
	 *            The year in which the event is to be observed. It is assumed that the year will not be less than the
	 *            event's start date. (If the year does occur before the event, the <code>###</code> will be replaced by
	 *            a negative ordinal value.)
	 * 
	 * @return A description of the event, with an indication of how many times it has been observed.
	 */
	public String getDescription(int year) {
		int yearsPast = getYearsPast(year);
		return desc.replace("###", yearsPast + Utilities.getOrdinalSuffix(yearsPast));
	}

	/**
	 * This doesn't work correctly, needs to be fixed
	 * 
	 */
	private int getYearsPast(int year) {
		Calendar cal = Calendar.getInstance();
		int curYear = cal.get(Calendar.YEAR);
		int curMonth = cal.get(Calendar.MONTH);
		int curDay = cal.get(Calendar.DATE);
		int yearsPast = curYear - this.year;
		if (yearsPast < 0)
			throw new IllegalArgumentException("Hash codes cannot be in Events that haven't happened.");
		return yearsPast;
	}

	/**
	 * Determine if the event is observed on the specified date. An event is observed only if it occurs on the specified
	 * month and day, but not if it has not happened by the specified year. That is, if an event first occurs in 1996
	 * and the specified year is 1995, the event does not occur, no matter the month and day.
	 * 
	 * @param year
	 *            Year of occurrence.
	 * 
	 * @param month
	 *            Month of occurrence.
	 * 
	 * @param day
	 *            Day of occurrence.
	 * 
	 * @return True if the event is observed on this date.
	 */
	public boolean isObservedOn(int year, int month, int day) {
		// if the month or day aren't the same, it isn't observed
		if (month != this.month || day != this.day) return false;
		// if the year supplied is after the event, it isn't observed
		return year > this.year;
	}
}
