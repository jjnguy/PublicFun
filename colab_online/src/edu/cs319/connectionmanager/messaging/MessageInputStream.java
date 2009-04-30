package edu.cs319.connectionmanager.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A wrapper class around an InputStream which allows for Messages to be read directly from the stream.
 * 
 * @author Justin Nelson
 **/
public class MessageInputStream {
	
	private InputStream in;

	/**
	 * Creates a MessageInputStream for the given InputStream
	 * 
	 * @param s The InputStream Messages are being received from
	 **/
	public MessageInputStream(InputStream s) {
		in = s;
	}

	/**
	 * Reads a Message from this stream
	 * 
	 * @return The next message in the stream.
	 * 
	 * @throws IOException If anything goes wrong in the reading process
	 **/
	public Message readMessage() throws IOException {
		// if we need more that 1KB of data in a message, then we have a problem
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read;
		while ((read = in.read()) != Byte.MAX_VALUE) {
			if(read == -1) {throw new IOException("EOF");}
			out.write(read);
		}
		return Message.decode(out.toByteArray());
	}
}
