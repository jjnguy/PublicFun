package webInterface;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3_Picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This class is used to manage the upload of an MP3 file.
 * 
 * @author Justin Nelson
 * 
 */
public class MP3FileUploadContainer {

	public static final String UPLOAD_DIRECTORY = "C:/uploads/";
	public static final String PICTURE_SAVE_DIR = UPLOAD_DIRECTORY + "pics/";

	static {
		File existTest = new File(UPLOAD_DIRECTORY);
		if (!existTest.exists()) {
			boolean created = existTest.mkdirs();
			if (!created) {
				// TODO throw new IOException("Could not access the uploads directory!");
			}
		}
	}

	private HttpServletRequest request;
	private List<FileItem> fileItems;
	/**
	 * Derived from fileItems
	 */
	private List<File> savedFilesLoc;
	private List<ID3v2_XTag> tags;

	public MP3FileUploadContainer(HttpServletRequest request) {
		this.request = request;

		DiskFileItemFactory f = new DiskFileItemFactory();
		ServletFileUpload serv = new ServletFileUpload(f);
		try {
			fileItems = serv.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		savedFilesLoc = createFileListFromFileItems();
		saveFilesToDisk();
	}

	private List<File> createFileListFromFileItems() {
		List<File> ret = new ArrayList<File>(fileItems.size());
		for (FileItem fItem : getFileItems()) {
			String originalPath = fItem.getName();
			String originalFileName;
			int sepIdx = originalPath.indexOf(File.separator);
			if (sepIdx == -1) {
				originalFileName = originalPath;
			} else {
				originalFileName = originalPath.substring(originalPath
						.lastIndexOf(File.separatorChar));
			}
			ret.add(new File(UPLOAD_DIRECTORY + originalFileName));
		}
		return ret;
	}

	private void saveFilesToDisk() {
		for (int i = 0; i < fileItems.size(); i++) {
			try {
				InputStream fileItemInputStream = fileItems.get(i).getInputStream();
				OutputStream saveFileStream = new FileOutputStream(savedFilesLoc.get(i));
				while (fileItemInputStream.available() > 0) {
					saveFileStream.write(fileItemInputStream.read());
				}
				saveFileStream.close();
				fileItemInputStream.close();
			} catch (IOException e) {

			} finally {
				// close streams
			}
		}
	}

	public List<ID3v2_XTag> getListOfTags() {
		if (tags != null)
			return tags;
		List<ID3v2_XTag> ret = new ArrayList<ID3v2_XTag>(savedFilesLoc.size());
		for (File file : savedFilesLoc) {
			try {
				ret.add(new ID3v2_XTag(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret.add(null);
				System.out.println("A null tag was created due to a IO error.");
			}
		}
		tags = ret;
		return ret;
	}

	public List<String> getHTMLRepresentation() {
		List<String> ret = new ArrayList<String>(fileItems.size());

		for (ID3v2_XTag tag : getListOfTags()) {
			String html = "";
			List<ID3v2_XFrame> frames;
			frames = tag.getAllFrames();
			for (ID3v2_XFrame frame : frames) {
				html += frame.getEnglishTagDescription() + "<br>";
				html += frame.getData() + "<br>";
				html += "<br>";
			}
			ret.add(html);
		}
		return ret;
	}

	/**
	 * 
	 * @param index
	 * @return true if a picture was saved, false otherwise.
	 */
	public boolean savePicsFromTag(int index) {
		boolean ret = false;
		ID3v2_XTag tag = getListOfTags().get(index);
		int picNum = 0;
		for (ID3v2_XFrame frame : tag.getAllFrames()) {
			if (frame.getFrameType().matches("APIC|PIC")) {
				ID3_Picture pic = (ID3_Picture) frame.getData();
				String picLoc = PICTURE_SAVE_DIR + savedFilesLoc.get(index).getName() + picNum
						+ ".png";
				System.out.println("Saving pic in location: " + picLoc);
				File picSaveFile = new File(picLoc);
				System.out.println("Created the file obj");
				pic.getType();
				try {
					pic.saveAs(picSaveFile);
					ret = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public List<FileItem> getFileItems() {
		return fileItems;
	}
}
