package api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Answer;
import entities.Comment;
import entities.Question;
import entities.User;

public class JSONParser {

	public static Question parseQuestion(String questionJSON) throws JSONException {
		JSONObject jQ = new JSONObject(questionJSON);
		return questionFromJSONObject(jQ.getJSONObject("question"));
	}

	public static Question questionFromJSONObject(JSONObject jQ) throws JSONException {
		Comment[] comments = parseListOfComments(jQ.getJSONArray("comments"));
		Answer[] answers = parseListOfAnswers(jQ.getJSONArray("answers"));
		Question ret = new Question(jQ.getLong("question_id"), jQ.getInt("total_answers"), jQ.getInt("owner_user_id"), jQ
				.getString("title"), jQ.getInt("up_vote_count"), jQ.getInt("down_vote_count"), jQ.getInt("view_count"), jQ
				.getLong("creation_date"), jQ.getString("tags"), jQ.optString("body"), answers, comments);
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
		Answer ret = new Answer(jA.getLong("answer_id"), jA.getLong("question_id"), jA.getInt("user_id"), jA
				.getInt("up_vote_count"), jA.getInt("down_vote_count"), jA.getLong("creation_date"));
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
		Comment ret = new Comment(jC.getLong("comment_id"), jC.getInt("user_id"), jC.getLong("post_id"), jC
				.getInt("vote_count"), jC.getBoolean("on_question"), jC.getLong("creation_date"), jC.getString("body"));
		return ret;
	}

	public static User[] parseListOfUsers(String usersJSON) throws JSONException{
		return parseListOfUsers(new JSONArray(usersJSON));
	}
	
	public static User[] parseListOfUsers(JSONArray userArr) throws JSONException{
		User[] users = new User[userArr.length()];
		for (int i = 0; i < userArr.length(); i++){
			users[i] = userFromJSONObject(userArr.getJSONObject(i));
		}
		return users;
	}
	
	public static User userFromJSONObject(JSONObject jU) throws JSONException {
		User ret = new User(jU.getInt("user_id"), jU.getInt("reputation"), jU.getLong("creation_date"), jU
				.getString("display_name"), jU.getString("email_hash"), jU.getInt("age"), jU.getString("website_url"), jU
				.getString("location"));
		return ret;
	}
}
