import java.io.IOException;

import org.json.JSONException;

import api.SoApi;
import entities.Question;

public class Test {
	public static void main(String[] args) throws JSONException, IOException {
		SoApi api = new SoApi("knockknock");
		Question q = api.getQuestionById(239147L);
		System.out.println(q.getBody());
	}
}
