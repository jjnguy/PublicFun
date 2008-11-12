import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestLoad {
	public static void main(String[] args) throws FileNotFoundException, IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(
				"data/output")));
		try {
			while (true) {
				StudentData obj = (StudentData) in.readObject();
				System.out.println(obj);
			}
		} catch (EOFException e) {
			// Just stop
		}
	}
}
