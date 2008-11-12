import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Program {

	private File objectFileLoc;
	private File rAccesssFileLoc;

	private Map<String, StudentData> studentInfo;

	public Program(File objectFile, File rAccFile) throws FileNotFoundException, IOException,
			ClassNotFoundException {
		objectFileLoc = objectFile;
		rAccesssFileLoc = rAccFile;
		studentInfo = loadObjFileToMap();
	}

	public StudentData getStudentData(String ID) {
		return studentInfo.get(ID);
	}

	public Collection<StudentData> getAllStudents() {
		return studentInfo.values();
	}

	private Map<String, StudentData> loadObjFileToMap() throws FileNotFoundException,
			IOException, ClassNotFoundException {
		List<StudentData> students = new ArrayList<StudentData>();
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(objectFileLoc));
		try {
			while (true) {
				students.add((StudentData) in.readObject());
			}
		} catch (EOFException e) {
			// stop
		}
		Map<String, StudentData> ret = new HashMap<String, StudentData>();
		for (StudentData studentData : students) {
			ret.put(studentData.getID(), studentData);
		}
		return ret;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException,
			ClassNotFoundException {
		Scanner stdin = new Scanner(System.in);
		System.out.println("Welsome to the Student Info Data Search Tool!!!");
		System.out.print("Please find the object file for me: ");
		String loc = stdin.nextLine();
		File objFile = new File(loc);
		Program p = new Program(objFile, null);
		System.out.print("Enter an ID to search for: ");
		String id = stdin.nextLine();
		StudentData sData = p.getStudentData(id);
		System.out.println(sData);
	}
}
