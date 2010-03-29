package api;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Answer;
import entities.Question;

public class JSONParser {

	public static Question parseQuestion(String questionJSON) throws JSONException {
		JSONObject jQ = new JSONObject(questionJSON);
		return questionFromJSONObject(jQ.getJSONObject("Question"));
	}

	public static Question questionFromJSONObject(JSONObject jQ) throws JSONException {
		Question ret = new Question(jQ.getLong("PostId"), jQ.getInt("TotalAnswers"), jQ.getInt("OwnerUserId"), jQ
				.getString("Title"), jQ.getInt("UpVoteCount"), jQ.getInt("DownVoteCount"), jQ.getInt("ViewCount"), jQ
				.getLong("CreationDate"), jQ.getString("Tags"), jQ.optString("Body"), null, null); //TODO
		return ret;
	}

	public static List<Question> parseListOfQuestions(String questionsJSON) throws JSONException {
		List<Question> ret = new ArrayList<Question>();
		JSONArray array = new JSONArray(questionsJSON);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			ret.add(questionFromJSONObject(obj));
		}
		return ret;
	}

	public static Answer answerFromJSONObject(JSONObject jA){
		Answer ret = new Answer();
		return null;
	}
	
}
