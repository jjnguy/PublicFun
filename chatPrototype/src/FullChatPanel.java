import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class FullChatPanel extends JPanel implements ChatInterface {

	private JTextArea conversation, outgoingMesages;
	private JScrollPane recievedScroller, outgoingScroller;
	private JSplitPane incomingAndOutgoingSplit;

	private List<String> outgoingMessageBuffer;

	private JButton send;

	private boolean connected;

	private Socket connection;
	private InputStream sockIn;

	public FullChatPanel() {
		super(new GridBagLayout());
		outgoingMessageBuffer = new ArrayList<String>();
		incomingAndOutgoingSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		conversation = new JTextArea();
		conversation.setEditable(false);
		outgoingMesages = new JTextArea();
		outgoingMesages.setEditable(true);
		outgoingMesages.addKeyListener(enterPress);
		recievedScroller = new JScrollPane(conversation);
		outgoingScroller = new JScrollPane(outgoingMesages);
		outgoingScroller.addKeyListener(enterPress);
		outgoingScroller.setPreferredSize(new Dimension(300, 150));
		recievedScroller.setPreferredSize(new Dimension(300, 150));
		incomingAndOutgoingSplit.add(recievedScroller);
		incomingAndOutgoingSplit.add(outgoingScroller);
		incomingAndOutgoingSplit.setResizeWeight(.5);
		incomingAndOutgoingSplit.addKeyListener(enterPress);
		send = new JButton("Send");
		send.addActionListener(sendAction);
		send.setEnabled(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		add(incomingAndOutgoingSplit, gc);
		gc.weightx = gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		add(send, gc);
	}

	public void newMessage(String text, String userName) {
		conversation.append("\n");
		conversation.append(userName + ": "
				+ FullChatPanel.timestamp(System.currentTimeMillis()));
		conversation.append(text);
	}

	/**
	 * 
	 * @return the mesage to be sent
	 */
	public void sendMessage() {
		if (outgoingMessageBuffer.isEmpty())
			throw new NoSuchElementException();
		String outgoingMessage = outgoingMessageBuffer.remove(0);
		PrintStream out = null;
		try {
			out = new PrintStream(connection.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(outgoingMessage);
	}

	@Override
	public boolean hasMesageToSend() {
		return !outgoingMessageBuffer.isEmpty();
	}

	@Override
	public void saveConversation(String location) {
		JFileChooser choose = new JFileChooser();
		int choice = choose.showSaveDialog(this);
		if (choice == JFileChooser.CANCEL_OPTION)
			return;

		File f = choose.getSelectedFile();

		PrintStream out = null;
		try {
			out = new PrintStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(conversation.getText());
	}

	private ActionListener sendAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (outgoingMesages.getText().trim().equals(""))
				return;
			newMessage(outgoingMesages.getText(), "You");
			outgoingMessageBuffer.add(outgoingMesages.getText());
			outgoingMesages.setText("");
			sendMessage();
		}
	};

	private KeyListener enterPress = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO doesn't work
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getModifiers() == KeyEvent.CTRL_DOWN_MASK)
					send.doClick();
			}
		}
	};

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(new FullChatPanel());
		f.setJMenuBar(new ChatPanelMenu());
		f.pack();
		f.setVisible(true);
	}

	@Override
	public void connectToChatServer(String host, int port) {
		if (connected())
			return;
		try {
			connection = new Socket(host, port);
			sockIn = connection.getInputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputListenerThread th = new InputListenerThread(this);
		th.start();
		send.setEnabled(true);
		connected = true;
	}

	@Override
	public void hostConversation() throws IOException {
		if (connected())
			return;
		ServerSocket servS = new ServerSocket(ChatServer.DEFAULT_PORT);
		connection = servS.accept();
		sockIn = connection.getInputStream();
		InputListenerThread th = new InputListenerThread(this);
		th.start();
		send.setEnabled(true);
		connected = true;
	}

	@Override
	public boolean connected() {
		return connected;
	}

	public static String timestamp(long currentTimeMillis) {
		return getTimeStringFromMiliseconds(currentTimeMillis) + " : ";
	}

	/**
	 * Simple way to turn miliseconds into the current date
	 * 
	 * @param secondsP
	 *            Miliseconds since Jan 1 1970, or something like that
	 * @return a string representing the current date
	 */
	public static String getTimeStringFromMiliseconds(long secondsP) {
		int seconds = (int) (secondsP / 1000);
		double year = seconds / 60.0 / 60 / 24.0 / 365;
		double day = (year - Math.floor(year)) * 365;
		double hour = (day - Math.floor(day)) * 24;
		double min = (hour - Math.floor(hour)) * 60;
		double sec = (min - Math.floor(min)) * 60;
		return (hour < 10 ? "0" : "") + (int) hour + ":" + (min < 10 ? "0" : "")
				+ (int) min + ":" + (Math.round(sec) < 10 ? "0" : "") + Math.round(sec);
	}

	@Override
	public InputStream getIStream() {
		// TODO Auto-generated method stub
		try {
			return connection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
