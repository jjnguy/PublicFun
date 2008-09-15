/**
 * 
 */
package edu.iastate.cs309.server;

import java.util.TimerTask;

/**
 * 
 * 
 * @author Michael Seibert
 */
public class StatusUpdater extends TimerTask
{
	private final Server target;

	/**
	 * @param target
	 * 
	 */
	public StatusUpdater(Server target)
	{
		this.target = target;
	}

	/**
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run()
	{
		target.existsStatusUpdater = true;
		if (!target.needStatusUpdates())
		{
			target.existsStatusUpdater = false;
			cancel();
			return;
		}
		target.updateStatus();
	}
}
