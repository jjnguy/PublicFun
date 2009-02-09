package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import MessageSending.ClientBackend;
import MessageSending.InputListenerThread;

// TODO THis should be two classes
// TODO gui half and message sender and reciever class

@SuppressWarnings("serial")
public class FullChatPanel extends JFrame {

	private ChatInterface client;
	
	private JTextArea outgoingMesages;
	private JScrollPane outgoingScroller;

	private JButton send;

	private JMenuItem connectItem;
	private JMenuItem hostItem;
	private JMenuItem saveItem;

	public FullChatPanel() {
		super("Chat");
		JPanel mainPane = new JPanel(new GridBagLayout());
		
		outgoingMesages = new JTextArea();
		outgoingMesages.setEditable(true);
		outgoingScroller = new JScrollPane(outgoingMesages);
		outgoingScroller.setPreferredSize(new Dimension(300, 150));
		send = new JButton("Send");
		send.setEnabled(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		mainPane.add(outgoingScroller);
		gc.weightx = gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		mainPane.add(send, gc);
		add(mainPane);

		this.addWindowListener(closeListener);

		client = new ClientBackend(this);
		
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

	private KeyListener enterPress = new KeyAdapter() {

		// @Override
		public void keyPressed(KeyEvent e) {
			// TODO doesn't work
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if ((e.getModifiers() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK)
					send.doClick();
			}
		}
	};

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FullChatPanel();
			}
		});
	}

	public void insertText(int location, String text){
		this.outgoingMesages.insert(text, location);
	}

	public boolean connected() {
		return client.connected();
	}

	private static String timestamp(long currentTimeMillis) {
		return getTimeStringFromMiliseconds(currentTimeMillis) + " : ";
	}

	/**
	 * Simple way to turn miliseconds into the current date
	 * 
	 * @param secondsP
	 *            Miliseconds since Jan 1 1970, or something like that
	 * @return a string representing the current date
	 */
	private static String getTimeStringFromMiliseconds(long secondsP) {
		int seconds = (int) (secondsP / 1000);
		double year = seconds / 60.0 / 60 / 24.0 / 365;
		double day = (year - Math.floor(year)) * 365;
		double hour = (day - Math.floor(day)) * 24;
		double min = (hour - Math.floor(hour)) * 60;
		double sec = (min - Math.floor(min)) * 60;
		return (hour < 10 ? "0" : "") + (int) hour + ":" + (min < 10 ? "0" : "") + (int) min
				+ ":" + (Math.round(sec) < 10 ? "0" : "") + Math.round(sec);
	}

	private ActionListener connectAction = new ActionListener() {

		// @Override
		public void actionPerformed(ActionEvent e) {
			ConnectToFrame connect = new ConnectToFrame();
			int choice = connect.showConnectDialog();
			if (choice != ConnectToFrame.CONNECT_OPTION)
				return;

			client.connectToChatServer(connect.getHost(), connect.getPort());
		}
	};
	private ActionListener hostAction = new ActionListener() {

		// @Override
		public void actionPerformed(ActionEvent e) {
			try {
				client.hostConversation();
			} catch (IOException e1) {
				JOptionPane
						.showMessageDialog(FullChatPanel.this, "Failed to host conversation.",
								"Host Fail", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	private ActionListener saveAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			client.saveConversation();
		}
	};

	private WindowListener closeListener = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			client.sendMessage();
		}
	};

	private KeyListener userTypesListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			int caratPosition = FullChatPanel.this.outgoingMesages.getCaretPosition();
			char c = e.getKeyChar();

		}

	};
}
