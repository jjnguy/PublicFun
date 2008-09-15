/**
 * 
 */
package edu.iastate.cs309.torrentparser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Michael Seibert
 */
public class BList extends BEncodedObject
{
	private List<BEncodedObject> list = new LinkedList<BEncodedObject>();

	/**
	 * Create a copy of a {@link BList}
	 * 
	 * @param other
	 *            The {@link BList} to copy
	 */
	public BList(BList other)
	{
		list = new LinkedList<BEncodedObject>(other.list);
	}

	/**
	 * Create an empty {@link BList}
	 */
	public BList()
	{
	}

	/**
	 * Add an object to this list.
	 * 
	 * @param e
	 *            Element to add
	 */
	public void add(BEncodedObject e)
	{
		list.add(e);
	}

	/**
	 * Add all the elements from another {@link BList} onto this {@link BList}.
	 * 
	 * @param list
	 *            {@link BList} to add elements from
	 */
	public void addAll(BList list)
	{
		this.list.addAll(list.list);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
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
		final BList other = (BList) obj;
		if (list == null)
		{
			if (other.list != null)
				return false;
		}
		else if (!list.equals(other.list))
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
		retVal.append("[ ");
		for (BEncodedObject entry : list)
		{
			retVal.append(entry);
			retVal.append(", ");
		}
		if (retVal.length() != 2)
			retVal.replace(retVal.length() - 2, retVal.length(), " ]");
		else
			retVal.append("]");
		return retVal.toString();
	}

	/**
	 * Get the elements of this list.
	 * 
	 * @return An array containing the elements of this list.
	 */
	public BEncodedObject[] get()
	{
		return list.toArray(new BEncodedObject[0]);
	}

	/**
	 * @see edu.iastate.cs309.torrentparser.BEncodedObject#bEncode(java.io.OutputStream)
	 */
	@Override
	public void bEncode(OutputStream target) throws IOException
	{
		target.write('l');
		for (Iterator<BEncodedObject> iter = list.iterator(); iter.hasNext();)
		{
			iter.next().bEncode(target);
		}
		target.write('e');
	}
}
