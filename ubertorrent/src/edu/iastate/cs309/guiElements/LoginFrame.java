package edu.iastate.cs309.guiElements;

// TODO save ip addr

import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.client.ClientLog;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.lowerlevel.Box;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.util.ConfigFile;
import edu.iastate.cs309.util.Util;

/**
 * The LoginFrame is a small dialog box that allows the user to enter
 * information regarding connecting to a server UberTorrent
 * 
 * @author Justin
 */
@SuppressWarnings("serial")
public class LoginFrame extends JDialog
{
	public static final int APROVE_OPTION = 0;
	public static final int CANCEL_ACTION = 1;
	private static final String DEFAULT_DEBUG_HOST = "kylebox.engl.iastate.edu";
	private int closeStatus;

	private Box bigPanel;

	private JLabel iPAddrLabel;
	private JTextField iPAddrField;
	private JLabel portLabel;
	private JTextField portFeild;

	private JLabel usernameLabel;
	private JTextField userNameField;

	private JLabel pwordLable;
	private JPasswordField pwordField;

	private JButton loginButton, cancelButton;

	private JCheckBox rememberMeCheckBox;

	//private JLabel userLevelLabel;
	private JComboBox userLevelBox;

	private boolean isReadyToGiveValues;
	private boolean closedWithCancel;

	private Logger log;

	/**
	 * Default LoginFrame
	 * 
	 * @param owner
	 *            the owner of this LoginFrame
	 */
	public LoginFrame(JFrame owner)
	{
		super(owner, "Login to " + Util.getUber());
		createComponents();
		arrangeComponents();
		addListenersToComponents();
	}

	private void setConfiguration()
	{
		ConfigFile conf = null;
		try
		{
			conf = new ConfigFile(new File(MainGui.getConfigFileLocation()));
		}
		catch (FileNotFoundException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
		}
		List<String> value = conf.getValueByName("savePword");
		if (value == null || value.size() == 1)
		{
			conf.setProperty("savePword", "false");
			return;
		}

		pwordField.setEnabled(false);
		rememberMeCheckBox.setSelected(true);
	}

	private void saveConfiguration()
	{
		File confFile = new File(MainGui.getConfigFileLocation());

		ConfigFile conf = null;
		try
		{
			if (!confFile.exists())
				ConfigFile.writeDefaultConfigFile();
			conf = new ConfigFile(confFile);
		}
		catch (FileNotFoundException e2)
		{
			log.log(Level.WARNING, "The config file could not be loaded.");
			if (Util.DEBUG)
				e2.printStackTrace();
		}

		ArrayList<String> toSave = new ArrayList<String>(2);
		toSave.add(rememberMeCheckBox.isSelected() ? "true" : "false");
		if (rememberMeCheckBox.isSelected())
			toSave.add(new String(Util.getHashedBytes(pwordField.getPassword())));
		conf.setProperty("savePword", toSave);

		try
		{
			conf.writeFile(new File(MainGui.getConfigFileLocation()));
		}
		catch (FileNotFoundException e1)
		{
			log.log(Level.WARNING, "The config file could not be saved to.");
			if (Util.DEBUG)
				e1.printStackTrace();
		}
	}

	private void createComponents()
	{
		iPAddrLabel = new JLabel("Hostname");
		iPAddrField = new JTextField();
		iPAddrField.setMinimumSize(new Dimension(120, 10));

		portLabel = new JLabel("Port");
		portFeild = new JTextField();
		portFeild.setMinimumSize(new Dimension(70, 10));

		usernameLabel = new JLabel("User");
		userNameField = new JTextField();

		pwordLable = new JLabel("Password");
		pwordField = new JPasswordField();

		rememberMeCheckBox = new JCheckBox("remember me");

		userLevelBox = new JComboBox(new String[] { "admin", "user", "guest" });

		loginButton = new JButton("Login");
		cancelButton = new JButton("Cancel");

		closeStatus = CANCEL_ACTION;

		log = ClientLog.log;
	}

