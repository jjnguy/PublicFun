package edu.iastate.cs309.guiElements;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.lowerlevel.Box;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.util.Util;

public class UbertorrentPreferencesDialog extends JDialog
{
	private Box mainPane;
	private Box downloadPreferencesPane;
	private JLabel downloadLocationLabel;
	private JTextField downloadLocationFeild;
	private JButton browse;
	private Box connectionPreferencesPane;
	private JLabel maxDownloadSpeedLabel;
	private JTextField maxDownloadSpeedFeild;
	private JLabel maxUploadSpeedLabel;
	private JTextField maxUploadSpeedFeild;
	private MainGui parent;
	private JButton ok, cancel;
	private int closingStatus;

	public UbertorrentPreferencesDialog(MainGui parentP)
	{
		super(parentP, Util.getUber() + " - Preferences");
		createComponents();
		parent = parentP;
	}

	private void createComponents()
	{
		JTabbedPane tabs = new JTabbedPane();
		createConnectionPane();
		createDownloadPanel();
		tabs.addTab("Connection", connectionPreferencesPane);
		tabs.addTab("Downloads", downloadPreferencesPane);

		ok = new JButton("Ok");
		ok.addActionListener(approveAction);
		cancel = new JButton("Cancel");
		cancel.addActionListener(cancleAction);

		mainPane = new VBox(Alignment.LEADING).addComp(tabs).addComp(new HBox(Alignment.LEADING).addComp(ok).addComp(cancel));
		add(mainPane);
		closingStatus = LoginFrame.CANCEL_ACTION;
	}

	private void createDownloadPanel()
	{
		downloadLocationFeild = new JTextField();
		browse = new JButton("Browse");
		browse.addActionListener(browseAction);
		// downloadLocationLabel = new JLabel("")
		downloadPreferencesPane = new HBox("Save Downloads In...", Alignment.LEADING).addComp(downloadLocationFeild).addComp(browse);
	}

	private void createConnectionPane()
	{
		maxDownloadSpeedFeild = new JTextField();
		maxDownloadSpeedLabel = new JLabel("Max global download speed:");
		maxUploadSpeedFeild = new JTextField();
		maxUploadSpeedLabel = new JLabel("Max global upload speed:");

		connectionPreferencesPane = new VBox(Alignment.LEADING).addComp(new HBox(Alignment.LEADING).addComp(maxDownloadSpeedLabel).addComp(maxDownloadSpeedFeild)).addComp(new HBox(Alignment.LEADING).addComp(maxUploadSpeedLabel).addComp(maxUploadSpeedFeild));
	}

	public static void main(String[] args)
	{
		GUIUtil.setLookAndFeel();
		new UbertorrentPreferencesDialog(null).showValueSettingDialog();
	}

	private WindowAdapter windowListner = new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			if (closingStatus == LoginFrame.CANCEL_ACTION)
				return;
			parent.setSaveFileLocation(downloadLocationFeild.getText());
		}
	};
	private ActionListener browseAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser choose = new JFileChooser();
			choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int choice = choose.showOpenDialog(UbertorrentPreferencesDialog.this);
			if (choice != JFileChooser.APPROVE_OPTION)
				return;
			downloadLocationFeild.setText(choose.getSelectedFile().getAbsolutePath());
		}
	};
	private ActionListener approveAction = new ActionListener()
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			closingStatus = LoginFrame.APROVE_OPTION;
			dispatchEvent(new WindowEvent(UbertorrentPreferencesDialog.this, WindowEvent.WINDOW_CLOSING));
		}
	};
	private ActionListener cancleAction = new ActionListener()
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			dispatchEvent(new WindowEvent(UbertorrentPreferencesDialog.this, WindowEvent.WINDOW_CLOSING));
		}
	};

	public void showValueSettingDialog()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(300, 100));
		pack();
		setVisible(true);
		// TODO justin finish
	}
}
