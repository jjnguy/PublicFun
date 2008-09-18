import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChatServer {

	public static void main(String[] args) throws IOException {
		String inputFile = "src/ChatServer.java";
		FileInputStream in = new FileInputStream(inputFile);
		FileChannel ch = in.getChannel();
		ByteBuffer buf = ByteBuffer.allocateDirect(256); // BUFSIZE = 256
		
		/* read the file into a buffer, 256 bytes at a time */
		int rd;
		while ((rd = ch.read(buf)) > -1) {
			buf.rewind();
			for (int i = 0; i < rd / 2; i++) {
				/* print each character */
				System.out.print((char)buf.get());
			}
			buf.clear();
		}
	}

}
