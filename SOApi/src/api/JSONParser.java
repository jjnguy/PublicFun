package api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Answer;
import entities.Comment;
import entities.Question;

public class JSONParser {

	public static Question parseQuestion(String questionJSON) throws JSONException {
		JSONObject jQ = new JSONObject(questionJSON);
		return questionFromJSONObject(jQ.getJSONObject("Question"));
	}

	public static Question questionFromJSONObject(JSONObject jQ) throws JSONException {
		Comment[] comments = parseListOfComments(jQ.getJSONArray("Comments"));
		Answer[] answers = parseListOfAnswers(jQ.getJSONArray("Answers"));
		Question ret = new Question(jQ.getLong("PostId"), jQ.getInt("TotalAnswers"), jQ.getInt("OwnerUserId"), jQ
				.getString("Title"), jQ.getInt("UpVoteCount"), jQ.getInt("DownVoteCount"), jQ.getInt("ViewCount"), jQ
				.getLong("CreationDate"), jQ.getString("Tags"), jQ.optString("Body"), answers, comments);
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

	public static Answer answerFromJSONObject(JSONObject jA) throws JSONException {
		Answer ret = new Answer(jA.getLong("PostId"), jA.getLong("QuestionId"), jA.getInt("OwnerUserId"), jA
				.getInt("UpVoteCount"), jA.getInt("DownVoteCount"), jA.getLong("CreationDate"));
		return ret;
	}

	public static Answer[] parseListOfAnswers(JSONArray answerArr) throws JSONException {
		Answer[] answers = new Answer[answerArr.length()];
		for (int i = 0; i < answerArr.length(); i++) {
			answers[i] = answerFromJSONObject(answerArr.getJSONObject(i));
		}
		return answers;
	}

	public static Comment[] parseListOfComments(JSONArray commentsArr) throws JSONException {
		Comment[] comments = new Comment[commentsArr.length()];
		for (int i = 0; i < commentsArr.length(); i++) {
			comments[i] = commentFromJSONObject(commentsArr.getJSONObject(i));
		}
		return comments;
	}

	public static Comment commentFromJSONObject(JSONObject jC) throws JSONException {
		Comment ret = new Comment(jC.getInt("UserId"), jC.getLong("PostId"), jC.getInt("VoteCount"), jC
				.getBoolean("OnQuestion"), jC.getLong("CreationDate"), jC.getString("Body"));
		return ret;
	}
}
