package actual;

import file.SaveSong;
import id3TagStuff.ID3v2_XTag;
import infoExpert.SongData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import databaseAccess.QueryDB;

import util.SongDataFactory;

public class UploadSong 
{
	
	/**
	 * Passes an InputStream containing the file to be uploaded to the Server
	 * @param fileStream
	 * @return A String containing a message describing whether or not the upload was successful
	 */
	public String uploadSong(InputStream fileStream, String owner, QueryDB db)
	{
		String fileLocation;
		
		try {
			SaveSong SS = new SaveSong(fileStream);
			fileLocation = SS.getPathToMP3();
			
		
			/* Create SongData class from the ID3 tag and insert it into the DB */
			ID3v2_XTag newTag = new ID3v2_XTag(new File(fileLocation));
			SongData sd = SongDataFactory.tagToSongData(newTag);
			sd.setFileName(fileLocation);
			if (!insertSongIntoDatabase(sd, owner, db)) {
				File f = new File(fileLocation);
				f.delete();
				return "Database access failed";
			}

			return "Saved to: " + fileLocation;
		} catch (IOException e) {
			e.printStackTrace();
			return "Error: Song not saved";
		}
	}
	private boolean insertSongIntoDatabase(SongData song, String owner, QueryDB db) 
	{
		boolean ret = db.insertSongIntoDatabase(song, owner);
		db.closeDatabase();
		return ret;
	}
	
	
}
