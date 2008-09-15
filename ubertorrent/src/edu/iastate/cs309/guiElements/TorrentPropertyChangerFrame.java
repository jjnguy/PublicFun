package edu.iastate.cs309.guiElements;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.client.TorrentInformationContainer;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.util.Util;

public class TorrentPropertyChangerFrame extends JDialog
{
	private TorrentInformationContainer tInfo;

	private JLabel maxDownloadLabel;
	private JTextField maxDownloadFeild;
	private JLabel maxUploadLabel;
	private JTextField maxUploadFeild;
	private JButton ok, cancel;
	private JLabel unitLabel1;
	private JLabel unitLabel2;
	private boolean updateValues;

	public TorrentPropertyChangerFrame(TorrentInformationContainer tInfoP, JFrame owner)
	{
		super(owner, Util.getUber() + " - Change Download Speed Settings");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tInfo = tInfoP;
		createComponents();
		setModal(true);
		layoutUI();
		showFrame();
	}

	private void showFrame()
	{
		setVisible(true);
	}

	private void createComponents()
	{
		maxDownloadLabel = new JLabel("Maximum Download Rate:");
		maxDownloadFeild = new JTextField();
		maxUploadLabel = new JLabel("Maximum Upload Rate:      "); //the spaces are dirty, but its easier this way
		maxUploadFeild = new JTextField();
		unitLabel1 = new JLabel("kb/s (0 for unlimmited)");
		unitLabel2 = new JLabel("kb/s (0 for unlimmited)");
		ok = new JButton("Ok");
		ok.addActionListener(confirmAction);
		cancel = new JButton("Cancel");
		cancel.addActionListener(cancelAction);
		pack();
	}

	private void layoutUI()
	{
		JPanel ret = new VBox("Network Speeds", Alignment.CENTER).addComp(new HBox(Alignment.CENTER).addComp(maxDownloadLabel).addComp(maxDownloadFeild).addComp(unitLabel1)).addComp(new HBox(Alignment.LEADING).addComp(maxUploadLabel).addComp(maxUploadFeild).addComp(unitLabel2)).addComp(new HBox(Alignment.CENTER).addComp(ok).addComp(cancel));
		setPreferredSize(new Dimension(400, 130));
		setSize(new Dimension(400, 130));
		setResizable(false);
		add(ret);
	}

	public boolean updateValues()
	{
		return updateValues;
	}

	private ActionListener confirmAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			updateValues = true;
			dispatchEvent(new WindowEvent(TorrentPropertyChangerFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};
	private ActionListener cancelAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			updateValues = false;
			dispatchEvent(new WindowEvent(TorrentPropertyChangerFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};

	public static void main(String[] args)
	{
		GUIUtil.setLookAndFeel();
		new TorrentPropertyChangerFrame(null, null).setVisible(true);
	}
}
