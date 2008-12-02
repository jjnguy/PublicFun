package dataContainers;
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
	private List<Course> classes;

	public StudentData(String lineFromFile) {
		String[] data = lineFromFile.split(";");
		int posInData = 0;
		id = data[posInData++];
		name = data[posInData++];

		classes = new ArrayList<Course>();

		for (; posInData < data.length; posInData += 2) {
			classes.add(new Course(data[posInData].trim(), Grade
					.stringToGrade(data[posInData + 1].trim())));
		}
	}

	@Override
	public String toString() {
		return String.format("Name: %10s, ID: %5s, Number Of Classes: %2d", name, id, classes
				.size());
	}

	public String getID() {
		return id;
	}

	public List<Course> getCourses() {
		return classes;
	}
	
	public byte[] getBytes(){
		byte[] ret = new byte[id.getBytes().length + name.getBytes().length];
		System.arraycopy(id.getBytes(), 0, ret, 0, id.getBytes().length);
		System.arraycopy(name.getBytes(), 0, ret, id.getBytes().length, name.getBytes().length);
		return ret;
	}
}
