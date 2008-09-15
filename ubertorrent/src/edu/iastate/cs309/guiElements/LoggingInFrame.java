package edu.iastate.cs309.guiElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.client.ConnectThread;
import edu.iastate.cs309.client.TheActualClient;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;

public class LoggingInFrame extends JDialog
{
	private String host;
	private int port;
	private PasswordHash pword;
	private TheActualClient client;
	private boolean success;
	private JLabel loggingInLabel;
	private JButton cancle;
	private edu.iastate.cs309.guiElements.lowerlevel.Box mainPane;
	private ConnectThread th;

	public LoggingInFrame(TheActualClient clientP, String hostP, int portP, PasswordHash pwordP)
	{
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		th = new ConnectThread(clientP, hostP, portP, pwordP, this);
		success = false;
		client = clientP;
		host = hostP;
		port = portP;
		pword = pwordP;
		loggingInLabel = new JLabel("Attempting to connect to " + host + " on port " + port + ".");
		cancle = new JButton("Give Up!");
		cancle.addActionListener(giveUpAction);
		mainPane = new HBox(Alignment.LEADING).addComp(loggingInLabel).addComp(cancle);
		add(mainPane);
		setResizable(false);
		pack();
		th.start();
		setVisible(true);

	}

	public boolean success()
	{
		return success;
	}

	private ActionListener giveUpAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			th.kill();
			dispatchEvent(new WindowEvent(LoggingInFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};
	private WindowAdapter windowClosing = new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			success = th.success();
		};
	};

	public void setSuccess(boolean b)
	{
		success = b;

	}
}
