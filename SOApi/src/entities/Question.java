package entities;

import api.SoApi;

public class Question extends Post {

	private String title;
	protected Tag[] tags;
	private int answerCount;
	private Answer[] answers;
	private int views;
	private long acceptedAnswer;

	public Question(long id, int answerCount, int ownerId, String title, int upVotes, int downVotes, int viewCount,
			long creationDate, String tags, String body, Answer[] answers, Comment[] comments) {
		this(id, answerCount, ownerId, title, upVotes, downVotes, viewCount, creationDate, tags);
		this.body = body;
		this.answers = answers;
		this.comments = comments;
	}

	public Question(long id, int answerCount, int ownerId, String title, int upVotes, int downVotes, int viewCount,
			long creationDate, String tags) {
		this.id = id;
		this.answerCount = answerCount;
		SoApi api = new SoApi(SoApi.key());
		this.owner = api.getUserById(ownerId);
		this.title = title;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
		this.views = viewCount;
	}

	public String getTitle() {
		return title;
	}

	public Tag[] getTags() {
		return tags;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public Answer[] getAnswers() {
		return answers;
	}

	public int getViews() {
		return views;
	}

	public long getAcceptedAnswer() {
		return acceptedAnswer;
	}

}
