/**
 * 
 */
package edu.iastate.cs309.torrentparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Michael Seibert
 */
public abstract class BEncodedObject
{
	/**
	 * Output this object into an {@link OutputStream} using the format
	 * specified by the BitTorrent specification.
	 * 
	 * The given stream will NOT be closed by this method.
	 * 
	 * @param target
	 *            {@link OutputStream} that this object will be encoded into.
	 * @throws IOException
	 *             If there is an error while writing to the stream.
	 */
	public abstract void bEncode(OutputStream target) throws IOException;

	/**
	 * Read a bencoded object from the given stream. Any "subobjects" (like the
	 * elements of a list or dictionary) will also be read, so that the returned
	 * value will be a complete {@link BEncodedObject}.
	 * 
	 * The given stream will NOT be closed, and will have been read just up to
	 * the end of the encoded object, and no further. (i.e. any bytes just after
	 * the object within the stream will not be read by this method.)
	 * 
	 * @param in
	 *            {@link InputStream} to read an object from
	 * @return A {@link BEncodedObject} read from the given stream
	 * @throws IOException
	 *             If there is an error while reading from the stream
	 * @throws ParseException
	 *             If the data in the stream does not follow the bencoding
	 *             specification.
	 */
	public static BEncodedObject readObject(InputStream in) throws IOException, ParseException
	{
		int input = in.read();
		if (input == -1)
			throw new IOException();
		return readObject((byte) input, in);
	}

	/**
	 * While reading from the stream, it is sometimes necessary to read the
	 * first byte of the next object to determine whether the end of some
	 * structure (like a list) has been reached. If the next byte is part of an
	 * object, then this method can be used to "prepend" the byte onto the
	 * stream before reading.
	 * 
	 * @see BEncodedObject#readObject(InputStream)
	 * 
	 * @param prevByte
	 *            The previous byte read from the stream
	 */
	private static BEncodedObject readObject(byte prevByte, InputStream in) throws IOException, ParseException
	{
		char type = (char) prevByte;
		switch (type)
		{
		case 'd':
			// Dictionary
			return readDictionary(in);
		case 'l':
			// List
			return readList(in);
		case 'i':
			// Integer
			return readInt(in);
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			// Byte String
			return readByteString(prevByte, in);
		default:
			// Invalid
			throw new ParseException("This is where the problem lies - " + type);
		}
	}

	/**
	 * Read a {@link Dictionary} from the {@link InputStream}. It is assumed
	 * that the leading 'd' will already have been read.
	 * 
	 * @param in
	 *            {@link InputStream} to read the {@link Dictionary} from.
	 * @return The {@link Dictionary} that was read from the stream.
	 * @throws IOException
	 *             If there is an error while reading from the stream
	 * @throws ParseException
	 *             If the data in the stream does not follow the bencoding
	 *             specification.
	 */
	private static Dictionary readDictionary(InputStream in) throws IOException, ParseException
	{
		// Read the type of the first object
		int input = in.read();
		if (input == -1)
			throw new IOException();

		char type = (char) input;
		Dictionary dict = new Dictionary();

		// type == e signals end of dictionary
		while (type != 'e')
		{
			// Read the key
			BEncodedObject key = readObject((byte) input, in);

			// Keys must be strings
			if (!(key instanceof ByteString))
				throw new ParseException();

			// Read the value
			BEncodedObject val = readObject(in);
			dict.put((ByteString) key, val);

			// Read the type of the next object
			input = in.read();
			if (input == -1)
				throw new IOException();
			type = (char) input;
		}

		return dict;
	}

	/**
	 * Read a {@link BList} from the {@link InputStream}. It is assumed that
	 * the leading 'l' will already have been read.
	 * 
	 * @param in
	 *            {@link InputStream} to read the {@link BList} from.
	 * @return The {@link BList} that was read from the stream.
	 * @throws IOException
	 *             If there is an error while reading from the stream
	 * @throws ParseException
	 *             If the data in the stream does not follow the bencoding
	 *             specification.
	 */
	private static BList readList(InputStream in) throws IOException, ParseException
	{
		// Read the type of the first object
		int input = in.read();
		if (input == -1)
			throw new IOException();

		char type = (char) input;
		BList list = new BList();

		// type == e signals the end of the list
		while (type != 'e')
		{
			// Read the rest of the object
			list.add(readObject((byte) input, in));

			// Read the type of the next object
			input = in.read();
			if (input == -1)
				throw new IOException();
			type = (char) input;
		}
		return list;
	}

	/**
	 * Read a {@link BInteger} from the {@link InputStream}. It is assumed that
	 * the leading 'i' will already have been read.
	 * 
	 * @param in
	 *            {@link InputStream} to read the {@link BInteger} from.
	 * @return The {@link BInteger} that was read from the stream.
	 * @throws IOException
	 *             If there is an error while reading from the stream
	 * @throws ParseException
	 *             If the data in the stream does not follow the bencoding
	 *             specification.
	 */
	private static BInteger readInt(InputStream in) throws IOException, ParseException
	{
		// Read first digit
		int input = in.read();
		if (input == -1)
			throw new IOException();

		char digit = (char) input;
		boolean positive = true;

		// If it's a negative integer...
		if (digit == '-')
		{
			positive = false;

			// Re-read the first digit
			input = in.read();
			if (input == -1)
				throw new IOException();
			digit = (char) input;
		}
		if (digit == '0')
		{
			// No leading zeroes

			// Make sure the next character is 'e'
			input = in.read();
			if (input == -1)
				throw new IOException();
			if ((char) input != 'e')
				throw new ParseException();
			return new BInteger(0);
		}

		long result = 0;
		while (Character.isDigit(digit))
		{
			result *= 10;
			result += digit - '0';

			// Read next digit
			input = in.read();
			if (input == -1)
				throw new IOException();
			digit = (char) input;
		}

		// Found a non-digit non-ending character
		if (digit != 'e')
			throw new ParseException();

		return new BInteger(positive ? result : -result);
	}

	/**
	 * Read a {@link ByteString} from the {@link InputStream}. The leading
	 * digit must be passed in
	 * 
	 * @param prevByte
	 *            The first byte of this string
	 * @param in
	 *            {@link InputStream} to read the {@link ByteString} from.
	 * @return The {@link ByteString} that was read from the stream.
	 * @throws IOException
	 *             If there is an error while reading from the stream
	 * @throws ParseException
	 *             If the data in the stream does not follow the bencoding
	 *             specification.
	 */
	private static ByteString readByteString(byte prevByte, InputStream in) throws IOException, ParseException
	{
		// Get the first digit
		int input = prevByte;
		char digit = (char) input;
		int size = 0;

		// Build up the integer
		while (Character.isDigit(digit))
		{
			size *= 10;
			size += digit - '0';
			input = in.read();
			if (input == -1)
				throw new IOException();
			digit = (char) input;
		}

		// ByteStrings must be of the form 'integer:string'
		if (digit != ':')
			throw new ParseException();

		// Read the ByteString out of the stream
		int bytesRead = 0;
		byte[] data = new byte[size];
		while (bytesRead != size)
			bytesRead += in.read(data, bytesRead, size - bytesRead);

		return new ByteString(data);
	}
}
