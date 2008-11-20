package controller;

import java.io.InputStream;

import webViewInterface.UploadSong;

public class Controller implements UploadSong {

	@Override
	public String uploadSong(InputStream fileStream) {
		// TODO Needs to call the appropriate classes to store
		// the song into a DB and save the file in the correct location
		return "OOps, this isn't implemented yet";
	}
	
}
