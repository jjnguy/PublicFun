import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServletPart3 extends HttpServlet {
	private static final String CONTENT_BASE_DIR_NAME = "C:/webcontent";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InputStream in = req.getInputStream();

		try {
			File f = new File(CONTENT_BASE_DIR_NAME + req.getPathInfo());

			// (resp.getOutputStream(), f);

			if (f.isDirectory()) {
				// handleDirectoryRequest(out, f);
			} else {
				// catch FileNotFoundException if file doesn't exist
				FileOutputStream fos = new FileOutputStream(f);

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
				// out.flush();
				// out.close();
				in.close();
				return;
			}

		} catch (FileNotFoundException e) {
			// System.out.println("File not found: " + request);
			// out.println("HTTP/1.0 404 Not Found\r\n\r\n");
			// out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintStream out = new PrintStream(resp.getOutputStream());
		try {
			File f = new File(CONTENT_BASE_DIR_NAME + req.getPathInfo());
			// checkBelowAndHandle(out, f);

			if (f.isDirectory()) {
				// handleDirectoryRequest(out, f);
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
				// in.close();
				return;
			}
		} catch (FileNotFoundException e) {
			// System.out.println("File not found: " + request);
			out.println("HTTP/1.0 404 Not Found\r\n\r\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
