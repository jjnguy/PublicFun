package edu.cs319.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * @author Amelia
 * @author Justin Nelson
 * 
 */
public class WindowLogIn extends JDialog {

	// TODO check username for spaces or @

	private JTextField usernameField = new JTextField();
	private JPasswordField pwField = new JPasswordField();
	private JButton logInButton = new JButton("Log In");
	// private JButton newUserButton = new JButton("Create New User");
	private JButton cancelButton = new JButton("Cancel");
	
	private int exitStatus;

	private WindowLogIn() {
		this.setTitle("CoLab Log In");
		this.setSize(300, 250);
		setModal(true);
		setUpAppearance();
		setUpListeners();
	}

	private void setUpAppearance() {
		JLabel usernameLabel = new JLabel("User Name");
		JLabel pwLabel = new JLabel("Password");
		JLabel newUserLabel = new JLabel("New to CoLab?");
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
		mainPanel.add(pwLabel, c);

		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(pwField, c);

		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		mainPanel.add(logInButton, c);

		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(newUserLabel, c);

		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		//mainPanel.add(newUserButton, c);

		this.add(mainPanel);
	}

	public int showLoginWindow() {

		
		return exitStatus;
	}

	private void setUpListeners() {
		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO log in

			}
		});

		/*newUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO create new user

			}
		});*/

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitStatus = JOptionPane.CANCEL_OPTION;
				dispose();
			}
		});
	}

}
