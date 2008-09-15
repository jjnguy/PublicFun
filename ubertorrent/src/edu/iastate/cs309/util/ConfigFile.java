package edu.iastate.cs309.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import edu.iastate.cs309.guiElements.MainGui;

/**
 * A ConfigFile is a file used to save states from one run to the next
 * 
 * @author Justin
 * 
 */
public class ConfigFile
{
	private List<Pair> listOFPropValPairs;

	/**
	 * Creates an empty config file
	 */
	public ConfigFile()
	{
		listOFPropValPairs = new ArrayList<Pair>();
	}

	/**
	 * Constructs a config file with the given file
	 * 
	 * @param in
	 *            the file to be constructed
	 * @throws FileNotFoundException
	 *             if in cannot be resolved
	 */
	public ConfigFile(File in) throws FileNotFoundException
	{
		readFile(in);
	}

	/**
	 * Reads a file into the config file
	 * 
	 * @param in
	 *            the file to be read
	 * @throws FileNotFoundException
	 */
	public void readFile(File in) throws FileNotFoundException
	{
		listOFPropValPairs = new ArrayList<Pair>();

		Scanner fIn = new Scanner(in);
		while (fIn.hasNextLine())
		{
			String curLine = fIn.nextLine().trim();
			curLine = curLine.substring(0, curLine.indexOf("//") == -1 ? curLine.length() : curLine.indexOf("//"));
			if (curLine.length() == 0)
				continue;
			if (!curLine.contains(" = "))
				continue;
			Pair toAdd = new Pair();
			toAdd.one = curLine.substring(0, curLine.indexOf(" = "));
			List<String> toAdd2 = new ArrayList<String>();
			String[] toAddArr = curLine.substring(curLine.indexOf(" = ") + 2).trim().split(",");
			for (int i = 0; i < toAddArr.length; i++)
			{
				toAdd2.add(toAddArr[i]);
			}
			toAdd.two = toAdd2;
			listOFPropValPairs.add(toAdd);
		}
		fIn.close();
	}

	/**
	 * Writes a config file given the current state of this object
	 * 
	 * @param outP
	 *            The file to be written to
	 * @throws FileNotFoundException
	 */
	public void writeFile(File outP) throws FileNotFoundException
	{
		PrintStream out = new PrintStream(outP);
		for (Iterator<Pair> iter = listOFPropValPairs.iterator(); iter.hasNext();)
		{
			Pair p = iter.next();
			out.print(p.one + " = ");
			for (Iterator<String> iter2 = p.two.iterator(); iter2.hasNext();)
			{
				String type = iter2.next();
				out.print(type);
				if (iter2.hasNext())
					out.print(",");
			}
			out.println();
		}
		out.close();
	}

	/**
	 * Gets the value from the supplied property name
	 * 
	 * @param propName
	 *            the property to search for
	 * @return the value. null if the property was not found
	 */
	public String getValueByNameString(String propName)
	{
		for (Iterator<Pair> iter = listOFPropValPairs.iterator(); iter.hasNext();)
		{
			Pair elm = iter.next();
			if (elm.one.equals(propName))
				return elm.two.get(0);
		}
		return null;
	}

	/**
	 * Gets the value from the supplied property name
	 * 
	 * @param propName
	 *            the property to search for
	 * @return the value. null if the property was not found
	 */
	public List<String> getValueByName(String propName)
	{
		for (Iterator<Pair> iter = listOFPropValPairs.iterator(); iter.hasNext();)
		{
			Pair elm = iter.next();
			if (elm.one.equals(propName))
				return elm.two;
		}
		return null;
	}

	/**
	 * Sets the property name to the given value
	 * 
	 * @param name
	 *            the property to set
	 * @param values
	 *            the new value of the property
	 */
	public void setProperty(String name, List<String> values)
	{
		Pair toAdd = new Pair(name, values);
		for (int i = 0; i < listOFPropValPairs.size(); i++)
		{
			if (listOFPropValPairs.get(i).equals(toAdd))
			{
				listOFPropValPairs.set(i, toAdd);
				return;
			}
		}
		listOFPropValPairs.add(toAdd);
	}

	/**
	 * Sets the property name to the given value
	 * 
	 * @param name
	 *            the property to set
	 * @param value
	 *            the new value of the property
	 */
	public void setProperty(String name, String value)
	{
		List<String> val = new ArrayList<String>(1);
		val.add(value);
		setProperty(name, val);
	}

	private class Pair
	{
		private String one;
		private List<String> two;

		private Pair(String str, List<String> l)
		{
			one = str;
			two = l;
		}

		private Pair()
		{
		}

		/**
		 * Two pairs are completely equal if their one's are equal as well as
		 * their twos
		 * 
		 * @param other
		 *            the object to test equality
		 * @return true if equal, false otherwise
		 */
		public boolean equalsNameAndVal(Object other)
		{
			if (!equals(other))
				return false;
			Pair otherP = (Pair) other;
			return two.equals(otherP.two);
		}

		/**
		 * Two pairs are equal if their one's are equal
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			//TODO Justin Explain the meaning of the "javadoc" above.

			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj.getClass().equals(this.getClass())))
				return false;
			Pair objP = (Pair) obj;
			return one.equals(objP.one);
		}
	}

	/**
	 * Writes a default Client config file
	 * 
	 * @throws FileNotFoundException
	 */
	public static void writeDefaultConfigFile() throws FileNotFoundException
	{
		PrintStream out = null;
		try
		{
			out = new PrintStream(new File(MainGui.getConfigFileLocation()));
		}
		catch (FileNotFoundException e)
		{
			return;
		}
		out.println("//Auto Generated config file for Uebertorrent's main Graphical user interface");
		out.println("//This file allows for comments only on its own line, beginning with \'//\'");
		out.println("savePword = false");
		out.println("currentlyDisplayedTab = 0");
		out.println("saveFilesLocation = downloads");
		out.close();
	}
}
