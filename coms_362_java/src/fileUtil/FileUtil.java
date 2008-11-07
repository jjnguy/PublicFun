package fileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to handle operations dealing with the filesystem.
 * 
 * @author Justin Nelson
 * 
 */
public class FileUtil {

	private FileUtil() {
	}

	/**
	 * Returns a list containing all of the song files located in the given directory
	 * determined by {@link SongFileFilter}.
	 * 
	 * @param topDirectory
	 *            the directory to search.
	 * @return List of song files. If the directory contains no files, then the List is empty.
	 */
	public static List<File> getAllSongFiles(File topDirectory) {
		FileFilter fileFilter = new SongFileFilter();
		List<File> ret = new ArrayList<File>();
		if (!topDirectory.isDirectory()) {
			if (fileFilter.accept(topDirectory)) {
				ret.add(topDirectory);
				return ret;
			}
		}
		File[] filesInDir = topDirectory.listFiles();
		for (File file : filesInDir) {
			if (file.isDirectory()) {
				ret.addAll(getAllSongFiles(file));
			} else {
				if (fileFilter.accept(file)) {
					ret.add(file);
				}
			}
		}
		return ret;
	}
}

class SongFileFilter implements FileFilter {

	// @Override
	public boolean accept(File pathname) {
		MP3FileFilter mp3 = new MP3FileFilter();
		AACFileFilter aac = new AACFileFilter();
		WAVFileFilter wav = new WAVFileFilter();
		return mp3.accept(pathname) && aac.accept(pathname) && wav.accept(pathname);
	}
}

class MP3FileFilter implements FileFilter {

	// @Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return false;
		String filename = pathname.getName();
		String extention = filename.substring(filename.lastIndexOf("."), filename.length());
		return extention.equalsIgnoreCase(".mp3");
	}
}

class WAVFileFilter implements FileFilter {

	// @Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return false;
		String filename = pathname.getName();
		String extention = filename.substring(filename.lastIndexOf("."), filename.length());
		return extention.equalsIgnoreCase(".wav");
	}
}

class AACFileFilter implements FileFilter {

	// @Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return false;
		String filename = pathname.getName();
		String extention = filename.substring(filename.lastIndexOf("."), filename.length());
		return extention.equalsIgnoreCase(".aac");
	}
}