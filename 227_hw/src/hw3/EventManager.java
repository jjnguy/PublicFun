package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * A utility for managing a list of recurring events.
 */
public class EventManager {

	private Map<Date, ArrayList<Event>> events;

	/**
	 * Create a manager for the events found in the specified file. The file is formatted with an event on each line,
	 * with year, month, day, and a textual description of the event. The following is an example file:
	 * 
	 * <pre>
	 * 1732 2 22 George Washington's ### birthday
	 * 1996 2 10 ### anniversary of IBM Deep Blue's defeat of chess master Garry Kasparov
	 * 1919 2 26 ### anniversary of the Grand Canyon's national park designation
	 * </pre>
	 * 
	 * The <code>###</code>s are special fields that can be replaced with a value indicating how many years the event
	 * has been celebrated or acknowledged. The <code>EventManager</code> constructor itself does not alter these
	 * fields.
	 * 
	 * @param datesFile
	 *            File containing events.
	 * 
	 * @throws FileNotFoundException
	 */
	public EventManager(File datesFile) throws FileNotFoundException {
		events = new HashMap<Date, ArrayList<Event>>();
		Calendar c = Calendar.getInstance();
		String[] lines = new Scanner(datesFile).useDelimiter("\\Z").next().split("\n");
		for (String s : lines) {
			String[] pieces = s.split(" ", 4);
			int year = Integer.parseInt(pieces[0]);
			int month = Integer.parseInt(pieces[1]);
			int day = Integer.parseInt(pieces[2]);
			String desc = pieces[3];
			c.set(year, month, day);
			Event e = new Event(year, month, day, desc);
			Date d = c.getTime();
			if (events.containsKey(d)) {
				events.get(d).add(e);
			} else {
				ArrayList<Event> toAdd = new ArrayList<Event>();
				toAdd.add(e);
				events.put(d, toAdd);
			}
		}
	}

	/**
	 * Get a list of descriptions for the events occurring on the specified month and day -- but only if they have
	 * occurred by the specified year. For instance, America's first Independence Day was in 1776. If <code>year</code>
	 * is 1775, such an event is not included in the returned list. If an event's description field contains a
	 * <code>###</code>, the description in the returned list contains instead numeric text indicating how many years
	 * the event has occurred. If <code>month</code> is 7, <code>day</code> is 4, <code>year</code> is 1776, and the
	 * event description is "America's ### birthday", the description in the returned list is "America's 0th birthday".
	 * For year 1777, the returned description is "America's 1st birthday". And so on.
	 * 
	 * @param year
	 *            The year matching events must occur on or before.
	 * 
	 * @param month
	 *            The month matching events must occur in.
	 * 
	 * @param day
	 *            The day matching events must occur on.
	 * 
	 * @return A list of descriptions for matching events.
	 */
	public ArrayList<String> getEventsObservedOn(int year, int month, int day) {
		ArrayList<String> ret = new ArrayList<String>();
		for (Entry<Date, ArrayList<Event>> entry : events.entrySet()) {
			for (Event e: entry.getValue()){
				if (e.isObservedOn(year, month, day))
					ret.add(e.getDescription(year));
			}
		}
		return ret;
	}
}
