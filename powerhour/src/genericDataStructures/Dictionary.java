package genericDataStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Dictionary {

	private List<String> keys;
	private List<Object> values;

	public Dictionary() {
		keys = new ArrayList<String>();
		values = new ArrayList<Object>();
	}

	/**
	 * 
	 * @param s
	 *            The Scanner represents a text file in the iTunes library format. Assumes
	 *            directly following a <dict> tag.
	 */
	public Dictionary(Scanner s) {
		this();
		String line = s.nextLine().trim();
		while (!line.toLowerCase().equals("</dict>")) {
			String key = line.substring(line.indexOf("<key>") + 5, line.indexOf("</key>"));
			keys.add(key);
			if (key.equals("Comments")) { // comments throw off the single line
				// way i parse this
				keys.remove(key);
				while (!line.contains("</string>")) {
					line = s.nextLine();
				}
			} else if (line.endsWith("</key>")) {
				createAndAddLargerData(s);
			} else {
				addSmallerData(line);
			}
			line = s.nextLine().trim();
		}
	}

	/**
	 * Silly method to split up the large constructor
	 * 
	 * @param s
	 */
	private void createAndAddLargerData(Scanner s) {
		String line2 = s.nextLine().trim();
		if (line2.contains("array")) {
			values.add(new Array(s));
		} else if (line2.contains("dict")) {
			values.add(new Dictionary(s));
		} else if (line2.contains("data")) {
			String data = s.nextLine();
			line2 = s.nextLine();
			while (!line2.contains("</data>")) {
				data += line2;
				line2 = s.nextLine();
			}
			values.add(data);
		} else {
			throw new IllegalArgumentException("The elment type " + line2
					+ " is not supported.");
		}
	}

	/**
	 * Silly method to split up the large constructor
	 * 
	 * @param line
	 */
	private void addSmallerData(String line) {
		String valuePart = line.substring(line.indexOf("><") + 2);
		String type = valuePart.substring(0, valuePart.indexOf('>')).toLowerCase();
		if (type.equals("string")) {
			String value = valuePart.substring(valuePart.indexOf('>') + 1, valuePart
					.indexOf('<'));
			values.add(value);
		} else if (type.equals("integer")) {
			String value = valuePart.substring(valuePart.indexOf('>') + 1, valuePart
					.indexOf('<'));
			values.add(Long.parseLong(value));
		} else if (type.equals("date")) {
			// just do a string deal for now
			String value = valuePart.substring(valuePart.indexOf('>') + 1, valuePart
					.indexOf('<'));
			values.add(value);
		} else if (type.equals("true/")) {
			values.add(new Boolean(true));
		} else if (type.equals("false/")) {
			values.add(new Boolean(false));
		} else
			throw new IllegalArgumentException("The elment type " + type
					+ " is not supported.");
	}

	/**
	 * Add an element to the Dictionary
	 * 
	 * @param key
	 * @param value
	 * @return true if the key was already in the Dictionary and the value was replaced and
	 *         false otherwise
	 */
	public boolean add(String key, Object value) {
		int keyIndex = keys.indexOf(key);
		if (keyIndex == -1) {
			keys.add(key);
			values.add(value);
			return false;
		}
		values.set(keyIndex, value);
		return true;
	}

	public Object get(String key) {
		int keyIndex = keys.indexOf(key);
		if (keyIndex == -1) {
			throw new NoSuchElementException("The key '" + key + "' was not found.");
		}
		return values.get(keyIndex);
	}

	public String toString() {
		if (keys.contains("name"))
			return (String) values.get(keys.indexOf("name"));
		if (keys.contains("Name"))
			return (String) values.get(keys.indexOf("Name"));
		else
			return "Dictionary with no name";
	}

	public boolean containsKey(String string) {
		return keys.contains(string);
	}
}
