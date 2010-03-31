package api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

import entities.Badge;
import entities.Question;
import entities.User;

public class SoApi {

	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGESIZE = 100;

	private static final String baseUrlS = "http://api.stackoverflow.com/";
	private static final String versionS = "0.5/";
	private static String key;

	public SoApi(String key) {
		SoApi.key = key;
	}

	public SoApi(){
		this(SoApi.key());
	}
	
	public Question getQuestionById(long id) throws JSONException, IOException {
		String urlS = baseUrlS + versionS;
		urlS += "questions/" + id + "?body=true";
		Question q = JSONParser.parseQuestion(jsonRequest(urlS));
		return q;
	}

	public List<Question> getListOfQuestions(Question.Sort sort) {
		return getListOfQuestions(sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfQuestions(Question.Sort sort, int page, int pageSize) {
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

	public List<Question> getListOfUnansweredQuestions(Question.SortUnanswered sort) {
		return getListOfUnansweredQuestions(sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfUnansweredQuestions(Question.SortUnanswered sort, int page, int pageSize) {
		// http://api.stackoverflow.com/questions/unanswered/newest - newest unanswered questions
		// http://api.stackoverflow.com/questions/unanswered/votes - highest voted unanswered questions
		return null; // TODO
	}

	public List<Question> getListOfQuestionsFromUser(int userId, Question.Sort sort) {
		return getListOfQuestionsFromUser(userId, sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfQuestionsFromUser(int userId, Question.Sort sort, int page, int pageSize) {
		// http://api.stackoverflow.com/users/{id}/questions/recent - questions created by user: id with recent activity
		// http://api.stackoverflow.com/users/{id}/questions/views- questions created by user: id with highest views
		// http://api.stackoverflow.com/users/{id}/questions/newest- questions created by user: id recently
		// http://api.stackoverflow.com/users/{id}/questions/votes- questions created by user: id with the highest votes
		return null; // TODO
	}

	public List<Question> getListOfFavoriteQuestionsFromUser(int userId, User.Sort sort) {
		return getListOfFavoriteQuestionsFromUser(userId, sort, DEFAULT_PAGE, DEFAULT_PAGESIZE);
	}

	public List<Question> getListOfFavoriteQuestionsFromUser(int userId, User.Sort sort, int page, int pageSize) {
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
		// http://api.stackoverflow.com/users/{id} - pull only the user passed in by id
		return null; // TODO
	}
	
	public List<User> getListOfUsers(){
		return getListOfUsers(User.Sort.REPUTATION, SoApi.DEFAULT_PAGE, SoApi.DEFAULT_PAGESIZE, "");
	}
	
	public List<User> getListOfUsers(User.Sort sort, int page, int pageSize, String filter){
		// http://api.stackoverflow.com/users/reputation - users by reputation score
		// http://api.stackoverflow.com/users/newest - newest users
		// http://api.stackoverflow.com/users/oldest - oldest users
		// http://api.stackoverflow.com/users/name - users by name 
		return null; // TODO
	}
	
	public List<Badge> getListOfAllBadges(){
		// http://api.stackoverflow.com/badges/name  - default is name
		return null; // TODO
	}
	
	public List<Badge> getListOfTagBadges(){
		// http://api.stackoverflow.com/badges/tags - tag based badges earned
		return null; // TODO
		
	}

	public List<Badge> getListOfBadgesForUser(int userId){
		// http://api.stackoverflow.com/users/{id}/badges - badges awarded to user {id}
		return null; // TODO
	}
	
	public static String key() {
		return key;
	}

	private String jsonRequest(String urlS) throws IOException {
		URL url = new URL(urlS + "&key=" + key);
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
}
