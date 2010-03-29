package entities;

import api.SoApi;

public class Answer extends Post {

	public Answer(long id, long questionId, int ownerId, int upVotes, int downVotes, long creationDate) {
		this.id = id;
		SoApi api = new SoApi(SoApi.key());
		this.owner = api.getUserById(ownerId);
		this.upVotes = upVotes;
		this.downVotes = downVotes;
	}

}
