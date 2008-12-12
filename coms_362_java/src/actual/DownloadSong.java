package actual;

import java.io.FileInputStream;

public interface DownloadSong
{

	/**
	 * 
	 * @param filename name corresponding to the mp3 filename on the server
	 * 
	 * @return a FileInputStream of the mp3 data from the specified mp3
	 */
	public FileInputStream downloadSong(String filename);
	
	
	
}
