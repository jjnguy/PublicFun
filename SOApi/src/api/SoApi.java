package api;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

	public Question getQuestionById(long id) throws JSONException, IOException{
		String urlS = baseUrls;
		urlS += "questions/" + id + "?body=true&key=" + key;
		Question q = JSONParser.parseQuestion(jsonRequest(urlS));
		return q;
	}

	public static String key(){
		return key;
	}
	
	public User getUserById(int id){
		// TODO
		return null;
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
