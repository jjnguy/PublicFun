package edu.iastate.cs309.guiElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.iastate.cs309.client.ClientLog;
import edu.iastate.cs309.client.PluginAccessors;
import edu.iastate.cs309.client.TheActualClient;
import edu.iastate.cs309.client.TorrentInformationContainer;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.lowerlevel.Box;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.guiElements.mainGuiTabs.ErrorLogTab;
import edu.iastate.cs309.guiElements.mainGuiTabs.FileInfoTab;
import edu.iastate.cs309.guiElements.mainGuiTabs.PeerInfoTab;
import edu.iastate.cs309.guiElements.mainGuiTabs.RotThirteen;
import edu.iastate.cs309.guiElements.mainGuiTabs.SpeedTab;
import edu.iastate.cs309.guiElements.mainGuiTabs.TorrentPropertiesTab;
import edu.iastate.cs309.guiElements.mainGuiTabs.UberTabbedPane;
import edu.iastate.cs309.plugins.PluginManager;
import edu.iastate.cs309.plugins.exceptions.PluginNotLoadableException;
import edu.iastate.cs309.util.ConfigFile;
import edu.iastate.cs309.util.Util;

/**
 * This is the class containing all of the layout code for the main application.
 * It also contains the list of torrents currently active for the client
 * 
 * @author Michael Seibert, Justin Nelson
 */
@SuppressWarnings("serial")
public class MainGui extends JFrame
{
	/**
	 * The location of the config save file
	 */
	private static String configFileLocation;

	public static final int ADMIN = Integer.MAX_VALUE;
	public static final int USER = 0;
	public static final int VIEWER = Integer.MIN_VALUE;
	private int currentUserLevel;

	// frame for facilitating logging into a server
	private LoginFrame login;
	private boolean loginInfoIsSet;
	private boolean isLoggedIn;
	/*
	 * Things on the top of the MainGui
	 */
	// the menubar
	private JMenuBar menu;
	// menubar item [file]
	private JMenu fileMenu;
	// file items
	private JMenuItem createTorrentMenuItem;
	private JMenuItem addPluginMenuItem;
	private JMenuItem removePluginMenuItem;
	private JMenuItem removeAllPluginMenuItem;
	private JMenuItem transferFilesMenuItem;
	private JMenuItem openTorrentMenuItem;
	private JMenuItem loginMenuItem;
	private JMenuItem logoutMenuItem;
	private JMenuItem changeServerPropsMenuItem;
	private JMenuItem shutdownServerMenuItem;
	private JMenuItem exitMenuItem;
	// menubar item [help]
	private JMenu helpMenu;
	// help items
	private JMenuItem aboutMenuItem;
	private JMenuItem resetConfigMenuItem;
	private JMenuItem preferencesMenuItem;
	private JMenuItem whyAreThingsDisabledMenuItem;
	// the toolbar
	private JToolBar toolBar;
	// the toolbar's items
	private JButton createTorrentToolbarItem;
	private AbstractButton openTorrentToolbarItem;
	private JButton removeTorrentToolbarItem;
	private JButton start_resume_TorrentToolbarItem;

	/*
	 * The containers of the main content
	 */
	private JScrollPane torrentListScroller;
	private UberTabbedPane tabPane;

	// Table of torrents
	private TorrentTable torrentList;

	// the items in the filter pane
	private JToggleButton active;
	private JToggleButton downloading;
	private JToggleButton completed;
	private JToggleButton inactive;
	// the tabs at the bottom
	private ErrorLogTab logTab;
	private SpeedTab speedTab;
	private PeerInfoTab peerTab;
	private TorrentPropertiesTab torrentTab;
	private FileInfoTab fileInfoTab;
	private RotThirteen rotTab;

	// Manager for loading plugins
	private PluginManager pManager;
	// Config file to save and recover settings from
	private ConfigFile confFile;
	private boolean saveStateThisTime;

	private TheActualClient clientBackend;
	private String saveLocation;
	private Logger log = ClientLog.log;

	/**
	 * Creates a MainGui
	 * 
	 * Shows the window right away.
	 * 
	 * @throws PluginNotLoadableException
	 */
	public MainGui() throws PluginNotLoadableException
	{
		super(Util.getUber());

		createComponents();

		addEventHandlers();

		JPanel bigPanel = layoutUI();

		tidyUpComponents();

		showWindow(bigPanel);

		PluginAccessors.setMainGui(this);
		pManager = new PluginManager();

		setConfiguration();
	}

