package genericDataStructures;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Array {
	
	private List<Object> values;
	
	public Array(){
		values = new ArrayList<Object>();
	}
	
	public Array(Scanner s) {
		this();
		String line = s.nextLine().trim();
		while (!line.equals("</array>")) {
			// only supporting dicts for now
			if (line.equals("<dict>")) {
				values.add(new Dictionary(s));
			} else throw new IllegalArgumentException("The elment type " + line + " is not supported.");
			
			line = s.nextLine().trim();
		}
	}
	
	public List<Object> getList(){
		return values;
	}
	
	public int length(){
		return values.size();
	}
	
	public Object get(int index){
		return values.get(index);
	}
	
}
