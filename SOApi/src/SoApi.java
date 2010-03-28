import java.util.ArrayList;
import java.util.List;

import entities.Question;


public class SoApi {

	private final String key;
	
	public SoApi(String key){
		this.key = key;
	}
	
	public List<Question> getQuestionById(long id){
		List<Question> ret = new ArrayList<Question>();
		return null;
	}
	
}
