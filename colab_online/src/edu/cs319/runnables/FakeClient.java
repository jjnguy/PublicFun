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
		// add client
		Socket s = new Socket("localhost", 4444);
		MessageOutputStream out = new MessageOutputStream(s.getOutputStream());
		out.printMessage(new Message(MessageType.NEW_CLIENT, "jjnguy", new ArrayList<String>()));
		s.getOutputStream().close();
		s.close();
		// create room
		s = new Socket("localhost", 4444);
		out = new MessageOutputStream(s.getOutputStream());
		List<String> ar = new ArrayList<String>();
		ar.add("room");
		ar.add(new String(new byte[] {}));
		out.printMessage(new Message(MessageType.NEW_COLAB_ROOM, "jjnguy", ar));
		s.getOutputStream().close();
		s.close();
		// send message to room
		s = new Socket("localhost", 4444);
		out = new MessageOutputStream(s.getOutputStream());
		List<String> args2 = new ArrayList<String>();
		args2.add("room");
		args2.add("my message");
		out.printMessage(new Message(MessageType.NEW_MESSAGE, "jjnguy", args2));
	}
}
