package edu.iastate.cs309.guiElements;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.guiElements.lowerlevel.Box;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.util.Util;

public class ServerPropDialog extends JDialog
{
	private ServerProperties props;

	private int option;

	private Box mainBox;

	private JLabel clientConnectionPortLabel;
	private JTextField clientConnectionPortField;

	private JLabel peerPortLAbel;
	private JTextField peerPortField;

	private JLabel peerIDLabel;
	private JTextField peerIDField;

	private JLabel rootDirLabel;
	private JTextField rootDirFeild;

	private JLabel timeoutLabel;
	private JTextField timeoutFeild;

	private JLabel passwordLabel;
	private JPasswordField passwordField;

	private JButton ok;

	public ServerPropDialog(MainGui parent, ServerProperties properties)
	{
		super(parent);

		props = properties;

		createComponents();
		addListeners();
		placeComponents();
		tidyUp();
		showWindow();
	}

	private void createComponents()
	{
		clientConnectionPortLabel = new JLabel("Client connection port:");
		clientConnectionPortField = new JTextField(props.getPort() + "");

		peerPortLAbel = new JLabel("Peer connection port:");
		peerPortField = new JTextField(props.getPeerPort() + "");

		peerIDLabel = new JLabel("Peer ID:");
		peerIDField = new JTextField(props.getPeerID().toString() + "");

		rootDirLabel = new JLabel("Save files in:");
		rootDirFeild = new JTextField(props.getRootDir());

		timeoutLabel = new JLabel("Peer timeout:");
		timeoutFeild = new JTextField(props.getTimeout() + "");

		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(props.getPass().toString() + "");

		ok = new JButton("Ok");

		option = LoginFrame.CANCEL_ACTION;
	}

	private void addListeners()
	{
		ok.addActionListener(confirmAction);
	}

	private void placeComponents()
	{
		mainBox = new VBox("Server Properties", Alignment.TRAILING).addComp(new HBox(Alignment.CENTER).addComp(clientConnectionPortLabel).addComp(clientConnectionPortField)).addComp(new HBox(Alignment.CENTER).addComp(peerIDLabel).addComp(peerIDField)).addComp(new HBox(Alignment.CENTER).addComp(peerPortLAbel).addComp(peerPortField)).addComp(new HBox(Alignment.CENTER).addComp(rootDirLabel).addComp(rootDirFeild)).addComp(new HBox(Alignment.CENTER).addComp(timeoutLabel).addComp(timeoutFeild)).addComp(new HBox(Alignment.CENTER).addComp(passwordLabel).addComp(passwordField)).addComp(ok);
		add(mainBox);
	}

	private void tidyUp()
	{
		clientConnectionPortField.setMinimumSize(new Dimension(100, 10));
	}

	private void showWindow()
	{
		setModal(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public PasswordHash getPass()
	{
		return new PasswordHash(Util.getHashedBytes(passwordField.getPassword()));
	}

	public int getPort()
	{
		return Integer.parseInt(clientConnectionPortField.getText());
	}

	public int getPeerPort()
	{
		return Integer.parseInt(peerPortField.getText());
	}

	public long getTimeout()
	{
		return Long.parseLong(timeoutFeild.getText());
	}

	public PeerID getPeerID()
	{
		return new PeerID(peerIDField.getText().getBytes());
	}

	public String getRootDir()
	{
		return rootDirFeild.getText();
	}

	public ServerProperties getNewProp()
	{
		ServerProperties prop = new ServerProperties();
		prop.setPass(getPass());
		prop.setPeerID(getPeerID());
		prop.setPeerPort(getPeerPort());
		prop.setRootDir(getRootDir());
		prop.setPort(getPort());
		prop.setTimeout(getTimeout());
		return prop;
	}

	private ActionListener confirmAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			option = LoginFrame.APROVE_OPTION;
			dispatchEvent(new WindowEvent(ServerPropDialog.this, WindowEvent.WINDOW_CLOSING));
		}
	};
}
