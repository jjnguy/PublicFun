package runnables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;

import file.SaveSong;


public class SaveSongTest {
	public static void main(String[] args) throws IOException {
		JFileChooser choose = new JFileChooser();
		File song = choose.getSelectedFile();
		InputStream testFile = new FileInputStream(song);
		SaveSong SS = new SaveSong(testFile);
		
		
		

		testFile.close();

		System.out.println("Path: " + SS.getPathToMP3());
	}

}
