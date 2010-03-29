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
	
	public List<Question> getListOfActiveQuestions(){
		return getListOfActiveQuestions(1, 100);
	}
	
	public List<Question> getListOfActiveQuestions(int page, int pageSize){
		return null; // TODO
	}
	
	public List<Question> getListOfNewestQuestions(){
		return getListOfNewestQuestions(1, 100);
	}
	
	public List<Question> getListOfNewestQuestions(int page, int pageSize){
		return null; // TODO
	}
	
	public List<Question> getListOfFeaturedQuestions(){
		return getListOfFeaturedQuestions(1, 100);
	}
	
	public List<Question> getListOfFeaturedQuestions(int page, int pageSize){
		return null; // TODO
	}
	
	public List<Question> getHighestVotedQuestions(){
		return getHighestVotedQuestions(1, 100); // TODO
	}
	
	public List<Question> getHighestVotedQuestions(int page, int pageSize){
		return null; // TODO
	}
	
/*
		http://api.stackoverflow.com/questions - default is active
		http://api.stackoverflow.com/questions/active - recent activity
		http://api.stackoverflow.com/questions/newest - newest questions
		http://api.stackoverflow.com/questions/featured - bounty questions
		http://api.stackoverflow.com/questions/hot - all time hot questions
		http://api.stackoverflow.com/questions/week - weekly hot questions
		http://api.stackoverflow.com/questions/month - monthly hot questions
		http://api.stackoverflow.com/questions/votes - highest votes
		http://api.stackoverflow.com/questions/unanswered - default is newest
		http://api.stackoverflow.com/questions/unanswered/newest - newest unanswered questions
		http://api.stackoverflow.com/questions/unanswered/votes - highest voted unanswered questions

		Questions created by a specific user {id}:
		http://api.stackoverflow.com/users/{id}/questions - default is recent
		http://api.stackoverflow.com/users/{id}/questions/recent - questions created by user: id with recent activity
		http://api.stackoverflow.com/users/{id}/questions/views- questions created by user: id with highest views
		http://api.stackoverflow.com/users/{id}/questions/newest- questions created by user: id
		recently
		http://api.stackoverflow.com/users/{id}/questions/votes- questions created by user: id
		with the highest votes

		Questions marked as favorite by a specific user {id}:
		http://api.stackoverflow.com/users/{id}/favorites - default is recent
		http://api.stackoverflow.com/users/{id}/favorites/recent - questions marked as favorite by user: id with recent activity
		http://api.stackoverflow.com/users/{id}/favorites/views- questions marked as favorite by user: id with highest views
		http://api.stackoverflow.com/users/{id}/favorites/newest- questions marked as favorite by user: id
		recently
		http://api.stackoverflow.com/users/{id}/favorites/added - questions marked as favorite in the order they were marked by user: id 
*/	
	
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

}
