package runnables;

import java.io.*;

import controller.SaveSong;


public class SaveSongTest 
{
	public static void main (String[] args) throws IOException
	{
		File song = new File("c:/test.mp3");
		InputStream testFile = new FileInputStream(song);
		SaveSong.SaveASong(testFile);
		
		testFile.close();
		
		System.out.println("File is saved");
	}
	
	
}
//public static void main(String[] args) throws IOException