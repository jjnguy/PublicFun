package gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import MessageSending.ChatInterface;
import MessageSending.InputListenerThread;

@SuppressWarnings("serial")
public class FullChatPanel extends JFrame implements ChatInterface {

	private static final int DEFAULT_PORT = 5001;
	private JTextArea conversation, outgoingMesages;
	private JScrollPane recievedScroller, outgoingScroller;
	private JSplitPane incomingAndOutgoingSplit;

	private List<String> outgoingMessageBuffer;

	private JButton send;

	private boolean connected;

	private Socket connection;
	private InputStream sockIn;
	private OutputStream socOut;
	private JMenuItem connectItem;
	private JMenuItem hostItem;
	private JMenuItem saveItem;

	public FullChatPanel() {
		super("Chat");
		JPanel mainPane = new JPanel(new GridBagLayout());
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
		mainPane.add(incomingAndOutgoingSplit, gc);
		gc.weightx = gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		mainPane.add(send, gc);
		add(mainPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(buildMenu());
		pack();
		setVisible(true);
	}

	private JMenuBar buildMenu() {
		JMenuBar ret = new JMenuBar();
		JMenu file = new JMenu("File");
		connectItem = new JMenuItem("Connect");
		connectItem.addActionListener(connectAction);
		hostItem = new JMenuItem("Host");
		hostItem.addActionListener(hostAction);
		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(saveAction);
		file.add(connectItem);
		file.add(hostItem);
		file.add(saveItem);
		ret.add(file);
		return ret;
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
		while (!outgoingMessageBuffer.isEmpty()) {
			String outgoingMessage = outgoingMessageBuffer.remove(0);
			try {
				socOut.write(outgoingMessage.getBytes());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Failed to send message.",
						"Message Send Fail", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// @Override
	public boolean hasMesageToSend() {
		return !outgoingMessageBuffer.isEmpty();
	}

	// @Override
	public void saveConversation() {
		JFileChooser choose = new JFileChooser();
		int choice = choose.showSaveDialog(this);
		if (choice == JFileChooser.CANCEL_OPTION)
			return;

		File f = choose.getSelectedFile();

		PrintStream out = null;
		try {
			out = new PrintStream(f);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Failed to save conversation.", "Save Fail",
					JOptionPane.ERROR_MESSAGE);
		}
		out.print(conversation.getText());
		out.close();
	}

	private ActionListener sendAction = new ActionListener() {

		// @Override
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

		// @Override
		public void keyPressed(KeyEvent e) {
			// TODO doesn't work
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getModifiers() == KeyEvent.CTRL_DOWN_MASK)
					send.doClick();
			}
		}
	};

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			// @Override
			public void run() {
				new FullChatPanel();
			}
		});
	}

	// @Override
	public void connectToChatServer(String host, int port) {
		if (connected())
			return;
		try {
			connection = new Socket(host, port);
			sockIn = connection.getInputStream();
			socOut = connection.getOutputStream();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, "Could not resolve host.", "Not Connected!",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to send message.",
					"Message Send Fail", JOptionPane.ERROR_MESSAGE);
		}
		InputListenerThread th = new InputListenerThread(this);
		th.start();
		send.setEnabled(true);
		connected = true;
		connectItem.setEnabled(false);
		hostItem.setEnabled(false);
	}

	// @Override
	public void hostConversation() throws IOException {
		if (connected())
			return;

		WaitingForConnectionFrame f = new WaitingForConnectionFrame(FullChatPanel.DEFAULT_PORT);
		connection = f.showConnectionDialog();
		if (connection == null)
			return;
		sockIn = connection.getInputStream();
		socOut = connection.getOutputStream();
		InputListenerThread th = new InputListenerThread(this);
		th.start();
		send.setEnabled(true);
		connected = true;
		connectItem.setEnabled(false);
		hostItem.setEnabled(false);
	}

	// @Override
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
		return (hour < 10 ? "0" : "") + (int) hour + ":" + (min < 10 ? "0" : "") + (int) min
				+ ":" + (Math.round(sec) < 10 ? "0" : "") + Math.round(sec);
	}

	// @Override
	public InputStream getIStream() {
		return sockIn;
	}

	private ActionListener connectAction = new ActionListener() {

		// @Override
		public void actionPerformed(ActionEvent e) {
			ConnectToFrame connect = new ConnectToFrame();
			int choice = connect.showConnectDialog();
			if (choice != ConnectToFrame.CONNECT_OPTION)
				return;

			connectToChatServer(connect.getHost(), connect.getPort());
		}
	};
	private ActionListener hostAction = new ActionListener() {

		// @Override
		public void actionPerformed(ActionEvent e) {
			try {
				hostConversation();
			} catch (IOException e1) {
				JOptionPane
						.showMessageDialog(FullChatPanel.this, "Failed to host conversation.",
								"Host Fail", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	private ActionListener saveAction = new ActionListener() {

		// @Override
		public void actionPerformed(ActionEvent e) {
			saveConversation();
		}
	};
}
