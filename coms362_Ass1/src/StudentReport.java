import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean saveToObjFile(File loc) {
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

	public boolean saveToRaccessFile(File loc) {
		// first build data
		// this map will contain lists of students keyed by course ID
		Map<String, List<StudentData>> data = new HashMap<String, List<StudentData>>();
		// for each student add his or her data to the map entry of that class id
		for (StudentData stu : students) {
			// now we get every class this student is in
			for (Course course : stu.getCourses()) {
				// get that class from the map and add the students name to it
				data.get(course.getName()).add(stu);
			}
		}
		int numCourses = data.size();
		// now what the fuck TODO

		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(loc, "rw");
		} catch (FileNotFoundException e) {
			return false;
		}

		try {
			file.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