	private void arrangeComponents()
	{
		bigPanel = new VBox("Login", Alignment.CENTER).addComp(new HBox(Alignment.LEADING).addComp(iPAddrLabel).addComp(iPAddrField).addComp(portLabel).addComp(portFeild)).addComp(new HBox(Alignment.LEADING).addComp(usernameLabel).addComp(userNameField).addComp(pwordLable).addComp(pwordField)).addComp(new HBox(Alignment.LEADING).addComp(userLevelBox).addComp(loginButton).addComp(cancelButton));

		add(bigPanel);
	}

	private void addListenersToComponents()
	{
		loginButton.addActionListener(loginAction);
		loginButton.addKeyListener(enterPressed);
		iPAddrField.addKeyListener(enterPressed);
		rememberMeCheckBox.addKeyListener(enterPressed);
		userLevelBox.addKeyListener(enterPressed);
		pwordField.addKeyListener(enterPressed);
		cancelButton.addActionListener(cancelAction);
		addWindowListener(windowActionlistener);
	}

	/**
	 * After creating a login frame one must call this to show it
	 * 
	 * @return the status of the frame
	 */
	public int showFrame()
	{
		//setPreferredSize(new Dimension(330, 140));
		pack();
		setResizable(false);
		GUIUtil.setUberImageIcon(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setVisible(true);
		return closeStatus;
	}

	/**
	 * @return the hashed bytes of the password if the frame is ready to share
	 *         values, null otherwise
	 */
	public PasswordHash getPassWord()
	{
		if (Util.DEBUG && pwordField.getPassword().length == 0)
			return Util.getPwordHashObject("password".getBytes());
		if (rememberMeCheckBox.isSelected())
		{
			ConfigFile f = null;
			try
			{
				f = new ConfigFile(new File(MainGui.getConfigFileLocation()));
			}
			catch (FileNotFoundException e)
			{
				log.log(Level.SEVERE, "The password could not be loaded from the config file.  Could not load it.  Exiting now.");
				if (Util.DEBUG)
					e.printStackTrace();
				System.exit(1);// Couldn't read the password from the file
			}

			return new PasswordHash(f.getValueByName("rememberPassword").get(1).getBytes());
		}// TODO justin need to grab from config file if pword is saved

		return Util.getPwordHashObject(pwordField.getPassword());
	}

	public String getAddrToConnectTo()
	{
		if (iPAddrField.getText().length() == 0 && Util.DEBUG)
			return DEFAULT_DEBUG_HOST;
		return iPAddrField.getText();
	}

	public int getPort()
	{
		try
		{
			int ret = Integer.parseInt(portFeild.getText());
			if (ret < 0)
				return 30908;
			return ret;
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "The port in the field was in correct.  Default used instead (30908).");
			return 30908;
		}
	}

	/**
	 * @return the acces lvl chosen by the user
	 */
	public int getAccessLvl()
	{
		String userLvl = (String) userLevelBox.getSelectedItem();
		if (userLvl.equals("admin"))
			return MainGui.ADMIN;
		if (userLvl.equals("user"))
			return MainGui.USER;
		return MainGui.VIEWER;
	}

	private ActionListener loginAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!validateInput() && !Util.DEBUG)
			{
				JOptionPane.showMessageDialog(LoginFrame.this, "You need to enter a valid IP address and/or a username", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return;
			}

			closeStatus = APROVE_OPTION;
			dispatchEvent(new WindowEvent(LoginFrame.this, WindowEvent.WINDOW_CLOSING));
		}

		private boolean validateInput()
		{
			if (pwordField.isEnabled() && (pwordField.getPassword().length == 0 || iPAddrField.getText().equals("")))
				return false;
			try
			{
				Short.parseShort(portFeild.getText());
			}
			catch (Exception e)
			{
				log.log(Level.WARNING, "The supplied port is not a short.  It will be submitted regardless.");
				return true;// This should be false, but it is dealt with later anyway
			}
			return true;
		}
	};

	private ActionListener cancelAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			log.log(Level.FINEST, "Loginframe was closed with cancel");
			closeStatus = CANCEL_ACTION;
			dispatchEvent(new WindowEvent(LoginFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};

	private KeyListener enterPressed = new KeyAdapter()
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				loginButton.doClick();
		}
	};

	private WindowListener windowActionlistener = new WindowAdapter()
	{
		@Override
		public void windowActivated(WindowEvent e)
		{
			setConfiguration();
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			saveConfiguration();
		}
	};
}
