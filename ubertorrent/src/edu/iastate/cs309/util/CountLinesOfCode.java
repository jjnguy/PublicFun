package edu.iastate.cs309.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Need a break from Uebertorrent
 * 
 * @author Justin
 * 
 */
public class CountLinesOfCode
{
	private CountLinesOfCode()
	{
	}

	public static void main(String[] args) throws IOException
	{
		System.out.println("There are " + (countLines(new File("C:/Documents and Settings/Justin Nelson/workspace/Ueber Torrent/src/edu/iastate/cs309/guiElements/"))));
	}

	public static int countLines(File rootDir) throws IOException
	{
		if (!rootDir.isDirectory())
		{
			if (rootDir.getName().endsWith("java"))
				return countLinesSingleJavaFile(rootDir);
		}
		File[] files = rootDir.listFiles();
		if (files == null)
			return countLinesSingleJavaFile(rootDir);
		int count = 0;
		for (File file : files)
		{
			if (file.isDirectory())
			{
				if (file.getName().equals(".svn"))
					continue;
				count += countLines(file);
			}
			else if (file.getName().endsWith("java"))
				count += countLinesSingleJavaFile(file);
		}
		return count;
	}

	private static int countLinesSingleJavaFile(File file)
	{
		int count = 0;
		boolean isCode = true;
		Scanner scan;
		try
		{
			scan = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
				System.out.println("Thats kinda shitty, huh?");
			}
			return 0;
		}
		String[] filters = new String[] { "import", "//", "/*", "*", "*/" };
		while (scan.hasNextLine())
		{
			String line = scan.nextLine();
			if (line.trim().length() == 0)
				continue;
			for (String string : filters)
			{
				if (line.trim().startsWith(string))
				{
					isCode = false;
					break;
				}
			}
			if (isCode)
				count++;
			isCode = true;
		}
		scan.close();
		return count;
	}
}
