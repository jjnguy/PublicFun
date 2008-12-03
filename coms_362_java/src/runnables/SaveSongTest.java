package runnables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;

import controller.SaveSong;

public class SaveSongTest {
	public static void main(String[] args) throws IOException {
		JFileChooser choose = new JFileChooser();
		int selection = choose.showOpenDialog(null);
		File song = choose.getSelectedFile();
		InputStream testFile = new FileInputStream(song);
		SaveSong.SaveASong(testFile);

		testFile.close();

		System.out.println("File is saved");
	}

}
