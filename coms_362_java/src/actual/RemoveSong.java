package actual;

import databaseAccess.Database;
import file.DeleteSong;

public class RemoveSong 
{
	/**
	 * 
	 * @param fileName
	 * @return A String containing a message indicating whether or not the removal was successful
	 */
	public String removeSong(String fileName, Database db)
	{
		DeleteSong ds = new DeleteSong(fileName);
		
		if (!ds.successfulDelete())
			return "Physical removal failed";
		
		if (!db.deleteSong(fileName))
			return "Song was deleted from the physical disk, but a Database error prevented it from being removed from the database."; 
		
		
		return "Song was successfully deleted!";
	}
	
}

