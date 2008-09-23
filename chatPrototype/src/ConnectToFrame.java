import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ConnectToFrame extends JDialog {
	public static final int CONNECT_OPTION = 5;
	public static final int CANCEL_OPTION = -5;

	private int choice;

	private JTextField hostField, portField;
	private JButton connectButton;
	private JLabel hostLable, portLable;

	public ConnectToFrame() {
		hostLable = new JLabel("Host:");
		hostField = new JTextField();
		portLable = new JLabel("Port:");
		portField = new JTextField("5001");
		connectButton = new JButton("Connect");
		connectButton.addActionListener(connectAction);

		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		mainPane.add(hostLable, gc);
		gc.gridx++;
		mainPane.add(hostField, gc);
		gc.gridx = 0;
		gc.gridy++;
		mainPane.add(portLable, gc);
		gc.gridx++;
		mainPane.add(portField, gc);
		gc.gridy++;
		mainPane.add(connectButton, gc);
		setModal(true);
		mainPane.setPreferredSize(new Dimension(200, 70));
		add(mainPane);
	}

	public int showConnectDialog() {
		pack();
		setVisible(true);
		return choice;
	}

	public int getPort() {
		return Integer.parseInt(portField.getText());
	}

	public String getHost() {
		return hostField.getText();
	}

	private WindowListener windowCloseListener = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			if (choice != ConnectToFrame.CONNECT_OPTION)
				choice = ConnectToFrame.CANCEL_OPTION;
		}
	};

	private ActionListener connectAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			choice = ConnectToFrame.CONNECT_OPTION;
			dispatchEvent(new WindowEvent(ConnectToFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};

}
