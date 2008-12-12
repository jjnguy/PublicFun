package file;

import java.io.File;

import controller.Controller;

public class DeleteSong 
{
private boolean deleteSuccessful;
/**
 * Physically removed file from the filesystem
 * @param filename
 */
	public DeleteSong(String filename)
	{
		deleteSuccessful = (new File(Controller.MP3_PATH + filename)).delete();
	}
	
	/**
	 * 
	 * @return a boolean indicating whether or not the removal was successful
	 */
	public boolean successfulDelete()
	{
		return deleteSuccessful;
	}
	
}
