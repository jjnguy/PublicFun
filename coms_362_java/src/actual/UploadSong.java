package actual;

import file.SaveSong;
import id3TagStuff.ID3v2_XTag;
import infoExpert.SongData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import databaseAccess.QueryDB;

import util.SongDataFactory;

/**
 * For uploading a mp3 to the filesystem and adding it to the database
 * 
 * @author Benjamin Petersen
 *
 */
public class UploadSong 
{
	
	/**
	 * Uploads the file to the system and saves its information to the database
	 * 
	 * @param fileStream
	 * 			InputStream of the uploaded mp3
	 * @param owner
	 * 			username of the owner of the song
	 * @param db
	 * 			QueryDB class that is being used
	 * @return
	 * 			A String indicating the success or failure of the upload
	 */
	public String uploadSong(InputStream fileStream, String owner, QueryDB db)
	{
		String fileLocation;
		
		try {
			//will save the song to the filesystem
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
	
	/*
	 * Will add the songs information to the database
	 */
	private boolean insertSongIntoDatabase(SongData song, String owner, QueryDB db) 
	{
		boolean ret = db.insertSongIntoDatabase(song, owner);
		db.closeDatabase();
		return ret;
	}
	
	
}
