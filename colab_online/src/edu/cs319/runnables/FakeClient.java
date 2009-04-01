package edu.cs319.runnables;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.connectionmanager.messaging.MessageType;

public class FakeClient {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 4444);
		MessageOutputStream out = new MessageOutputStream(s.getOutputStream());
		out.printMessage(new Message(MessageType.NEW_CLIENT, "jjnguy", new ArrayList<String>()));
		s.getOutputStream().close();
		
		List<String> args2 = new ArrayList<String>();
		args2.add("room");
		args2.add("my message");
	}
}
