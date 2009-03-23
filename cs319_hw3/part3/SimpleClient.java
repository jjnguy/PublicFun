import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
			pw.println("POST /hi.txt HTTP/1.1");
			pw.println(); // empty line
			pw.println("asfdsafdafd");
			pw.println("asfdsafdafd");
			pw.println("asfdsafdafd");
			pw.println();
			pw.println();
			pw.flush(); // don't forget to flush...

			// read response, which we expect to be line-oriented
			InputStream in = s.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
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
}
