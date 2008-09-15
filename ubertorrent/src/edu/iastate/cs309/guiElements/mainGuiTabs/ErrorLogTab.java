package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.Rectangle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.iastate.cs309.client.ClientLog;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.util.Util;

/**
 * An ErrorLogTab contains information regarding certain errors that occurred
 * during UeberTorrent's runtime.
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class ErrorLogTab extends UberTab
{
	private Logger log;

	private JTextArea theTxt;

	private JScrollPane sc;

	/**
	 * Constructs a basic ErrorLogTab
	 */
	public ErrorLogTab()
	{
		log = ClientLog.log;
		theTxt = new JTextArea("Error Log: " + getTimestamp() + '\n');
		theTxt.setEditable(false);
		sc = new JScrollPane(theTxt);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(sc);
		ErrorHandler handler = new ErrorHandler(this);
		ClientLog.log.addHandler(handler);
	}

	private String getTimestamp()
	{
		return "[" + Util.getTimeStringFromMiliseconds(System.currentTimeMillis()) + "]";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabName()
	 */
	@Override
	public String getTabName()
	{
		return "Error Log";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText()
	{
		return "Displays errors or warnings that may have occured";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabIcon()
	 */
	@Override
	public Icon getTabIcon()
	{
		return new ImageIcon(GUIUtil.tabImageFOlder + "/" + "ErrorLogLogo.png");
	}

	public JTextArea getTextArea()
	{
		return theTxt;
	}

	public JScrollPane getScroll()
	{
		return sc;
	}

	/**
	 * 
	 */
	public void scrollBottom()
	{
		theTxt.scrollRectToVisible(new Rectangle(0, Integer.MAX_VALUE, 0, 0));
	}
}

class ErrorHandler extends Handler
{
	private ErrorLogTab tab;
	private SimpleFormatter formatter;

	public ErrorHandler(ErrorLogTab parent)
	{
		tab = parent;
		formatter = new SimpleFormatter();
	}

	/**
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() throws SecurityException
	{
	}

	/**
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush()
	{
	}

	/**
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord record)
	{
		tab.getTextArea().append(formatter.format(record));
		tab.scrollBottom();
	}
}
