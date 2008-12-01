package runnables;

import java.io.*;

import javax.swing.JFileChooser;

import controller.SaveSong;

public class SaveSongTest {
	public static void main(String[] args) throws IOException {
		JFileChooser choose = new JFileChooser();
		choose.showOpenDialog(null);
		File song = choose.getSelectedFile();
		InputStream testFile = new FileInputStream(song);
		SaveSong.SaveASong(testFile);

		testFile.close();

		System.out.println("File is saved");
	}

}
