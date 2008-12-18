package file;

import java.io.File;

import controller.Controller;

/**
 * Deletes a song from the filesystem
 * 
 * @author Benjamin Petersen
 *
 */
public class DeleteSong {
	private boolean deleteSuccessful;

	/**
	 * Constructor that physically removes a  file from the file system
	 * 
	 * @param filename
	 * 			file name of the mp3 to be deleted
	 */
	public DeleteSong(String filename) {
		deleteSuccessful = (new File(Controller.MP3_PATH + filename)).delete();
	}

	/**
	 * 
	 * @return 
	 * 		a boolean indicating whether or not the removal was successful
	 */
	public boolean successfulDelete() {
		return deleteSuccessful;
	}

}
