package edu.iastate.cs309.plugins;

import java.io.File;
import java.io.FileInputStream;

import edu.iastate.cs309.util.Util;

/**
 * NOTE: This was used for expediency
 * 
 * Loads class bytes from a file.
 * 
 * @author Jack Harich - 8/30/97
 * @author kc0dhb
 */
public class FileClassLoader extends MyClassLoader
{

	//---------- Private Fields ------------------------------
	private String filePrefix;

	//---------- Initialization ------------------------------
	/**
	 * Attempts to load from a local file using the relative "filePrefix", ie
	 * starting at the current directory. For example
	 * 
	 * @param filePrefix
	 *            could be "webSiteClasses\\site1\\".
	 */
	public FileClassLoader(String filePrefix)
	{
		this.filePrefix = filePrefix;
	}

	//---------- Abstract Implementation ---------------------
	/**
	 * Override
	 */
	@Override
	protected byte[] loadClassBytes(String className)
	{

		className = formatClassName(className);
		byte result[];
		String fileName = filePrefix + File.separator + className;
		try
		{
			FileInputStream inStream = new FileInputStream(fileName);
			// *** Is available() reliable for large files?
			result = new byte[inStream.available()];
			inStream.read(result);
			inStream.close();
			return result;

		}
		catch (Exception e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
			// If we caught an exception, either the class
			// wasn't found or it was unreadable by our process.
			return null;
		}
	}
}
