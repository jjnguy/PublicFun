package controller;

import java.io.InputStream;

public interface UploadSong {
	
	/**
	 * Passes an InputStream containing the file to be uploaded to the Server
	 * @param fileStream
	 * @return A String containing a message describing whether or not the upload was succssfull
	 */
	public String uploadSong(InputStream fileStream);
	
	
	
}
