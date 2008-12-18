package actual;

import databaseAccess.QueryDB;
import file.DeleteSong;

/**
 * For removing a song (mp3) from the system
 * 
 * 
 * @author Benjamin Petersen
 *
 */
public class RemoveSong 
{

	/**
	 * Removes the specified song from the filesystem and the database
	 * 
	 * @param fileName
	 * 			file name of the mp3 to be deleted
	 * @param db
	 * 			QueryDB class that is being used
	 * @param owner
	 * 			The owner of the mp3 file
	 * @return
	 * 			A String indicating the success or failure of the removal
	 */
	public String removeSong(String fileName, QueryDB db, String owner)
	{
		DeleteSong ds = new DeleteSong(fileName);
		
		if (!ds.successfulDelete())
			return "Physical removal failed";
		
		if (!db.deleteSong(fileName, owner))
			return "Song was deleted from the physical disk, but a Database error prevented it from being removed from the database."; 
		
		
		return "Song was successfully deleted!";
	}
	
}

