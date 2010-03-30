package entities;

import java.util.List;

import api.SoApi;
import api.SoApi.Sort;

public class User {

	private int userId;
	private int reputation;
	private long creationDate;
	private String displayName;
	private String emailHash;
	private int age;
	private String websiteUrl;
	private String location;

	public User(int userId, int reputation, long creationDate, String displayName, String emailHash, int age,
			String websiteUrl, String location) {
		this.userId = userId;
		this.reputation = reputation;
		this.creationDate = creationDate;
		this.displayName = displayName;
		this.emailHash = emailHash;
		this.age = age;
		this.websiteUrl = websiteUrl;
		this.location = location;
	}
	
	public int getUserId() {
		return userId;
	}

	public int getReputation() {
		return reputation;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getAge() {
		return age;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public String getLocation() {
		return location;
	}

	public List<Question> getAllQuestions(Sort sort, int page, int pageSize){
		SoApi api = new SoApi(SoApi.key());
		return api.getListOfQuestionsFromUser(userId, sort, page, pageSize);
	}
	
	public List<Question> getAllQuestions(Sort sort){
		return getAllQuestions(sort, SoApi.DEFAULT_PAGE, SoApi.DEFAULT_PAGESIZE);
	}
	
	public List<Question> getAllQuestions(){
		return getAllQuestions(Sort.VOTES, SoApi.DEFAULT_PAGE, SoApi.DEFAULT_PAGESIZE);
	}
	
}