	private final void createComponents()
	{
		makeMenu();

		makeToolbar();

		loginInfoIsSet = false;
		isLoggedIn = false;

		torrentList = new TorrentTable();
		torrentListScroller = new JScrollPane(torrentList);

		tabPane = new UberTabbedPane();
		saveStateThisTime = true;

		active = new JCheckBox("active");
		completed = new JCheckBox("complete");
		downloading = new JCheckBox("downloading");
		inactive = new JCheckBox("inactive");

		logTab = new ErrorLogTab();
		speedTab = new SpeedTab();
		peerTab = new PeerInfoTab();
		torrentTab = new TorrentPropertiesTab(null, null);
		fileInfoTab = new FileInfoTab();
		rotTab = new RotThirteen(13);
	}

	private final void makeToolbar()
	{
		toolBar = new JToolBar();
		createTorrentToolbarItem = toolBar.add(createTorrentAction);
		createTorrentToolbarItem.setText("Create Torrent...");
		openTorrentToolbarItem = toolBar.add(openTorrentAction);
		openTorrentToolbarItem.setText("Open Torrent...");
		openTorrentToolbarItem.setEnabled(false);
		toolBar.addSeparator();
		removeTorrentToolbarItem = toolBar.add(removeTorrent);
		removeTorrentToolbarItem.setIcon(new ImageIcon(GUIUtil.toolBarIconFolder + File.separator + "RemoveTorrentIcon.png"));
		removeTorrentToolbarItem.setToolTipText("Remove selected torrent");
		removeTorrentToolbarItem.setEnabled(false);
		start_resume_TorrentToolbarItem = toolBar.add(toggleTorrentStatus);
		start_resume_TorrentToolbarItem.setIcon(new ImageIcon(GUIUtil.toolBarIconFolder + File.separator + "NotSureIcon.png"));
		start_resume_TorrentToolbarItem.setToolTipText("Pause/Resume current torrent");
		start_resume_TorrentToolbarItem.setEnabled(false);
		// no vertical orientation
		toolBar.setOrientation(JToolBar.HORIZONTAL);
	}

