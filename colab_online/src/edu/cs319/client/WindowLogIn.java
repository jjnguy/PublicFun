package edu.cs319.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.cs319.connectionmanager.clientside.ConnectionFactory;
import edu.cs319.connectionmanager.clientside.Proxy;

/**
 * 
 * @author Amelia Gee
 * @author Justin Nelson
 * 
 */
public class WindowLogIn extends JDialog {

	// TODO check username for spaces or @

	private JTextField hostField = new JTextField();
	private JTextField usernameField = new JTextField();
	private JPasswordField pwField = new JPasswordField();
	private JButton logInButton = new JButton("Log In");
	// private JButton newUserButton = new JButton("Create New User");
	private JButton cancelButton = new JButton("Cancel");

	private IClient client;
	private Proxy serverConnection;

	private WindowLogIn(JFrame parent, IClient client) {
		super(parent, "CoLab Log In");
		this.client = client;
		this.setSize(300, 250);
		setModal(true);
		setUpAppearance();
		setUpListeners();
	}

	private void setUpAppearance() {
		JLabel hostNameLabel = new JLabel("Host Name");
		JLabel usernameLabel = new JLabel("User Name");
		JLabel pwLabel = new JLabel("Password");
		// JLabel newUserLabel = new JLabel("New to CoLab?");
		Dimension textFieldSize = new Dimension(125, 25);
		usernameField.setPreferredSize(textFieldSize);
		pwField.setPreferredSize(textFieldSize);

		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets borderInsets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.insets = borderInsets;

		c.gridx = 1;

		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(usernameLabel, c);

		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(usernameField, c);

		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(hostNameLabel, c);

		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(hostField, c);

		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		mainPanel.add(logInButton, c);

		// c.gridx = 0;
		// c.gridy = 4;
		// c.anchor = GridBagConstraints.LINE_END;
		// mainPanel.add(newUserLabel, c);
		//
		// c.gridx = 1;
		// c.anchor = GridBagConstraints.CENTER;
		// mainPanel.add(newUserButton, c);

		this.add(mainPanel);
	}

	public static Proxy showLoginWindow(JFrame parent, IClient client) {
		WindowLogIn win = new WindowLogIn(parent, client);
		win.setVisible(true);
		return win.serverConnection;
	}

	private void login(String host, String username) {
		serverConnection = ConnectionFactory.getNetworkedInstance().connect(host, 4444, client,
				username);
	}

	public static boolean isValidUserName(String usernme) {
		if (usernme.length() < 1)
			return false;
		return !(usernme.contains(" ") || usernme.startsWith("@"));
	}

	private void setUpListeners() {
		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!isValidUserName(usernameField.getText())) {
					JOptionPane.showMessageDialog(WindowLogIn.this,
							"Your username may not contain a space or begin with an '@'.  "
									+ "It must also be at least one character long.");
					return;
				}
				login(hostField.getText(), usernameField.getText());
				dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverConnection = null;
				dispose();
			}
		});
	}
}
