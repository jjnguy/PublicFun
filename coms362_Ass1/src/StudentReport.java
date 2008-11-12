import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentReport {
	private List<StudentData> students;

	public StudentReport(File studentDataFile) throws IOException {
		students = new ArrayList<StudentData>();
		LineNumberReader in = new LineNumberReader(new FileReader(studentDataFile));
		String line = in.readLine();
		while (line != null) {
			students.add(new StudentData(line));
			line = in.readLine();
		}
	}

	public boolean saveToFile(File loc) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(loc));
		} catch (IOException e) {
			return false;
		}
		for (StudentData stu : students) {
			try {
				out.writeObject(stu);
				// out.writeChar('\n');
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
}
