package edu.cs319.server.events;

public interface CoLabEvent {

	/**
	 * Should only be allowed to be called once Calling a second time should throw an
	 * AlreadyPrecessedException
	 * 
	 * @return Whether or not the event completed successfully
	 */
	public boolean processEvent();
}
