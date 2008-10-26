import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.tools.FileObject;

public class StackoverflowUserPage {

	public static final String userpageHost = "http://stackoverflow.com/users/";

	public static void main(String[] args) throws IOException, URISyntaxException {
		int userNumber = 2598;
		URL url = new URL(userpageHost + userNumber);
		final String saveLoc = url.getPath();
		String delim = "/";
		String replaceWith = "_";
		String fname = url.getFile().replace(delim, replaceWith) + ".html";
		File saveFile = new File(fname);
		InputStream in = url.openStream();
		OutputStream out = new FileOutputStream(saveFile);
		while (in.available() > 0) {
			out.write(in.read());
		}
	}

}
