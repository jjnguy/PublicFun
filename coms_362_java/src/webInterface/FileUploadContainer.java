package webInterface;

import id3TagStuff.ID3v2_XTag;

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

public class FileUploadContainer {

	public static final String UPLOAD_DIRECTORY = "C:/uploads/";

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

	public FileUploadContainer(HttpServletRequest request) {
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
		return ret;
	}
	
	public List<FileItem> getFileItems() {
		return fileItems;
	}
}
