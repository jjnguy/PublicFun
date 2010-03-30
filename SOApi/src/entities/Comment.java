package entities;

import api.SoApi;

public class Comment {

	private long commnentId;
	private int ownerId;
	private User owner;
	private long postId;
	private int votes;
	private boolean onQuestion;
	private long creationDate;
	private String body;

	public long getCommnentId() {
		return commnentId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public User getOwner() {
		return owner;
	}

	public long getPostId() {
		return postId;
	}

	public int getVotes() {
		return votes;
	}

	public boolean isOnQuestion() {
		return onQuestion;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public String getBody() {
		return body;
	}

	public Comment(long commentId, int ownerId, long postId, int votes, boolean onQuestion, long creationDate,
			String body) {
		this.commnentId = commentId;
		this.ownerId = ownerId;
		SoApi api = new SoApi(SoApi.key());
		this.owner = api.getUserById(ownerId);
		this.postId = postId;
		this.votes = votes;
		this.onQuestion = onQuestion;
		this.creationDate = creationDate;
		this.body = body;
	}

}
