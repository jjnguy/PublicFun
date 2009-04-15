package edu.cs319.connectionmanager.messaging;

public enum MessageType {
	UPDATE_ALL_SUBSECTIONS((byte) 0x00, "Update All Subsections"), 
	UPDATE_SUBSECTION((byte) 0x01, "Update Subsection"), 
	SUBSECTION_LOCKED((byte) 0x02, "Subsection Locked"), 
	SUBSECTION_UNLOCKED((byte) 0x03, "Subsection Unlocked"), 
	NEW_SUBSECTION((byte) 0x04, "New Subsection"),
	NEW_MESSAGE((byte) 0x05, "New Message"), 
	NEW_PRIVATE_MESSAGE((byte) 0x06, "New Private Message"), 
	NEW_CLIENT((byte) 0x07, "New Client"), 
	MEMBER_JOIN_ROOM((byte) 0x08, "Member Joined Room"), 
	MEMBER_LEAVE_ROOM((byte) 0x09, "Member Left Room"), 
	MEMBERS_IN_ROOM((byte) 0x0a, "Members In Room"),
	CHANGE_USER_PRIV((byte) 0x0b, "Changed User Priv"), 
	GET_ROOM_LIST((byte) 0x0c, "Get Room List"), 
	NEW_COLAB_ROOM(	(byte) 0x0d, "New CoLab Room"), 
	COMMUNICATION_FAIL((byte) 0x0e,	"A previous communication attempt failed"),
	REMOVE_SUBSECTION((byte) 0x0f, "Remove A Subsection"),
	NEW_DOCUMENT((byte) 0x10, "New Document"),
	REMOVE_DOCUMENT((byte) 0x11, "Remove Document"),
	LOG_OUT((byte)0x12, "Log Out");

	private byte code;
	private String name;

	private MessageType(byte code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public byte getCode() {
		return code;
	}

	public static MessageType getMessageTypeByCode(byte code) {
		switch (code) {
		case 0x0:
			return UPDATE_ALL_SUBSECTIONS;
		case 0x1:
			return UPDATE_SUBSECTION;
		case 0x2:
			return SUBSECTION_LOCKED;
		case 0x3:
			return SUBSECTION_UNLOCKED;
		case 0x4:
			return NEW_SUBSECTION;
		case 0x5:
			return NEW_MESSAGE;
		case 0x6:
			return NEW_PRIVATE_MESSAGE;
		case 0x7:
			return NEW_CLIENT;
		case 0x8:
			return MEMBER_JOIN_ROOM;
		case 0x9:
			return MEMBER_LEAVE_ROOM;
		case 0xa:
			return MEMBERS_IN_ROOM;
		case 0xb:
			return CHANGE_USER_PRIV;
		case 0xc:
			return GET_ROOM_LIST;
		case 0xd:
			return NEW_COLAB_ROOM;
		case 0xe:
			return COMMUNICATION_FAIL;
		case 0xf:
			return REMOVE_SUBSECTION;
		case 0x10:
			return NEW_DOCUMENT;
		case 0x11:
			return REMOVE_DOCUMENT;
		case 0x12:
			return LOG_OUT;
		default:
			return null;
		}
	}
}
