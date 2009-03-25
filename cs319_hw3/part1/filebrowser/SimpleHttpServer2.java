package filebrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Another simple http server. This version improves on <code>SimpleHttpServer</code> in the following ways:
 * <ul>
 * <li>Sends response headers consistent with HTTP 1.0
 * <li>Sends Content-Type and Content-Length headers
 * <li>Generates a directory listing if the URL provided by the client is a directory
 * <li>Attempts to identify the MIME type for the file in the response header
 * <li>Only allows clients access to files and directories below a specified content directory
 * </ul>
 */
public class SimpleHttpServer2 {

	/**
	 * Base directory for all files made available from this server.
	 */
	private static final String CONTENT_BASE_DIR_NAME = "C:/webcontent";
	public static final String APP_DIR = "/filebrowser/listfiles";

	/**
	 * Starts an instance of the server.
	 * 
	 * @param args
	 *            port number on which the server will listen (optional, defaults to 2222)
	 */
	public static void main(String args[]) {
		// final int port = args.length > 0 ? Integer.parseInt(args[0]) : 2222;
		// new SimpleHttpServer2().runServer(port);
		new SimpleHttpServer2().runServer(args.length > 0 ? Integer.parseInt(args[0]) : 2222);
	}

	/**
	 * Basic server loop. Note this version has the following potential deficiencies:
	 * <ul>
	 * <li>if an I/O error occurs, the server will exit rather than attempting to re-create the server socket.
	 * <li>the server is single-threaded, that is, while handling a connection, new connections cannot be accepted.
	 * </ul>
	 * 
	 * @param port
	 *            the port number on which to listen
	 */
	public void runServer(int port) {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			while (true) {
				System.out.println("Server listening on " + port);

				// blocks here until a client attempts to connect
				Socket s = ss.accept();
				handleConnection(s);
			}
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
			System.out.println("Server will now exit.");
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException ignore) {
				}
			}
		}
	}

	/**
	 * Read line method for an InputStream. No buffering and doesn't waste data. Similar to StreamReader.readLine()
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readLine(InputStream in) throws IOException {
		String ret = "";
		while (true) {
			int next = in.read();
			if (next == -1)
				return ret.trim();
			if ((char) next == '\n')
				return ret.trim();
			ret += (char) next;
		}
	}

	/**
	 * Helper method for handling a client connection. If the file is a directory, a directory listing is generated as a
	 * text file. Closes the socket (and therefore the associated streams) when the method returns.
	 * 
	 * @param s
	 *            Socket representing the client connection
	 * @throws IOException
	 */
	private void handleConnection(Socket s) {
		InputStream in = null;
		PrintStream out = null;
		try {
			in = s.getInputStream();
			out = new PrintStream(s.getOutputStream());

			List<String> headers = new ArrayList<String>();

			// first line is request
			String request = readLine(in);
			System.out.println("Initial Request: " + request);
			headers.add(request);

			// read and display remaining headers
			String line;
			while (true) {
				line = readLine(in);
				if (line.length() == 0) {
					break; // blank line is end of headers
				}
				headers.add(line);
				System.out.println("Header line read from request: " + line);
			}
			System.out.println("~end of headers~");

			parseRequest(headers, in, out);

		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException ignore) {
				}
			}
		}
	}

	/**
	 * Figures out the type of request and sends the data to the correct method to handle it.
	 * 
	 * @param headers
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private void parseRequest(List<String> headers, InputStream in, PrintStream out) throws IOException {
		// parse request
		if (headers.get(0).toUpperCase().startsWith("GET")) {
			parseGET(headers, out, in);
		} else if (headers.get(0).toUpperCase().startsWith("POST")) {
			parsePOST(headers, out, in);
		} else {
			System.out.println("Could not successfully parse request: " + headers);
			// This wasn't a well-formed request
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(out), true);
			writer.println("HTTP/1.0 400 Bad Request\r\n\r\n");
			writer
					.println("<html><body>It seems like you made a bad request.<br />Please try again...and this time don't screw up.</html></body>");
			writer.flush();
		}
	}

	/**
	 * Parses a GET request. Just breaks up the original request to find the correct file parts
	 * 
	 * @param headers
	 * @param out
	 * @param in
	 * @throws IOException
	 */
	private void parseGET(List<String> headers, PrintStream out, InputStream in) throws IOException {
		int i = headers.get(0).indexOf("GET");
		int j = headers.get(0).indexOf('/', i);
		String fileRequested;
		if (j > i + 3) {
			// strip off everything up until the first slash
			fileRequested = headers.get(0).substring(j + 1);

			// strip off anything after the filename (if there
			// is anything after the filename it is separated
			// by a space)
			j = fileRequested.indexOf(' ');
			if (j >= 0) {
				fileRequested = fileRequested.substring(0, j);
			}

			// strip off the base app dir
			if (!fileRequested.contains(APP_DIR.substring(1))) {
				System.out.println("GET did not contain propper dir beginning");
				System.out.println("Disallowed request");
				out.println("HTTP/1.0 403 Forbidden\r\n\r\n");
				out.println("<html><body>You do not have permission to access that file.  "
						+ "Are you trying to snoop arround?</html></body>");
				out.flush();
				return;
			}
			fileRequested = fileRequested.substring(APP_DIR.length() - 1);

			// if the request was just "/" we'll give them
			// a listing of the base directory
			if (fileRequested.equals("")) {
				fileRequested = ".";
			}

			handleFileRequestGET(fileRequested, headers, out, in);
		}
	}

	/**
	 * Parses a POST request. Just breaks up the original request to find the correct file parts
	 * 
	 * @param headers
	 * @param out
	 * @param in
	 * @throws IOException
	 */
	private void parsePOST(List<String> headers, PrintStream out, InputStream in) throws IOException {
		int indexOfPost = headers.get(0).indexOf("POST");
		int indexOfSlash = headers.get(0).indexOf('/', indexOfPost);
		final int lengthOfPost = 4;
		if (indexOfSlash <= indexOfPost + lengthOfPost) {
			System.out.println("There was no slash in the correct place.");
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(out), true);
			writer.println("HTTP/1.0 400 Bad Request\r\n\r\n");
			writer
					.println("<html><body>It seems like you made a bad request.<br />Please try again...and this time don't screw up.</html></body>");
			writer.flush();
			return;
		}
		// strip off everything up until the first slash
		String strippedRequest = headers.get(0).substring(indexOfSlash + 1);

		// strip off anything after the filename (if there
		// is anything after the filename it is separated
		// by a space)
		indexOfSlash = strippedRequest.indexOf(' ');
		if (indexOfSlash >= 0) {
			strippedRequest = strippedRequest.substring(0, indexOfSlash);
		}

		// strip off the base app dir
		if (!strippedRequest.contains(APP_DIR.substring(1))) {
			System.out.println("POST did not contain propper dir beginning");
			System.out.println("Disallowed request");
			out.println("HTTP/1.0 403 Forbidden\r\n\r\n");
			out.println("<html><body>You do not have permission to access that file.  "
					+ "Are you trying to snoop arround?</html></body>");
			out.flush();
			return;
		}
		strippedRequest = strippedRequest.substring(APP_DIR.length() - 1);

		// if the request was just "/" we'll give them
		// a listing of the base directory
		if (strippedRequest.equals("")) {
			strippedRequest = ".";
		}

		handleFileRequestPOST(strippedRequest, headers, out, in);
	}

	/**
	 * Processes one request for a file or directory.
	 * 
	 * @param request
	 *            the pathname for the requested file
	 * @param headers
	 * @param out
	 *            output stream for the client connection
	 * @throws IOException
	 */
	private void handleFileRequestGET(String request, List<String> headers, PrintStream out, InputStream in) {
		System.out.println("File requested: " + request);

		try {
			File f = new File(CONTENT_BASE_DIR_NAME + request);

			if (!checkBelowAndHandle(out, f))
				return;

			if (f.isDirectory()) {
				handleDirectoryRequest(out, f);
			} else {
				// catch FileNotFoundException if file doesn't exist
				FileInputStream fis = new FileInputStream(f);

				out.print("HTTP/1.0 200 OK\r\n");
				out.print("Content-Type: " + SimpleHttpServer2.guessMimeType(f) + "\r\n");
				out.print("Content-Length: " + f.length() + "\r\n\r\n");
				out.flush();

				// copy contents of file to output stream
				// loop uses array versions of read/write methods to
				// copy multiple bytes with each call
				byte[] data = new byte[64 * 1024];
				int bytesRead;
				while ((bytesRead = fis.read(data)) != -1) {
					out.write(data, 0, bytesRead);
				}
				out.flush();
				out.close();
				in.close();
				return;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + request);
			out.println("HTTP/1.0 404 Not Found\r\n\r\n");
			out.println("<html><body>404 File Not Found, not my problem.  Sorry.</body></html>");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Processes one request for a file or directory.
	 * 
	 * @param request
	 *            the pathname for the requested file
	 * @param headers
	 * @param out
	 *            output stream for the client connection
	 * @throws IOException
	 */
	private void handleFileRequestPOST(String request, List<String> headers, PrintStream out, InputStream in) {
		System.out.println("File given: " + request);

		try {
			File f = new File(CONTENT_BASE_DIR_NAME + request);
			if (!checkBelowAndHandle(out, f))
				return;
			if (f.exists()) {
				out.println("HTTP/1.0 409 Conflict\r\n\r\n");
				out.print("<html><body>It seems your file already exists...<br />"
						+ "you wouldn't be trying ot overwrite someones stuff, would ya?</html></body>");
				out.flush();
				return;
			}

			if (f.isDirectory()) {
				handleDirectoryRequest(out, f);
			} else {
				// catch FileNotFoundException if file doesn't exist
				FileOutputStream fos = new FileOutputStream(f);

				out.println("HTTP/1.0 201 Created\r\n\r\n");
				// Need to write post contents to file
				while (true) {
					if (in.available() != 0) {
						int next = in.read();
						if (next == -1)
							break;
						fos.write(next);
					}
				}
				fos.flush();
				fos.close();
				out.flush();
				out.close();
				in.close();
				return;
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + request);
			out.println("HTTP/1.0 404 Not Found\r\n\r\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Deals with all directory requests the same. Printing a list of files to the output.
	 * 
	 * @param out
	 *            the stream to prin to
	 * @param f
	 *            the file to look in
	 * @throws IOException
	 */
	private void handleDirectoryRequest(PrintStream out, File f) throws IOException {
		// create a dir listing as a text file
		String[] listing = SimpleHttpServer2.generateDirListing(f);
		out.print("HTTP/1.0 200 OK\r\n");
		out.print("Content-Type: text/plain\r\n");
		long contentLength = SimpleHttpServer2.getContentLengthFromStringArray(listing);
		out.print("Content-Length: " + contentLength + "\r\n\r\n");
		out.flush();
		for (String s : listing) {
			out.println(s);
		}
		out.flush();
		return;
	}

	/**
	 * Checks to see if a directory requested is fair game.
	 * 
	 * @param out
	 *            stream to print errors to
	 * @param f
	 *            the file to check
	 * @return true if all was good, false otherwise.
	 */
	private boolean checkBelowAndHandle(PrintStream out, File f) {
		// make sure the file is really in the content directory
		try {
			if (!SimpleHttpServer2.checkIsBelow(new File(CONTENT_BASE_DIR_NAME), f)) {
				System.out.println("Disallowed request");
				out.println("HTTP/1.0 403 Forbidden\r\n\r\n");
				out.println("<html><body>You do not have permission to access that file.  "
						+ "Are you trying to snoop arround?</html></body>");
				out.flush();
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Finds the total length of strings in a string array
	 * 
	 * @param str
	 * @return the length of the content
	 */
	private static long getContentLengthFromStringArray(String[] str) {
		long ret = 0;
		for (String s : str) {
			ret += s.length();
		}
		return ret;
	}

	/**
	 * Try to determine the MIME type for a file based on the file extension. This is a simple example that returns
	 * 'application/octet-stream' for unknown file types, which in this case, is almost all file types.
	 * <p>
	 * For a more realistic example try: <code>
	 *   javax.activation.MimetypesFileTypeMap mmp = 
	 *      new javax.activation.MimetypesFileTypeMap();
	 *   return mmp.getContentType(filename);
	 * </code>
	 * <p>
	 * Also see http://en.wikipedia.org/wiki/Internet_media_type
	 * 
	 * @param f
	 *            the file to evaluate
	 * @return the MIME type as a string
	 */
	public static String guessMimeType(File f) {
		String filename = f.getName().toLowerCase();

		if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		if (filename.endsWith("htm") || filename.endsWith("html")) {
			return "text/html";
		}
		if (filename.endsWith("txt")) {
			return "text/plain";
		}
		return "application/octet-stream";
	}

	/**
	 * Create a list of directory contents as a text file.
	 * 
	 * @param dir
	 *            the directory to be listed
	 * @return byte array capturing the output stream into which the text file was written
	 * @throws IOException
	 */
	private static String[] generateDirListing(File dir) throws IOException {
		System.out.println("generating dir listing for base " + dir);

		File[] files = dir.listFiles();
		String[] fileNames = new String[files.length];
		for (int i = 0; i < files.length; ++i) {
			System.out.println(files[i].getName());
			fileNames[i] = (files[i].isDirectory() ? "DIR:" : "") + files[i].getName();
		}
		return fileNames;
	}

	/**
	 * Determines whether a file or directory is beneath a given base directory. This involves finding the actual files
	 * in the file system to get their 'canonical' representations as File objects, in which the pathnames are absolute
	 * and contain no '.' or '..' elements.
	 * 
	 * @param f
	 *            file or directory to be checked
	 * @param base
	 *            directory against which to check
	 * @return true if f is below base in the filesystem
	 * @throws IOException
	 *             if there is an error in getting the canonical files
	 */
	private static boolean checkIsBelow(File base, File f) throws IOException {
		// convert to "canonical" files to normalize the pathnames
		base = base.getCanonicalFile();
		File current = f.getCanonicalFile();

		// make sure that some parent file of the given one
		// is the base directory
		while (current != null) {
			if (current.equals(base)) {
				return true;
			}
			current = current.getParentFile();
		}
		return false;
	}
}
