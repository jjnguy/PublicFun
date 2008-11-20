package controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;

import util.Util;


public class SaveSong 
{
	private static final String  SONG_DIRECTORY = "c:/SONGS/";
	private static final String FILE_NAME = "file_number.txt";

	public static boolean SaveASong(InputStream fileStream) throws IOException// throws IOException 
	{
		String sInt = "";
		String songLocation = "";
		int fileInt;

	
		
		Scanner fin = new Scanner (new File (SONG_DIRECTORY + FILE_NAME));
		//File f_fileLocation = new File (SONG_DIRECTORY + FILE_NAME);
		
		//FileInputStream fis_fileLocation = new FileInputStream(f_fileLocation);
		
		//InputStreamReader in = new InputStreamReader(fis_fileLocation); 
		
		
		fileInt = fin.nextInt();
		
		sInt = fileInt + "";
		
		
		
		
		File song = new File(SONG_DIRECTORY + sInt + ".mp3");
		if(!song.exists())
			song.createNewFile();
		else return false;

		OutputStream f_song;
		
			try {
				f_song = new FileOutputStream(song);
			} catch (FileNotFoundException e1) {
				
			return false;
			}
		
		
		try {
			util.Util.copyStream(fileStream, f_song);
		} catch (IOException e) {
			f_song.close();
			return false;
		}
		
			
			f_song.close();
			
			return true;

		
	}
	
	
	
	
	
}
