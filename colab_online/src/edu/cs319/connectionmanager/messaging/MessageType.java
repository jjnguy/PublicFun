package edu.cs319.connectionmanager.messaging;

public enum MessageType {
	TEXT_ADDED((byte) 0x00, "Text Added"), TEXT_CHANGED((byte) 0x01, "Text Changed"), TEXT_DELETED(
			(byte) 0x02, "Text Deleted"), TEXT_HIGHLIGHTED((byte) 0x03, "Text Highlighted"), TEXT_UNHIGHLIGHTED(
			(byte) 0x04, "Text UnHighlighted"), NEW_MESSAGE((byte) 0x05, "New Message"), NEW_PRIVATE_MESSAGE(
			(byte) 0x06, "New Private Message"), NEW_CLIENT((byte) 0x07, "New Client"), MEMBER_JOIN_ROOM(
			(byte) 0x08, "Member Joined Room"), MEMBER_LEAVE_ROOM((byte) 0x09, "Member Left Room"), CHANGE_USER_PRIV(
			(byte) 0x0a, "Changed User Priv"), GET_ROOM_LIST((byte) 0x0b, "Get Room List");

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
		case 0:
			return TEXT_ADDED;
		case 1:
			return TEXT_CHANGED;
		case 2:
			return TEXT_DELETED;
		case 3:
			return TEXT_HIGHLIGHTED;
		case 4:
			return TEXT_UNHIGHLIGHTED;
		case 5:
			return NEW_MESSAGE;
		case 6:
			return NEW_PRIVATE_MESSAGE;
		case 7:
			return NEW_CLIENT;
		case 8:
			return MEMBER_JOIN_ROOM;
		case 9:
			return MEMBER_LEAVE_ROOM;
		case 10:
			return CHANGE_USER_PRIV;
		case 11:
			return GET_ROOM_LIST;
		default:
			return null;
		}
	}
}
