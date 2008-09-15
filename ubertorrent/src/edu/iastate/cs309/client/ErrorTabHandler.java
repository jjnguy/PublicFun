package edu.iastate.cs309.client;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import edu.iastate.cs309.guiElements.mainGuiTabs.ErrorLogTab;

public class ErrorTabHandler extends Handler
{
	private ErrorLogTab tab;

	public ErrorTabHandler(ErrorLogTab tabToPublishTo)
	{
		tab = tabToPublishTo;
	}

	@Override
	public void close() throws SecurityException
	{
		// Left blank... TODO ???
	}

	@Override
	public void flush()
	{
		// Left blank... TODO ???
	}

	@Override
	public void publish(LogRecord record)
	{
		tab.getTextArea().append(record.toString());
	}
}
