package entities;

import api.SoApi;

public class Comment {

	private int ownerId;
	private User owner;
	private long postOwner;
	private int votes;
	private boolean onQuestion;
	private long creationDate;
	private String body;

	public Comment(int ownerId, long postOwner, int votes, boolean onQuestion, long creationDate, String body) {
		this.ownerId = ownerId;
		SoApi api = new SoApi(SoApi.key());
		this.owner = api.getUserById(ownerId);
		this.postOwner = postOwner;
		this.votes = votes;
		this.onQuestion = onQuestion;
		this.creationDate = creationDate;
		this.body = body;
	}

}
