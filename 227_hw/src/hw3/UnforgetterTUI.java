package hw3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * The textual user interface for the Unforgetter application.
 */
public class UnforgetterTUI {

	private EventManager manager;

	/**
	 * Create a new textual user interface for the events managed by the specified EventsManager.
	 * 
	 * @param manager
	 *            The manager to interact with.
	 */
	public UnforgetterTUI(EventManager manager) {
		this.manager = manager;
	}

	/**
	 * Let the user interact with and query the events. The user is prompted for a command like so:
	 * 
	 * <pre>
	 * Enter quit or YYYY MM DD or YYYY MM:
	 * </pre>
	 * 
	 * If the user enters <code>quit</code>, the method returns without further input. If the user enters a command of
	 * the form <code>YYYY MM DD</code>, the list of events occurring on that month and day are displayed, with the
	 * description of the event modified to describe how many times that event has occurred. For instance, if we have an
	 * event for George Washington's birthday -- February 22, 1732 -- and the user types in <code>2009 2 22</code>, the
	 * following interaction will be seen on the console:
	 * 
	 * <pre>
	 * Enter quit or YYYY MM DD or YYYY MM: 2009 2 22
	 * 2009/02/22: George Washington's 277th birthday
	 * </pre>
	 * 
	 * If the user enters a command of the form <code>YYYY MM</code>, all events for the specified month are displayed:
	 * 
	 * <pre>
	 * Enter quit or YYYY MM DD or YYYY MM: 2009 2
	 * 2009/02/10: 13th anniversary of IBM Deep Blue's defeat of chess master Garry Kasparov
	 * 2009/02/22: George Washington's 277th birthday
	 * 2009/02/26: 90th anniversary of the Grand Canyon's national park designation
	 * </pre>
	 * 
	 * For both the <code>YYYY MM DD</code> and <code>YYYY MM</code> command, entries that have not occurred by the
	 * given <code>YYYY</code> should not be displayed. For example, since Deep Blue defeated Kasparov in 1996, we see
	 * the following interaction if the user enters 1995:
	 * 
	 * <pre>
	 * Enter quit or YYYY MM DD or YYYY MM: 1995 2
	 * 2009/02/22: George Washington's 277th birthday
	 * 2009/02/26: 90th anniversary of the Grand Canyon's national park designation
	 * </pre>
	 * 
	 * If no events are observed in the requested time frame, the message "No events found" is printed. After handling
	 * the user's command, the prompt is redisplayed and a new command is processed.
	 */
	public void listen() {
	
		Scanner stdin = new Scanner(System.in);
		while (true) {
			System.out.print("Enter quit or YYYY MM DD or YYYY MM: ");
			String input = stdin.nextLine().trim();
			if (input.toLowerCase().equals("quit")) {
				break;
			}
			String[] pieces = input.split(" ");
			int year = Integer.parseInt(pieces[0]);
			int month = Integer.parseInt(pieces[1]);
			
			int day = -1;
			if (pieces.length > 2)
				day = Integer.parseInt(pieces[2]);
			List<String> evts = null;
			if (day == -1) {
				evts = new ArrayList<String>();
				for (int i = 1; i < Utilities.getDaysInMonth(year, month); i++){
					evts.addAll(manager.getEventsObservedOn(year, month, i));
				}
			} else {
				evts = manager.getEventsObservedOn(year, month, day);
			}
			if (evts.size() == 0) {
				System.out.println("No Events");
			} else {
				for (String s : evts) {
					System.out.println("\t" + s);
				}
			}
		}
	}
}
