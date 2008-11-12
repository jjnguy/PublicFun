import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentData implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 9189478728462445675L;
	
	private String id;
	private String name;
	private List<SchoolClass> classes;

	public StudentData(String lineFromFile) {
		String[] data = lineFromFile.split(";");
		int posInData = 0;
		id = data[posInData++];
		name = data[posInData++];

		classes = new ArrayList<SchoolClass>();

		for (; posInData < data.length; posInData += 2) {
			classes.add(new SchoolClass(data[posInData].trim(), Grade
					.stringToGrade(data[posInData + 1].trim())));
		}
	}
	
	@Override
	public String toString() {
		return String.format("Name: %10s, ID: %5s, Number Of Classes: %2d", name, id, classes.size());
	}
}
