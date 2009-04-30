package edu.cs319.connectionmanager.messaging;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A wrapper class around an OutputStream which allows for Messages to be written directly to the stream.
 * 
 * @author Justin Nelson
 **/
public class MessageOutputStream {

	OutputStream out;
	
	/**
	 * Creates a MessageOutputStream for the given OutputStream
	 * 
	 * @param s The OutputStream Messages are being written to.
	 **/
	public MessageOutputStream(OutputStream s) {
		out = s;
	}
	
	/**
	 * Writes a Message to this stream
	 * 
	 * @param message The message to write to the stream.
	 * 
	 * @throws IOException If anything goes wrong in the writing process
	 **/
	public void printMessage(Message message) throws IOException {
		out.write(message.encode());
	}
}
