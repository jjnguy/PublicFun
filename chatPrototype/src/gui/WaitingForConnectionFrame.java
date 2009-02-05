package gui;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class WaitingForConnectionFrame extends JDialog {

	private JLabel waitingForConnectionLable;
	private WaitToAcceptThread th;

	private int port;

	public WaitingForConnectionFrame(int port) {
		this.port = port;
		waitingForConnectionLable = new JLabel("Waiting for connection");
		getContentPane().add(waitingForConnectionLable);
		th = new WaitToAcceptThread(port, this);
		// addWindowListener(closeListener);
		setModal(true);
		pack();
	}

	public Socket showConnectionDialog() {
		th.start();

		setVisible(true);

		return th.getSock();
	}

	WindowListener closeListener = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			if (th.isAlive())
				th.stop();
		}
	};
}

class WaitToAcceptThread extends Thread {
	private int port;
	private Socket theSock;
	private WaitingForConnectionFrame frame;

	public WaitToAcceptThread(int port, WaitingForConnectionFrame f) {
		this.port = port;
		frame = f;
	}

	public Socket getSock() {
		return theSock;
	}

	public void run() {
		// TODO closing w/out connection disalows connecting again
		try {
			theSock = new ServerSocket(port).accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		frame.dispose();
		frame.setVisible(false);
	}

}