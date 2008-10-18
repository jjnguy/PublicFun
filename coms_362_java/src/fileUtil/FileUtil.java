package fileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	private FileUtil() {
	}

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

	static class SongFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
