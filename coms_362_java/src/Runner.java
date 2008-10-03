import java.io.File;
import java.io.IOException;

import id3TagStuff.ID3v2_XTag;

public class Runner {
	public static void main(String[] args) throws IOException {
		System.out.println(new ID3v2_XTag(new File("src/12 Sex Rap.mp3")));
	}
}