	private final void makeMenu()
	{
		menu = new JMenuBar();

		fileMenu = new JMenu("File");
		createTorrentMenuItem = fileMenu.add(new JMenuItem("Create Torrent..."));
		openTorrentMenuItem = fileMenu.add(new JMenuItem("Open Torrent..."));
		openTorrentMenuItem.setEnabled(false);
		//fileMenu.addSeparator();
		//transferFilesMenuItem = fileMenu.add(new JMenuItem("Transfer Files"));
		//fileMenu.addSeparator();
		addPluginMenuItem = fileMenu.add(new JMenuItem("Add Plugin..."));
		removePluginMenuItem = fileMenu.add(new JMenuItem("Remove Plugin..."));
		removeAllPluginMenuItem = fileMenu.add(new JMenuItem("Remove all plugins!"));
		fileMenu.addSeparator();
		loginMenuItem = fileMenu.add(new JMenuItem("Login..."));
		logoutMenuItem = fileMenu.add(new JMenuItem("Logout"));
		logoutMenuItem.setEnabled(false);
		fileMenu.addSeparator();
		changeServerPropsMenuItem = fileMenu.add(new JMenuItem("Change Server Properties"));
		changeServerPropsMenuItem.setEnabled(false);
		shutdownServerMenuItem = fileMenu.add(new JMenuItem("Shutdown Server"));
		shutdownServerMenuItem.setEnabled(false);
		fileMenu.addSeparator();
		exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
		menu.add(fileMenu);

		helpMenu = new JMenu("Help");
		aboutMenuItem = helpMenu.add(new JMenuItem("About..."));
		resetConfigMenuItem = helpMenu.add(new JMenuItem("Reset Defaults"));
		preferencesMenuItem = helpMenu.add(new JMenuItem("Preferences..."));
		preferencesMenuItem.addActionListener(preferencesAction);
		whyAreThingsDisabledMenuItem = helpMenu.add(new JMenuItem("Why are some things disabled?"));
		whyAreThingsDisabledMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(MainGui.this, "Things are disabled because you need to be logged into a Server in order to perform the actions.\nOr plugins are not installed.\nOr you don't have permission do do certain actions.", "Server Connection Message", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		menu.add(helpMenu);
	}

	private Box createFilterPane()
	{
		return new VBox(Alignment.LEADING, false).addComp(active).addComp(downloading).addComp(completed).addComp(inactive);
	}

	private final void addEventHandlers()
	{
		addPluginMenuItem.addActionListener(installPlugin);
		removePluginMenuItem.addActionListener(removePluginAction);
		removeAllPluginMenuItem.addActionListener(removeAllPluginsAction);
		createTorrentMenuItem.addActionListener(createTorrentAction);
		//transferFilesMenuItem.addActionListener(transferFileActon);
		aboutMenuItem.addActionListener(about);
		resetConfigMenuItem.addActionListener(resetConfigAction);
		loginMenuItem.addActionListener(loginAction);
		logoutMenuItem.addActionListener(logoutAction);
		changeServerPropsMenuItem.addActionListener(changeServerPropsAction);
		shutdownServerMenuItem.addActionListener(shutDownAction);
		exitMenuItem.addActionListener(exit);
		openTorrentMenuItem.addActionListener(openTorrentAction);
		torrentList.addRightClickListener(torrentContextMenu);
		torrentList.getSelectionModel().addListSelectionListener(torrentSelected);
		addWindowListener(windowClosedListener);
		// TODO torrentlist is a listselection listener justin

	}

	/**
	 * @return a big panel
	 */
	private final JPanel layoutUI()
	{
		setJMenuBar(menu);
		JPanel toolBarPanel = new JPanel(new BorderLayout());
		toolBarPanel.add(toolBar, BorderLayout.NORTH);

		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		toolBarPanel.add(splitter);

		JSplitPane splitter2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		splitter2.setRightComponent(torrentListScroller);
		splitter2.setLeftComponent(createFilterPane());

		splitter.setLeftComponent(splitter2);
		splitter.setRightComponent(tabPane);
		splitter.setResizeWeight(.5);
		addTabs();
		return toolBarPanel;
	}

	private void addTabs()
	{
		tabPane.addTab(torrentTab);
		//tabPane.addTab(peerTab);
		//tabPane.addTab(speedTab);
		//tabPane.addTab(fileInfoTab);
		tabPane.addTab(logTab);
		tabPane.addTab(rotTab);
	}

	private void tidyUpComponents()
	{
		active.setSelected(true);
		inactive.setSelected(true);
		downloading.setSelected(true);
		completed.setSelected(true);

		setMinimumSize(new Dimension(100, 48));
	}

	private void setConfiguration()
	{
		loadConfFileForGet();
		setBounds();
		setSaveLoc();
		setSelectedTab();
		log.log(Level.INFO, "The configuration was loaded from the config file");
	}

	private void loadConfFileForGet()
	{
		configFileLocation = "config/clientGUI.conf";
		try
		{
			confFile = new ConfigFile(new File(MainGui.getConfigFileLocation()));
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.SEVERE, "The config file could not be created for some reason.  Expect failure.");
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	private void setSelectedTab()
	{
		try
		{
			tabPane.setSelectedIndex(new Integer(confFile.getValueByNameString("currentlyDisplayedTab")).intValue());
		}
		catch (IndexOutOfBoundsException e)
		{
			log.log(Level.WARNING, "The tab index in the config file was out of range.");
			if (Util.DEBUG)
				System.out.println("Tabs were out of range for some reason");
			tabPane.setSelectedIndex(-0);//negative 0 is way better than regular 0
		}
		if (pManager.getInstalledPlugins().size() == 0)
		{
			removePluginMenuItem.setEnabled(false);
			removeAllPluginMenuItem.setEnabled(false);
		}
		log.log(Level.FINE, "The selected tab was loaded from the conf file");
	}

	private void setSaveLoc()
	{
		saveLocation = confFile.getValueByNameString("saveFilesLocation");
		log.log(Level.FINE, "The location in which to save the files was loaded from the conf file.");
	}

	private void setBounds()
	{
		List<String> compBounds = confFile.getValueByName("windowLoc");
		if (compBounds != null)
		{
			if (compBounds.size() != 4)
			{
				log.log(Level.WARNING, "The value for saving the height, with , x , and y loc did not have the correct number of values.");
			}
			else
			{
				setSize(Integer.parseInt(compBounds.get(2)), Integer.parseInt(compBounds.get(3)));
				setBounds(Integer.parseInt(compBounds.get(0)), Integer.parseInt(compBounds.get(1)), Integer.parseInt(compBounds.get(2)), Integer.parseInt(compBounds.get(3)));
				log.log(Level.FINE, "The window bounds were loaded from the conf file.");
			}
		}
	}

	/**
	 * Is called by TheActualClient to let the Gui tell the Client to do things
	 * 
	 * @param clientP
	 */
	public void setClientInstance(TheActualClient clientP)
	{
		clientBackend = clientP;
	}

	public boolean login()
	{
		login = new LoginFrame(this);
		int action = login.showFrame();
		if (action == LoginFrame.CANCEL_ACTION)
			return false;

		if (clientBackend == null)
		{
			log.log(Level.WARNING, "This MainGui has no instance of a Client.  Login failed.");
			JOptionPane.showMessageDialog(this, "The client has not yet been given to this GUI.", "No client Known Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String host = login.getAddrToConnectTo();

		currentUserLevel = login.getAccessLvl();

		LoggingInFrame f = new LoggingInFrame(clientBackend, host, login.getPort(), login.getPassWord());

		isLoggedIn = f.success();
		if (isLoggedIn)
			changeComponentStatusAfterLoginOrOut();

		clientBackend.getListOfTorrents();

		// clientBackend.tellTheGUIAboutSomeNewTorrentInfo();

		return isLoggedIn;
	}

	/**
	 * Logs a user out of the Server
	 */
	public void logout()
	{
		isLoggedIn = false;
		clientBackend.disconnect();
		changeComponentStatusAfterLoginOrOut();
		List<TorrentInformationContainer> empty = new ArrayList<TorrentInformationContainer>(0);
		torrentList.updateList(empty);
	}

	private void changeComponentStatusAfterLoginOrOut()
	{
		toggleComponentEnabledness(loginMenuItem);
		toggleComponentEnabledness(logoutMenuItem);
		if (currentUserLevel != VIEWER) //A cheat to disallow a guest from doing anything
		{
			toggleComponentEnabledness(removeTorrentToolbarItem);
			toggleComponentEnabledness(openTorrentMenuItem);
			toggleComponentEnabledness(openTorrentToolbarItem);
		}
		if (currentUserLevel == ADMIN)
		{
			toggleComponentEnabledness(shutdownServerMenuItem);
			toggleComponentEnabledness(changeServerPropsMenuItem);
		}
	}

	/**
	 * If a component is enabled it will be set to disabled, otherwise it is set
	 * to enabled
	 * 
	 * @param comp
	 *            the component to be toggled
	 */
	public static void toggleComponentEnabledness(Component comp)
	{
		comp.setEnabled(!comp.isEnabled());
	}

	public void updateTorrentInformation(final List<TorrentInformationContainer> allTorrents)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				torrentList.updateList(allTorrents);
				torrentList.repaint(torrentList.getBounds());
				updateTorrentPropsTab();
			}
		});
	}

