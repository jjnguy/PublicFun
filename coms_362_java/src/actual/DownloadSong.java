package actual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DownloadSong
{

	/**
	 * 
	 * @param filename name corresponding to the mp3 filename on the server
	 * 
	 * @return a FileInputStream of the mp3 data from the specified mp3
	 */
	public FileInputStream downloadSong(String fileName, String path)
	{
		   File fileToDownload = new File(path + fileName);
		   
			
			  try {
				return new FileInputStream(fileToDownload);
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
				return null;
			}
	}
	
	
	
}
