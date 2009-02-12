package prototypes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class BasicTestPrototype extends JFrame {

	public static final int defaultPort = 5001;
	public static final String defaultHost = "localhost";
	public static final String textAddedMessage = "textAdded";
	public static final String textDeletedMessage = "textDeleted";

	private List<String> outGoingMessageBuffer;

	private JTextArea theText;
	private Socket connection;

	public BasicTestPrototype() {
		super("Test Prototype");

		outGoingMessageBuffer = new ArrayList<String>();

		theText = new JTextArea();
		theText.addKeyListener(typeListener);
		theText.getDocument();
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.setPreferredSize(new Dimension(300, 300));
		mainPane.add(theText, BorderLayout.CENTER);
		add(mainPane);
		pack();
		setJMenuBar(buildMenu());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private DocumentListener l = new DocumentListener() {

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub

		}
	};

	private JMenuBar buildMenu() {
		JMenuItem host = new JMenuItem("Host");
		host.addActionListener(hostAction);
		JMenuItem connect = new JMenuItem("Connect");
		connect.addActionListener(connectAction);
		JMenu file = new JMenu("File");
		file.add(host);
		file.add(connect);
		JMenuBar ret = new JMenuBar();
		ret.add(file);
		return ret;
	}

	public boolean hasNewMessages() {
		return !this.outGoingMessageBuffer.isEmpty();
	}

	public void sendMessage() throws IOException {
		if (!hasNewMessages())
			return;
		PrintStream out = new PrintStream(connection.getOutputStream());
		out.println(this.outGoingMessageBuffer.remove(0));
	}

	private boolean decodeMessage(String message) {
		// SSystem.out.println("We made it to a decode");
		if (message.startsWith(BasicTestPrototype.textAddedMessage)) {
			return decodeTextAddedMessage(message);
		} else if (message.startsWith(BasicTestPrototype.textDeletedMessage)) {
			return decodeTextDeletedMessage(message);
		} else
			return false;
	}

	private boolean decodeTextAddedMessage(String message) {
		if (!message.startsWith(BasicTestPrototype.textAddedMessage))
			return false;
		String[] data = message.split(";", -1);
		if (data.length < 3)
			return false;
		String textAdded = data[1];
		int pos = -1;
		try {
			pos = Integer.parseInt(data[2]);
		} catch (NumberFormatException e) {
			return false;
		}
		if (pos < 0 || pos > theText.getText().length())
			return false;
		theText.insert(textAdded, pos);
		return true;
	}

	private boolean decodeTextDeletedMessage(String message) {
		if (!message.startsWith(BasicTestPrototype.textDeletedMessage))
			return false;
		return true;
	}

	private KeyListener typeListener = new KeyListener() {
		String typableChars = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?";

		@Override
		public void keyPressed(KeyEvent e) {
			if (typableChars.contains(e.getKeyChar() + "")) {
				return;
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("In enter press event");
				String newMessage = textAddedMessage + ";";
				newMessage += "\n;";
				newMessage += theText.getCaretPosition() + ";";
				outGoingMessageBuffer.add(newMessage);
				try {
					sendMessage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (!typableChars.contains(e.getKeyChar() + "")) {
				System.out.println("Typed key was undef");
				return;
			}
			String newMessage = textAddedMessage + ";";
			newMessage += e.getKeyChar() + ";";
			newMessage += theText.getCaretPosition() + ";";
			outGoingMessageBuffer.add(newMessage);
			try {
				sendMessage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(newMessage);
		}
	};

	private ActionListener connectAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				connection = new Socket("localhost", defaultPort);
				ListenForClientChangeThread th = new ListenForClientChangeThread(connection
						.getInputStream());
				th.start();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};

	private ActionListener hostAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				ServerSocket servSoc = new ServerSocket(defaultPort);
				connection = servSoc.accept();
				ListenForClientChangeThread th = new ListenForClientChangeThread(connection
						.getInputStream());
				th.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};

	class ListenForClientChangeThread extends Thread {
		Scanner in;

		public ListenForClientChangeThread(InputStream source) {
			in = new Scanner(source);
		}

		@Override
		public void run() {
			while (true) {
				while (in.hasNextLine()) {
					String message = in.nextLine();
					System.out.println(decodeMessage(message));
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new BasicTestPrototype();
	}
}
