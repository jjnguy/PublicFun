package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Scanner;

/**
 * Saves a mp3 file to the file system of the host computer
 * 
 * A path to the saved mp3 can be then retrieved
 * 
 * Files are named numerically  (ex: 1.mp3) and a text file is kept in the 
 * same directory that holds the next available number
 * 
 * @author Benjamin, Justin
 *
 */
public class SaveSong {
	// TODO: Reuse numbers after a file gets removed in the future?
	
	//file that holds one number that will be the next filename
	private static final String FILE_NAME = "file_number.txt";

	//String to hold path to mp3 file thats saved
	private String pathToMP3 = null;
	/**
	 * 
	 * @param fileStream the mp3 file that we are saving
	 * 
	 * 
	 * @throws IOException
	 */
	public SaveSong(InputStream fileStream) throws IOException {
		OutputStream f_song;
		int fileInt;
		File intFile = new File(Controller.MP3_PATH + FILE_NAME);
		
		//if file doesn't exist create it and start with 0
		if (!intFile.exists()) {
			PrintStream out = new PrintStream(intFile);
			out.print(0);
			out.close();
		}
		
		//get number from file and create a file to save mp3 data into 
		Scanner fin = new Scanner(intFile);
		fileInt = fin.nextInt();
		fin.close();
		File song = new File(Controller.MP3_PATH + fileInt + ".mp3");

		//shouldn't happen unless mp3's or text file gets corrupted
		if (!song.createNewFile()) {
			System.err.println("Could not create song, location already exists, this is a bug");
			return;
		}

		try {
			f_song = new FileOutputStream(song);
		} catch (FileNotFoundException e1) {
			System.err.println("Could not save song, opening the stream caused an exception");
			return;
		}

		try {
			// save mp3 data into file
			Util.copyStream(fileStream, f_song);
		} catch (IOException e) {
			f_song.close();
			return;
		}

		f_song.close();

		//increment number in text file by 1 
		Writer output = new BufferedWriter(new FileWriter(intFile));
		output.write((fileInt + 1) + "");
		output.close();
		
		//path to newly saved mp3
		pathToMP3 = Controller.MP3_PATH + fileInt + ".mp3";
	}
	
	
	/**
	 * 
	 * @return String containing path to saved mp3 file.  String will be null if mp3 was not saved in constructor
	 */
	public String getPathToMP3() {
		return pathToMP3;
	}

}
