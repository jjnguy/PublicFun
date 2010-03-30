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
	protected Comment[] comments;
	
	public long getId() {
		return id;
	}
	public User getOwner() {
		return owner;
	}
	public int getUpVotes() {
		return upVotes;
	}
	public int getDownVotes() {
		return downVotes;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public long getLastEditedDate() {
		return lastEditedDate;
	}
	public long getLastActivityDate() {
		return lastActivityDate;
	}
	public boolean isCommunityWiki() {
		return isCommunityWiki;
	}
	public String getBody() {
		return body;
	}
	public Comment[] getComments() {
		return comments;
	}

}
