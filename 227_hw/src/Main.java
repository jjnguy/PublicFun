import hw4.AccessibleNumber;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		AccessibleNumber n = new AccessibleNumber(-2112376215);
		n.getAudio("number_wavs").writeTo("out.wav");
	}
}
