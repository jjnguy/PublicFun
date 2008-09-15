package edu.iastate.cs309.plugins;

import java.util.Hashtable;

/**
 * NOTE: This was used for expediency
 * 
 * A simple test class loader capable of loading from multiple sources, such as
 * local files or a URL. Must be subclassed and the abstract method
 * loadClassBytes() implemented to provide the preferred source.
 * 
 * This class is derived from an article by Chuck McManis
 * http://www.javaworld.com/javaworld/jw-10-1996/indepth.src.html with large
 * modifications.
 * 
 * @author Jack Harich - 8/18/97
 */
public abstract class MyClassLoader extends ClassLoader
{

	//---------- Fields --------------------------------------
	/**
	 * the classes
	 */
	@SuppressWarnings("unchecked")
	private Hashtable classes = new Hashtable();
	private char classNameReplacementChar;

	//---------- Initialization ------------------------------
	/**
	 * Init
	 */
	public MyClassLoader()
	{
	}

	/**
	 * This is a simple version for external clients since they will always want
	 * the class resolved before it is returned to them.
	 */
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException
	{
		return (loadClass(className, true));
	}

	/**
	 * @param className
	 * @param resolveIt
	 * @return the class
	 * @throws ClassNotFoundException
	 * 
	 */
	@SuppressWarnings( { "unchecked" })
	@Override
	public synchronized Class<?> loadClass(String className, boolean resolveIt) throws ClassNotFoundException
	{

		Class<?> result;
		byte[] classBytes;

		//----- Check our local cache of classes
		result = (Class<?>) classes.get(className);
		if (result != null)
			return result;

		//----- Check with the primordial class loader
		try
		{
			result = super.findSystemClass(className);
			return result;
		}
		catch (ClassNotFoundException e)
		{
		}

		//----- Try to load it from preferred source
		// Note loadClassBytes() is an abstract method
		classBytes = loadClassBytes(className);
		if (classBytes == null)
			throw new ClassNotFoundException();

		//----- Define it (parse the class file)
		result = defineClass(className, classBytes, 0, classBytes.length);
		if (result == null)
			throw new ClassFormatError();

		//----- Resolve if necessary
		if (resolveIt)
			resolveClass(result);

		// Done
		classes.put(className, result);
		return result;
	}

	/**
	 * This optional call allows a class name such as "COM.test.Hello" to be
	 * changed to "COM_test_Hello", which is useful for storing classes from
	 * different packages in the same retrival directory. In the above example
	 * the char would be '_'.
	 * 
	 * @param replacement
	 */
	public void setClassNameReplacementChar(char replacement)
	{
		classNameReplacementChar = replacement;
	}

	/**
	 * @param className
	 * @return a byte array
	 * 
	 */
	protected abstract byte[] loadClassBytes(String className);

	/**
	 * 
	 * @param className
	 * @return the string on the formatClass
	 */
	protected String formatClassName(String className)
	{
		if (classNameReplacementChar == '\u0000')
			// '/' is used to map the package to the path
			return className.replace('.', '/') + ".class";
		else
			// Replace '.' with custom char, such as '_'
			return className.replace('.', classNameReplacementChar) + ".class";
	}

}
