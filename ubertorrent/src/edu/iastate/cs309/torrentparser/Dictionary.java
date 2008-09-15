/**
 * 
 */
package edu.iastate.cs309.torrentparser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.iastate.cs309.util.Util;

/**
 * @author Michael Seibert
 */
public class Dictionary extends BEncodedObject
{
	private Hashtable<ByteString, BEncodedObject> dict = new Hashtable<ByteString, BEncodedObject>();

	/**
	 * Get the value of a key.
	 * 
	 * @param key
	 *            Key to get the value of
	 * @return The value for the given key
	 * @see java.util.Map#get(Object)
	 */
	public BEncodedObject get(ByteString key)
	{
		return dict.get(key);
	}

	/**
	 * Convenience method to go from {@link String}->value
	 * 
	 * @param key
	 *            String to get the value of
	 * @return The value for the given key
	 * @see java.util.Map#get(Object)
	 */
	public BEncodedObject get(String key)
	{
		// This is what happens when you try to make strings be unicode...
		return dict.get(new ByteString(key));
	}

	/**
	 * Add a key into this {@link Dictionary}
	 * 
	 * @param key
	 *            Key to add
	 * @param val
	 *            Value for the key
	 */
	public void put(ByteString key, BEncodedObject val)
	{
		dict.put(key, val);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dict == null) ? 0 : dict.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Dictionary other = (Dictionary) obj;
		if (dict == null)
		{
			if (other.dict != null)
				return false;
		}
		else if (!dict.equals(other.dict))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder retVal = new StringBuilder();
		retVal.append("{ ");
		for (Map.Entry<ByteString, BEncodedObject> entry : dict.entrySet())
		{
			retVal.append(entry.getKey());
			retVal.append(" => ");
			retVal.append(entry.getValue());
			retVal.append(", ");
		}
		if (retVal.length() != 2)
			retVal.replace(retVal.length() - 2, retVal.length(), " }");
		else
			retVal.append("}");
		return retVal.toString();
	}

	/** ********** methods for easy manipulating in TorrentFile ******** */
	// TODO: De-ugly this stuff
	public long getInt(String name) throws ParseException
	{
		BEncodedObject intObj = this.get(name);
		if (intObj == null)
		{
			if (Util.DEBUG)
			{
				System.err.println("Couldn't find " + name + " in the dictionary.");
				return 0;
			}
		}
		else if (!(intObj instanceof BInteger))
		{
			if (Util.DEBUG)
			{
				System.out.println(name + " does not have integer data!");
				return 0;
			}
		}
		return ((BInteger) intObj).get();
	}

	public String getString(String name) throws ParseException
	{
		BEncodedObject stringObj = this.get(name);
		if (stringObj == null)
			throw new ParseException("Couldn't find " + name + " in the dictionary.");
		if (!(stringObj instanceof ByteString))
			throw new ParseException(name + " does not have String data!");

		return ((ByteString) stringObj).toString();
	}

	public Dictionary getDictionary(String name) throws ParseException
	{
		BEncodedObject dictObj = this.get(name);
		if (dictObj == null)
			throw new ParseException("Couldn't find " + name + " in the dictionary.");
		if (!(dictObj instanceof Dictionary))
			throw new ParseException(name + " is not a dictionary!");

		return (Dictionary) dictObj;
	}

	public byte[] getBytes(String name) throws ParseException
	{
		BEncodedObject byteObj = this.get(name);
		if (byteObj == null)
			throw new ParseException("Couldn't find " + name + " in the dictionary.");
		if (!(byteObj instanceof ByteString))
			throw new ParseException(name + " is not a byte string!");

		return ((ByteString) byteObj).get();
	}

	public BEncodedObject[] getList(String name) throws ParseException
	{
		BEncodedObject byteObj = this.get(name);
		if (byteObj == null)
			throw new ParseException("Couldn't find " + name + " in the dictionary.");
		if (!(byteObj instanceof BList))
			throw new ParseException(name + " is not a list!");

		return ((BList) byteObj).get();
	}

	public boolean hasValue(String name)
	{
		BEncodedObject temp = this.get(name);
		return (temp != null);
	}

	/**
	 * @see edu.iastate.cs309.torrentparser.BEncodedObject#bEncode(java.io.OutputStream)
	 */
	@Override
	public void bEncode(OutputStream target) throws IOException
	{
		// List of pairs in the dictionary
		Set<Entry<ByteString, BEncodedObject>> entries = dict.entrySet();

		// Convert set to list
		List<Entry<ByteString, BEncodedObject>> entryList;
		entryList = new LinkedList<Entry<ByteString, BEncodedObject>>(entries);

		// Sort them as per the BitTorrent spec
		Collections.sort(entryList, new Comparator<Entry<ByteString, BEncodedObject>>()
		{
			@Override
			public int compare(Entry<ByteString, BEncodedObject> o1, Entry<ByteString, BEncodedObject> o2)
			{
				return byteStringCompare(o1.getKey(), o2.getKey());
			}
		});

		// Write out the dictionary
		target.write('d');
		for (Iterator<Entry<ByteString, BEncodedObject>> iter = entryList.iterator(); iter.hasNext();)
		{
			Entry<ByteString, BEncodedObject> entry = iter.next();
			entry.getKey().bEncode(target);
			entry.getValue().bEncode(target);
		}
		target.write('e');
	}

	/**
	 * Compare ByteStrings lexicographically
	 * 
	 * @param s1
	 *            First byte string to compare
	 * @param s2
	 *            Second byte string to compare
	 * @return An integer representing the order of the two {@link ByteString}s
	 * @see Comparator#compare(Object, Object)
	 */
	private int byteStringCompare(ByteString s1, ByteString s2)
	{
		byte[] arr1 = s1.get();
		byte[] arr2 = s2.get();

		for (int i = 0; i < arr1.length; i++)
		{
			// Second string is a prefix of first
			if (i >= arr2.length)
				return 1;

			// Strings are different, return the difference
			if (arr1[i] != arr2[i])
				return arr1[i] - arr2[i];
		}

		// First string is a prefix of second
		if (arr1.length != arr2.length)
			return -1;

		// Strings are equal
		return 0;
	}
}
