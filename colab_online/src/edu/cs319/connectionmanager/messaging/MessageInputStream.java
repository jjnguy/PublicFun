package edu.cs319.connectionmanager.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 
 * @author Justin Nelson
 * 
 */
public class MessageInputStream {
	InputStream in;

	public MessageInputStream(InputStream s) {
		in = s;
	}

	public Message readMessage() throws IOException {
		// if we need more that 1KB of data in a message, then we have a problem
		byte[] buffer = new byte[1024];
		int read;
		int bytesRead = 0;
		while ((read = in.read()) != Byte.MAX_VALUE) {
			if(read == -1) {throw new IOException("EOF");}
			buffer[bytesRead] = (byte) read;
			bytesRead++;
		}
		byte[] ret = Arrays.copyOfRange(buffer, 0, bytesRead);
		return Message.decode(ret);
	}
}
