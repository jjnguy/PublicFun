package entities;

public class Question extends Post {

	private String title;
	private int answerCount;
	private Answer[] answers;
	private int views;
	private long acceptedAnswer;

	public Question(long id, int ownerId, String title, String body, int upVotes, int downVotes, int viewCount,
			Tag[] tags, Answer[] answers, Comment[] comments) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
		this.views = viewCount;
		this.tags = tags;
		this.answers = answers;
		this.comments = comments;
	}

	public Question(long id, int answerCount, int ownerId, String title, int upVotes, int downVotes, int viewCount,
			long creationDate, String tags) {
		this.id = id;
		this.title = title;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
		this.views = viewCount;
		this.answerCount = answerCount;
	}
}