	public String getAddressToConnectTo()
	{
		if (login == null)
			return null;
		return login.getAddrToConnectTo();
	}

	public PasswordHash getPasswordHash()
	{
		if (login == null)
			return null;
		return login.getPassWord();
	}

	public boolean connected()
	{
		return clientBackend.connected();
	}

	/**
	 * Fix up the JFrame with size info and show it.
	 * 
	 * @param bigPanel
	 *            The panel to put in the JFrame before displaying.
	 */
	private void showWindow(JPanel bigPanel)
	{
		add(bigPanel);
		pack();
		GUIUtil.setUberImageIcon(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void saveState()
	{
		loadConfFileForeSave();
		saveSelectedTab();
		saveWindowBounds();
		saveFileSaveDirectory();
		writeConfFile();
	}

	private void writeConfFile()
	{
		try
		{
			confFile.writeFile(new File(MainGui.getConfigFileLocation()));
			log.log(Level.FINE, "The conf file was saved to: " + MainGui.getConfigFileLocation());
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.WARNING, "The config file could not be saved.");
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	private void saveFileSaveDirectory()
	{
		confFile.setProperty("saveFilesLocation", saveLocation);
		log.log(Level.FINE, "The save file directory was saved to the conf file.");
	}

	private void saveWindowBounds()
	{
		List<String> windowLocTemp = new ArrayList<String>(4);
		windowLocTemp.add(getX() + "");
		windowLocTemp.add(getY() + "");
		windowLocTemp.add(getWidth() + "");
		windowLocTemp.add(getHeight() + "");
		confFile.setProperty("windowLoc", windowLocTemp);
		log.log(Level.FINE, "The window bounds were saved to the conf file");
	}

	private void saveSelectedTab()
	{
		confFile.setProperty("currentlyDisplayedTab", tabPane.getSelectedIndex() + "");
		log.log(Level.FINE, "The selected tab was saved to the conf file");
	}

	private void loadConfFileForeSave()
	{
		try
		{
			confFile = new ConfigFile(new File(MainGui.getConfigFileLocation()));
		}
		catch (FileNotFoundException e1)
		{
			log.log(Level.WARNING, "The config file could not be loaded.  A new one will be created.");
			confFile = new ConfigFile();
		}
	}

	public static String getConfigFileLocation()
	{
		return configFileLocation;
	}

	private void removeAllPlugins()
	{
		if (pManager.getInstalledPlugins().size() == 0)
			return;
		if (!pManager.removeAllPluginsLocal())
		{
			int choice2 = JOptionPane.showConfirmDialog(MainGui.this, "Not all plugins were uninstalled.  Something bad happened.\nWould you like to forcefully remove them?", "Error Resetting Configuration", JOptionPane.YES_NO_OPTION);
			if (choice2 == JOptionPane.YES_OPTION)
			{
			}
		}
	}

	private void resetConfiguration()
	{
		int choice = JOptionPane.showConfirmDialog(MainGui.this, "Are you sure you wish to reset all options to default and remove all plugins? (Some changes will take place on restart)", "Reset Configuration?", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
		if (choice == JOptionPane.NO_OPTION)
			return;
		try
		{
			ConfigFile.writeDefaultConfigFile();
			saveStateThisTime = false;
		}
		catch (FileNotFoundException e1)
		{
			JOptionPane.showMessageDialog(MainGui.this, "The config file could not be changed.  Something bad happened.", "Error Resetting Configuration", JOptionPane.ERROR_MESSAGE);
			if (Util.DEBUG)
				e1.printStackTrace();
		}
		removeAllPlugins();
	}

	/*
	 * getters for kyle
	 */
	/**
	 * @return the Floatable JToolBar in Ubertorrent
	 */
	public JToolBar getToolbar()
	{
		return toolBar;
	}

	/**
	 * @return the UberTabbedPane that Ubertorrent has on the bottom
	 */
	public UberTabbedPane getTabPane()
	{
		return tabPane;
	}

	/**
	 * @return the MenuBar on the top of Ubertorrent
	 * 
	 * By default contains two menus
	 */
	public JMenuBar getMainMenu()
	{
		return menu;
	}

	/**
	 * @return the PluginManager that this GUI is connected to
	 */
	public PluginManager getPluginManager()
	{
		return pManager;
	}

	private ActionListener loginAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			login();
		}
	};

	private ActionListener logoutAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			logout();
		}
	};

