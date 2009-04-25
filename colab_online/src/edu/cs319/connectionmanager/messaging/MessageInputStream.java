package edu.cs319.connectionmanager.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Justin Nelson
 * 
 */
public class MessageInputStream {
	private InputStream in;

	public MessageInputStream(InputStream s) {
		in = s;
	}

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
