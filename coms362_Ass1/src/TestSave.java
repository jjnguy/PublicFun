import java.io.File;
import java.io.IOException;

public class TestSave {
	public static void main(String[] args) throws IOException {
		StudentReport r = new StudentReport(new File("data/testData"));
		r.saveToFile(new File("data/output"));
	}
}