	private ActionListener changeServerPropsAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!connected())// TODO allert the user justin
				return;
			ServerPropDialog serverProp = new ServerPropDialog(MainGui.this, clientBackend.getServerProps());
			clientBackend.changeServerSettings(serverProp.getNewProp());
		}
	};

	private ActionListener shutDownAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			clientBackend.shutDownServer();
			isLoggedIn = false;
			changeComponentStatusAfterLoginOrOut();

		}
	};

	private Action createTorrentAction = new AbstractAction()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			CreateTorrentFrame f = new CreateTorrentFrame(MainGui.this);
			if (f.seed())
			{
				clientBackend.addTorrent(f.getFile());
			}
		}
	};

	private Action openTorrentAction = new AbstractAction()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			AcceptTorrentFileChooser chooser = new AcceptTorrentFileChooser();
			int result = chooser.showOpenDialog(MainGui.this);
			if (result == AcceptTorrentFileChooser.CANCEL_OPTION)
				return;
			File[] files = chooser.getSelectedFiles();
			for (File file : files)
			{
				if (!file.isDirectory())
					clientBackend.addTorrent(file);
			}
		}
	};

	private ActionListener exit = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			dispatchEvent(new WindowEvent(MainGui.this, WindowEvent.WINDOW_CLOSING));
		}
	};

	private ActionListener resetConfigAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			resetConfiguration();
		}
	};

	private ActionListener about = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			new AboutUberTorrentFrame(MainGui.this);
		}
	};

	private Action removeTorrent = new AbstractAction()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int selectedIndex = torrentList.getSelectedRow();
			if (selectedIndex == -1)
				return;
			log.log(Level.FINE, "The GUI requested that a torrent be removed.");
			clientBackend.removeTorrent(torrentList.getTorrent(selectedIndex).refID);
		}
	};

	private Action toggleTorrentStatus;// TODO justin

	private ActionListener installPlugin = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AddPluginFrame p = new AddPluginFrame(pManager);
			int result = p.showOpenDialog(MainGui.this);
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			if (p.addPlugin())
			{
				removeAllPluginMenuItem.setEnabled(true);
				removePluginMenuItem.setEnabled(true);
				log.log(Level.FINE, "Plugin installed succesfully.");
			}
			else
			{
				log.log(Level.WARNING, "Plugin not installed succesfully.");
			}
		}
	};

	private ActionListener removePluginAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			new UnInstallPluginFrame(pManager, MainGui.this);
			if (pManager.getInstalledPlugins().size() == 0)
			{
				removePluginMenuItem.setEnabled(false);
				removeAllPluginMenuItem.setEnabled(false);
				log.log(Level.FINE, "Plugin removed succesfully.");
			}
		}
	};

	private ActionListener removeAllPluginsAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			removeAllPlugins();
			if (pManager.getInstalledPlugins().size() == 0)
			{
				removePluginMenuItem.setEnabled(false);
				removeAllPluginMenuItem.setEnabled(false);
				log.log(Level.FINE, "All plugin removed succesfully.");
			}
			else
			{
				log.log(Level.WARNING, "Plugins not removed succesfully.");
			}
		}
	};

	private ActionListener preferencesAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			UbertorrentPreferencesDialog p = new UbertorrentPreferencesDialog(MainGui.this);
			p.showValueSettingDialog();
		}
	};

	private PropertyChangeListener selectedTorrentChangeListner = new PropertyChangeListener()
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			int selectedRow = torrentList.getSelectedRow();
			if (selectedRow == -1)
				return;
			fileInfoTab.setInfo(torrentList.getTorrent(selectedRow));
		}
	};

	private WindowAdapter windowClosedListener = new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			if (saveStateThisTime)
				saveState();
			saveStateThisTime = true;
			if (connected())
				logout();
		}
	};

	private TorrentRightClickListener torrentContextMenu = new TorrentRightClickListener()
	{
		private TorrentInformationContainer torrent;

		@Override
		public void actionPerformed(MouseEvent e, TorrentInformationContainer torrent)
		{
			this.torrent = torrent;
			JPopupMenu popup = makePopupMenu(e);
			popup.show(torrentList, e.getX(), e.getY());
		}

		private JPopupMenu makePopupMenu(MouseEvent e)
		{
			JPopupMenu popup = new JPopupMenu();
			JMenuItem torrentPropMenuItem = new JMenuItem("Properties...");
			torrentPropMenuItem.addActionListener(torentPropAction);
			popup.add(torrentPropMenuItem);
			JMenuItem removeTorrentMenuItem = new JMenuItem("Remove");
			removeTorrentMenuItem.addActionListener(removeTorrent);
			popup.add(removeTorrentMenuItem);
			JMenuItem transferTorrentMenuItem = new JMenuItem("Transfer Files");
			transferTorrentMenuItem.addActionListener(transferFileActon);
			if (torrent.info == null)
				return popup;
			transferTorrentMenuItem.setEnabled(torrent.info.getComplete());
			popup.add(transferTorrentMenuItem);
			return popup;
		}

		private ActionListener torentPropAction = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				TorrentPropertyChangerFrame propChange = new TorrentPropertyChangerFrame(torrentList.getSelectedTorrent(), MainGui.this);
			}
		};

		private ActionListener transferFileActon = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// List<Integer> indexes = new ArrayList<Integer>(torrent.info.getIndividualFileNames().size());

				/** only multi file downloads have getIndividualFileNames() set */
				int numFiles = 1;
				if (torrent.info.getIndividualFileNames().size() != 0)
					numFiles = torrent.info.getIndividualFileNames().size();

				List<Integer> indices = new ArrayList<Integer>();

				for (int i = 0; i < numFiles; ++i)
					indices.add(i);

				if (Util.DEBUG)
					System.out.println("The GUI says: We should be transfering the Torrent with refID " + torrent.refID);
				clientBackend.getFile(torrent.refID, indices);
			}
		};
	};

	private ListSelectionListener torrentSelected = new ListSelectionListener()
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			updateTorrentPropsTab();
		}
	};

	private void updateTorrentPropsTab()
	{
		if (torrentList.getSelectedRow() != -1)
		{
			TorrentInformationContainer torrent;
			torrent = torrentList.getModel().getTorrent(torrentList.getSelectedRow());
			torrentTab.setProperties(torrent.info, torrent.prop);
		}
		else
			torrentTab.setProperties(null, null);
	}

	public void setSaveFileLocation(String text)
	{
		saveLocation = text;
	}

	public TheActualClient getClient()
	{
		return clientBackend;
	}
}
