package edu.cs319.dataobjects;

import edu.cs319.server.IServer;

/**
 * Information needed to send requests to the Server
 **/
public interface DocumentInfo {
	
	/**
	 * The name of the room this document is part of
	 * 
	 * @return The name of the room this document is part of
	 **/
	public String getRoomName();
	
	/**
	 * The username of this client
	 * 
	 * @return The username of this client
	 **/
	public String getUserName();
	
	/**
	 * The name of this document
	 * 
	 * @return The name of this document
	 **/
	public String getDocumentName();
	
	/**
	 * The Server we're using
	 * 
	 * @return The Server we're using
	 **/
	public IServer getServer();

	
} 
