import java.io.IOException;

import hw4.AccessibleNumber;
import hw4.ShortAudio;

public class Main {
	public static void main(String[] args) throws IOException {
		AccessibleNumber n = new AccessibleNumber(1237210);
		n.getAudio("number_wavs").writeTo("out.wav");
	}
}
