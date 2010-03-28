package entities;

public class Post {

	protected long id;
	protected User owner;
	protected int upVotes;
	protected int downVotes;
	protected long creationDate;
	protected long lastEditedDate;
	protected long lastActivityDate;
	protected boolean isCommunityWiki;
	protected String body;
	protected Tag[] tags;
	protected Comment[] comments;

	
}
