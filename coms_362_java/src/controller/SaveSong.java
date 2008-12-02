package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Scanner;

import util.Util;

public class SaveSong {
	// TODO: Reuse Numbers?
	private static final String FILE_NAME = "file_number.txt";

	public static String SaveASong(InputStream fileStream) throws IOException {
		OutputStream f_song;
		int fileInt;
		File intFile = new File(Controller.MP3_PATH + FILE_NAME);
		if (!intFile.exists()) {
			PrintStream out = new PrintStream(intFile);
			out.print(0);
			out.close();
		}
		Scanner fin = new Scanner(intFile);
		fileInt = fin.nextInt();
		fin.close();
		File song = new File(Controller.MP3_PATH + fileInt + ".mp3");

		if (!song.createNewFile()) {
			return "Could not create song, location already exists, this is a bug";
		}

		try {
			f_song = new FileOutputStream(song);
		} catch (FileNotFoundException e1) {
			return "Could not save song, opening the stream caused an exception";
		}

		try {
			SaveSong.copyStream(fileStream, f_song);
		} catch (IOException e) {
			f_song.close();
			return null;
		}

		f_song.close();

		Writer output = new BufferedWriter(new FileWriter(intFile));
		output.write((fileInt + 1) + "");
		output.close();
		return (Controller.MP3_PATH + fileInt + ".mp3");
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[32 * 1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, bytesRead);
		}
	}
}
