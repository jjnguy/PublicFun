import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Test client for use with SimpleServer.
 */
public class SimpleClient {

	public static void main(String[] args) {
		Socket s = null;
		try {
			// open a connection to the server on port 2222
			s = new Socket("localhost", 2222);
			OutputStream out = s.getOutputStream();

			// for line-oriented output we use a PrintWriter
			PrintWriter pw = new PrintWriter(out);
			pw.println("POST /aPic24.png HTTP/1.1");
			pw.print("\r\n"); // empty line

			pw.flush();

			InputStream fout = new FileInputStream(new File("C:/Documents and Settings/jnelson/My Documents/My Pictures/classScheduleWithWorkTime.bmp"));
			copyStream(fout, out);
			
			fout.close();
			out.close();
			pw.close();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (s != null)
					s.close();
			} catch (IOException ignore) {
			}
		}
	}
	
	private static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[32 * 1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, bytesRead);
		}
	}
}
