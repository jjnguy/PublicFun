package api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

import entities.Question;
import entities.User;

public class SoApi {

	private static int DEFAULT_PAGE = 1;
	private static int DEFAULT_PAGESIZE = 100;

	private static final String baseUrls = "http://api.stackoverflow.com/";
	private static String key;

	public SoApi(String key) {
		SoApi.key = key;
	}

	public Question getQuestionById(long id) throws JSONException, IOException {
		String urlS = baseUrls;
		urlS += "questions/" + id + "?body=true&key=" + key;
		Question q = JSONParser.parseQuestion(jsonRequest(urlS));
		return q;
	}

	public List<Question> getListOfQuestions(Sort sort) {
		return getListOfQuestions(sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfQuestions(Sort sort, int page, int pageSize) {
		// http://api.stackoverflow.com/questions/active - default is active
		// http://api.stackoverflow.com/questions/newest - newest questions
		// http://api.stackoverflow.com/questions/votes - highest votes
		return null; // TODO
	}

	public List<Question> getListOfFeaturedQuestions() {
		return getListOfFeaturedQuestions(DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfFeaturedQuestions(int page, int pageSize) {
		// http://api.stackoverflow.com/questions/featured - bounty questions
		return null; // TODO
	}

	public List<Question> getListofHotQuestions(Hottness hottness) {
		return getListofHotQuestions(hottness, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListofHotQuestions(Hottness hottness, int page, int pageSize) {
		// http://api.stackoverflow.com/questions/hot - all time hot questions
		// http://api.stackoverflow.com/questions/week - weekly hot questions
		// http://api.stackoverflow.com/questions/month - monthly hot questions
		return null; // TODO
	}

	public List<Question> getListOfUnansweredQuestions(Sort sort) {
		return getListOfUnansweredQuestions(sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfUnansweredQuestions(Sort sort, int page, int pageSize) {
		// http://api.stackoverflow.com/questions/unanswered/newest - newest unanswered questions
		// http://api.stackoverflow.com/questions/unanswered/votes - highest voted unanswered questions
		return null; // TODO
	}

	public List<Question> getListOfQuestionsFromUser(int userId, Sort sort) {
		return getListOfQuestionsFromUser(userId, sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfQuestionsFromUser(int userId, Sort sort, int page, int pageSize) {
		// http://api.stackoverflow.com/users/{id}/questions/recent - questions created by user: id with recent activity
		// http://api.stackoverflow.com/users/{id}/questions/views- questions created by user: id with highest views
		// http://api.stackoverflow.com/users/{id}/questions/newest- questions created by user: id recently
		// http://api.stackoverflow.com/users/{id}/questions/votes- questions created by user: id with the highest votes
		return null; // TODO
	}

	public List<Question> getListOfFavoriteQuestionsFromUser(int userId, Sort sort) {
		return getListOfFavoriteQuestionsFromUser(userId, sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfFavoriteQuestionsFromUser(int userId, Sort sort, int page, int pageSize) {
		// http://api.stackoverflow.com/users/{id}/favorites/recent - questions marked as favorite by user: id with
		// recent activity
		// http://api.stackoverflow.com/users/{id}/favorites/views- questions marked as favorite by user: id with
		// highest views
		// http://api.stackoverflow.com/users/{id}/favorites/newest- questions marked as favorite by user: id recently
		// http://api.stackoverflow.com/users/{id}/favorites/added - questions marked as favorite in the order they were
		// marked by user: id
		return null; // TODO
	}

	public User getUserById(int id) {
		// TODO
		return null;
	}

	public static String key() {
		return key;
	}

	private String jsonRequest(String urlS) throws IOException {
		URL url = new URL(urlS);
		BufferedInputStream stream = new BufferedInputStream(url.openStream());
		Scanner s = new Scanner(stream);
		String ret = "";
		while (s.hasNextLine()) {
			ret += s.nextLine();
		}
		return ret;
	}

	public static enum Hottness {
		ALL_TIME, MONTH, WEEK;
	}

	public static enum Sort {
		ACTIVE, NEWEST, VOTES, VIEWS, ADDED;
	}
}
